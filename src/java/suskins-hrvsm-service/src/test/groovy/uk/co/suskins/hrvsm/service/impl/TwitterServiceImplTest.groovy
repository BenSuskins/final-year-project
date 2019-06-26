package uk.co.suskins.hrvsm.service.impl

import com.twitter.hbc.twitter4j.Twitter4jStatusClient
import spock.lang.Specification
import twitter4j.Status
import twitter4j.StatusDeletionNotice
import uk.co.suskins.hrvsm.model.exception.HSSMServiceException
import uk.co.suskins.hrvsm.model.models.Tweet
import uk.co.suskins.hrvsm.repository.TweetsRepository
import uk.co.suskins.hrvsm.service.Config.TwitterConfig
import uk.co.suskins.hrvsm.service.nlp.TwitterPreprocessorService

class TwitterServiceImplTest extends Specification {
    def twitterService
    def twitterConfig
    def mockTwitterPreprocessorService
    def mockTweetsRepository

    void setup() {
        mockTweetsRepository = Mock(TweetsRepository)
        twitterConfig = new TwitterConfig()
        twitterConfig.setTwitterAccessSecret("accessSecret")
        twitterConfig.setTwitterAccessToken("accessToken")
        twitterConfig.setTwitterConsumerKey("consumerKey")
        twitterConfig.setTwitterConsumerSecret("consumerSecret")
        mockTwitterPreprocessorService = Mock(TwitterPreprocessorService)
        twitterService = new TwitterServiceImpl(twitterConfig,
                mockTwitterPreprocessorService,
                mockTweetsRepository)
    }

    def "StatusListener onStatus"() {
        given:
            def status = Mock(Status)

        when:
            twitterService.statusListener.onStatus(status)

        then:
            1 * mockTwitterPreprocessorService.addToQueue(status)
    }

    def "StatusListener onDeletionNotice no deletion"() {
        given:
            def mockStatusDeletionNotice = Mock(StatusDeletionNotice)

        when:
            twitterService.statusListener.onDeletionNotice(mockStatusDeletionNotice)

        then:
            1 * mockStatusDeletionNotice.getStatusId() == 0
            1 * mockTweetsRepository.findByTweetId(0)
    }

    def "StatusListener onDeletionNotice deletion"() {
        given:
            def mockStatusDeletionNotice = Mock(StatusDeletionNotice)
            def mockTweet = Mock(Tweet)

        when:
            twitterService.statusListener.onDeletionNotice(mockStatusDeletionNotice)

        then:
            1 * mockStatusDeletionNotice.getStatusId() == 0
            1 * mockTweetsRepository.findByTweetId(0) >> mockTweet
            1 * mockTweetsRepository.delete(mockTweet)
    }

    def "StatusListener onScrubGeo no deletion"() {
        given:
            def statusId = 1
            def userId = 2
            def mockTweet = Mock(Tweet)


        when:
            twitterService.statusListener.onScrubGeo(userId, statusId)

        then:
            1 * mockTweetsRepository.findByTweetId(statusId) >> mockTweet
            1 * mockTweetsRepository.delete(mockTweet)
    }

    def "StatusListener onScrubGeo deletion"() {
        given:
            def statusId = 1
            def userId = 2

        when:
            twitterService.statusListener.onScrubGeo(userId, statusId)

        then:
            1 * mockTweetsRepository.findByTweetId(statusId)
    }


    def "twitterStopStream, exception thrown"() {
        given:
            twitterService.setTwitterConnectionStatus(false)

        when:
            twitterService.twitterStopStream()

        then:
            thrown HSSMServiceException
    }

    def "TwitterStopStream, mocked Twitter4jStatusClient"() {
        given:
            twitterService.twitter4jStatusClient = Mock(Twitter4jStatusClient)
            twitterService.setTwitterConnectionStatus(true)
        when:
            twitterService.twitterStopStream()
        then:
            1 * twitterService.twitter4jStatusClient.stop()
            !twitterService.getTwitterConnectionStatus()
    }

    def "GetTwitterConnectionStatus, returns false on start"() {
        when:
            def result = twitterService.getTwitterConnectionStatus()
        then:
            !result
    }

    def "TwitterStartStream, mocked Twitter4jStatusClient, all methods called correct number times"() {
        given:
            twitterService.twitter4jStatusClient = Mock(Twitter4jStatusClient)
        when:
            twitterService.twitterStartStream()
        then:
            twitterService.getTwitterConnectionStatus() == true
            1 * twitterService.twitter4jStatusClient.connect()
            32 * twitterService.twitter4jStatusClient.process()
    }

    def "TwitterStartStream, exception thrown"() {
        given:
            twitterService.setTwitterConnectionStatus(true)
        when:
            twitterService.twitterStartStream()
        then:
            thrown HSSMServiceException
    }

    def "ConfigureTwitterClient, client starts null, ends not null"() {
        given:
            twitterService.twitter4jStatusClient == null
        when:
            twitterService.configureTwitterClient()
        then:
            twitterService.twitter4jStatusClient != null
    }
}
