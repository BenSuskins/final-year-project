package uk.co.suskins.hrvsm.service.nlp.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import uk.co.suskins.hrvsm.model.models.ProcessedStatus;
import uk.co.suskins.hrvsm.service.nlp.HSClassifier;
import uk.co.suskins.hrvsm.service.nlp.PreprocessorService;
import uk.co.suskins.hrvsm.service.nlp.TwitterPreprocessorService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service implementation for Twitter Preprocessor
 */
@Service
public class TwitterPreprocessorServiceImpl implements TwitterPreprocessorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterPreprocessorServiceImpl.class);
    private static final String USERNAME_PATTERN =
            "(?:\\s|\\A)[@]+([A-Za-z0-9_]+)";
    private PreprocessorService preprocessorService;
    private HSClassifier hsClassifier;
    private BlockingQueue<Status> twitterQueue;
    private TwitterPreprocessor twitterPreprocessor;


    public TwitterPreprocessorServiceImpl() {
        twitterQueue = new LinkedBlockingQueue<>(1000);
    }

    @Autowired
    public TwitterPreprocessorServiceImpl(PreprocessorService preprocessorService,
                                          HSClassifier hsClassifier) {
        this.preprocessorService = preprocessorService;
        this.hsClassifier = hsClassifier;
        this.twitterQueue = new LinkedBlockingQueue<>(1000);
        this.twitterPreprocessor = new TwitterPreprocessor();
        preprocessQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToQueue(Status status) {
        twitterQueue.offer(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preprocessQueue() {
        twitterPreprocessor.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String preprocessDataItem(String data) {
        data = preprocessorService.removeEmojis(data);
        data = preprocessorService.removeUrls(data);
        data = preprocessorService.lower(data);
        data = preprocessorService.removeHashtags(data);
        data = removeAts(data);
        return data.trim();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean processTweet(Status status) {
        //Checks if conditions are true to process tweet
        return !isRetweet(status) &&
                hasCountryData(status) &&
                !isTruncated(status) &&
                !isFiveOrFewer(status);
    }

    //Check if Tweet is five tokens or fewer
    private boolean isFiveOrFewer(Status status) {
        if (status.getText().split("\\s+").length <= 5) {
            LOGGER.debug("TwitterPreprocessorServiceImpl: processTweet Ignored Tweet with Five Words or Fewer");
            return true;
        }
        return false;
    }

    //Check if Tweet is truncated
    private boolean isTruncated(Status status) {
        if (status.isTruncated()) {
            LOGGER.debug("TwitterPreprocessorServiceImpl: processTweet Ignored Truncated Tweet");
            return true;
        }
        return false;
    }

    //Check if Country Code is null
    private boolean hasCountryData(Status status) {
        try {
            status.getPlace().getCountryCode();
        } catch (NullPointerException ex) {
            return false;
        }
        return true;
    }

    //Check if Tweet is retweet
    private boolean isRetweet(Status status) {
        if (status.isRetweet()) {
            LOGGER.debug("TwitterPreprocessorServiceImpl: processTweet Ignored Retweet");
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeAts(String data) {
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(data);
        return matcher.replaceAll("").trim();
    }

    /**
     * Inner class used for Queue Processing.
     * This class is an infinite loop
     * which will try take an item from the
     * preprocessor queue, pre-process it
     * and pass it on to the classifier.
     */
    class TwitterPreprocessor extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    //Initialise variables
                    Status status = twitterQueue.take();

                    //Check if tweet should be processed
                    if (processTweet(status)) {
                        //Create ProcessedStatus
                        ProcessedStatus processedStatus = new ProcessedStatus(status.getId(),
                                status.getText(),
                                preprocessDataItem(status.getText()),
                                status.getPlace().getCountryCode(),
                                status.getUser().getScreenName(),
                                status.getCreatedAt());

                        //Send ProcessedStatus to classifier
                        hsClassifier.addToQueue(processedStatus);
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("TwitterPreprocessor: " +
                            "Interrupted Exception {}", e.toString());
                }
            }
        }
    }
}
