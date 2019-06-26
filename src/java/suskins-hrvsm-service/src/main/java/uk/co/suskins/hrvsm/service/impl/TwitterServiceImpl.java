package uk.co.suskins.hrvsm.service.impl;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;
import com.twitter.hbc.twitter4j.handler.StatusStreamHandler;
import com.twitter.hbc.twitter4j.message.DisconnectMessage;
import com.twitter.hbc.twitter4j.message.StallWarningMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import uk.co.suskins.hrvsm.model.exception.HSSMServiceException;
import uk.co.suskins.hrvsm.model.models.Tweet;
import uk.co.suskins.hrvsm.repository.TweetsRepository;
import uk.co.suskins.hrvsm.service.Config.TwitterConfig;
import uk.co.suskins.hrvsm.service.TwitterService;
import uk.co.suskins.hrvsm.service.nlp.TwitterPreprocessorService;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Service implementation for Twitter
 */
@Service
public class TwitterServiceImpl implements TwitterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterServiceImpl.class);
    private static final int PROCESSING_THREADS = 32;
    private final TwitterConfig twitterConfig;
    private final StatusesFilterEndpoint filterEndpoint;
    private final TwitterPreprocessorService twitterPreprocessorService;
    private final TweetsRepository tweetsRepository;
    private boolean twitterConnectionStatus;
    private final StatusListener statusListener = new StatusStreamHandler() {
        @Override
        public void onStatus(Status status) {
            LOGGER.debug("TwitterServiceImpl: statusListener Sending Status to Preprocessor");
            twitterPreprocessorService.addToQueue(status);
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            LOGGER.warn("TwitterServiceImpl: statusListener Deletion Notice {}", statusDeletionNotice);
            Tweet tweet = tweetsRepository.findByTweetId(statusDeletionNotice.getStatusId());
            if (Objects.nonNull(tweet)) {
                tweetsRepository.delete(tweet);
            }
        }

        @Override
        public void onTrackLimitationNotice(int limit) {
            LOGGER.debug("TwitterServiceImpl: statusListener Track Limitation Notice {}", limit);
        }

        @Override
        public void onScrubGeo(long user, long upToStatus) {
            LOGGER.warn("TwitterServiceImpl: statusListener Scrub Geo User {} Up To Status {}", user, upToStatus);
            Tweet tweet = tweetsRepository.findByTweetId(upToStatus);
            if (Objects.nonNull(tweet)) {
                tweetsRepository.delete(tweet);
            }
        }

        @Override
        public void onStallWarning(StallWarning warning) {
            LOGGER.warn("TwitterServiceImpl: statusListener Stall Warning {}", warning);
        }

        @Override
        public void onException(Exception e) {
            LOGGER.debug("TwitterServiceImpl: statusListener Exception {}", e.toString());
        }

        @Override
        public void onDisconnectMessage(DisconnectMessage message) {
            LOGGER.warn("TwitterServiceImpl: statusListener Disconnect Message {}", message);
            setTwitterConnectionStatus(false);
        }

        @Override
        public void onStallWarningMessage(StallWarningMessage warning) {
            LOGGER.warn("TwitterServiceImpl: statusListener Warning Message {}", warning);

        }

        @Override
        public void onUnknownMessageType(String s) {
            LOGGER.debug("TwitterServiceImpl: statusListener Unknown Message Type {}", s);
        }
    };
    private Twitter4jStatusClient twitter4jStatusClient;

    @Autowired
    public TwitterServiceImpl(TwitterConfig twitterConfig,
                              TwitterPreprocessorService twitterPreprocessorService,
                              TweetsRepository tweetsRepository) {
        this.twitterConfig = twitterConfig;
        this.twitterPreprocessorService = twitterPreprocessorService;
        this.tweetsRepository = tweetsRepository;

        //Configure endpoint
        filterEndpoint = new StatusesFilterEndpoint();
        filterEndpoint.locations(twitterConfig.getLocationList());
        filterEndpoint.trackTerms(twitterConfig.getTerms());
        filterEndpoint.languages(twitterConfig.getLanguages());

        //Set Twitter Connection to false
        setTwitterConnectionStatus(false);

        //Configure Client
        configureTwitterClient();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void twitterStopStream() {
        if (!getTwitterConnectionStatus()) {
            LOGGER.warn("twitterStopStream: Stream already stopped");
            throw new HSSMServiceException("TwitterServiceImpl: twitterStopStream Stream already stopped");
        }

        //Disconnect from Filter Endpoint
        LOGGER.info("TwitterServiceImpl: twitterStopStream Stopping Twitter Stream");
        twitter4jStatusClient.stop();
        configureTwitterClient();

        //Update Twitter Connection Status
        setTwitterConnectionStatus(false);
        LOGGER.info("TwitterServiceImpl: twitterStopStream Stopped Twitter Stream");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getTwitterConnectionStatus() {
        return twitterConnectionStatus;
    }

    /**
     * Sets TwitterConnectionStatus
     *
     * @param twitterConnectionStatus Boolean new value for TwitterConnectionStatus
     */
    private void setTwitterConnectionStatus(boolean twitterConnectionStatus) {
        this.twitterConnectionStatus = twitterConnectionStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void twitterStartStream() {
        if (getTwitterConnectionStatus()) {
            LOGGER.warn("TwitterServiceImpl: twitterStartStream Stream already started");
            throw new HSSMServiceException("TwitterServiceImpl: twitterStartStream Stream already started");
        }
        LOGGER.info("TwitterServiceImpl: twitterStartStream Starting Twitter Stream");

        //Connect and process
        twitter4jStatusClient.connect();
        for (int threads = 0; threads < PROCESSING_THREADS; threads++) {
            twitter4jStatusClient.process();
        }

        //Update Twitter Connection Status
        setTwitterConnectionStatus(true);

        LOGGER.info("TwitterServiceImpl: twitterStartStream Started Twitter Stream");
    }

    /**
     * {@inheritDoc}
     */
    public void configureTwitterClient() {
        //Configure OAuth
        Authentication authentication = new OAuth1(twitterConfig.getTwitterConsumerKey(),
                twitterConfig.getTwitterConsumerSecret(),
                twitterConfig.getTwitterAccessToken(),
                twitterConfig.getTwitterAccessSecret());

        //Create Vars
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();
        Client client = new ClientBuilder()
                .name("Hosebird-Filter-Client-01")
                .hosts(Constants.STREAM_HOST)
                .authentication(authentication)
                .endpoint(filterEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .build();
        ExecutorService service = Executors.newFixedThreadPool(PROCESSING_THREADS);

        //Create Twitter4jClient
        twitter4jStatusClient = new Twitter4jStatusClient(
                client, msgQueue, Lists.newArrayList(statusListener), service);
    }
}
