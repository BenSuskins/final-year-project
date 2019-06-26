package uk.co.suskins.hrvsm.service.nlp.impl

import spock.lang.Specification
import twitter4j.Place
import twitter4j.Status
import twitter4j.StatusJSONImpl
import uk.co.suskins.hrvsm.service.nlp.HSClassifier
import uk.co.suskins.hrvsm.service.nlp.impl.PreprocessorServiceImpl
import uk.co.suskins.hrvsm.service.nlp.impl.TwitterPreprocessorServiceImpl

class TwitterPreprocessorServiceImplTest extends Specification {
    def twitterPreprocessorService
    def mockPreprocessorService
    def mockHrvClassifier

    void setup() {
        mockPreprocessorService = new PreprocessorServiceImpl()
        mockHrvClassifier = Mock(HSClassifier)

        twitterPreprocessorService = new TwitterPreprocessorServiceImpl()
        twitterPreprocessorService.preprocessorService = mockPreprocessorService
        twitterPreprocessorService.hsClassifier = mockHrvClassifier
    }

    def "PreprocessDataItem"() {
        given:
            def text = "This is a test TWEET 😂 http://www.suskins.co.uk @TestUser"

        when:
            def result = twitterPreprocessorService.preprocessDataItem(text)

        then:
            result == "this is a test tweet"

    }

    def "AddToQueue"() {
        when:
            twitterPreprocessorService.addToQueue(_ as Status)

        then:
            twitterPreprocessorService.twitterQueue.size() == 1
    }

    def "ProcessTweet"() {
        given:
            def status = Mock(Status)

        when:
            def result = twitterPreprocessorService.processTweet(status)

        then:
            1 * status.isRetweet()
            1 * status.getPlace()
            !result
    }

    def "RemoveAts"() {
        given:
            def text = "@UserXYZ Hello"

        when:
            def result = twitterPreprocessorService.removeAts(text)

        then:
            result == "Hello"
    }

    def "IsFiveOrFewer"() {
        given:
            def trueStatus = new StatusJSONImpl()
            trueStatus.text = "one two three four five"

            def falseStatus = new StatusJSONImpl()
            falseStatus.text = "one two three four five six"
        when:
            def trueResult = twitterPreprocessorService.isFiveOrFewer(trueStatus)
            def falseResult = twitterPreprocessorService.isFiveOrFewer(falseStatus)

        then:
            trueResult
            !falseResult
    }

    def "IsTruncated"() {
        given:
            def trueStatus = new StatusJSONImpl()
            trueStatus.isTruncated = true

            def falseStatus = new StatusJSONImpl()
            falseStatus.isTruncated = false
        when:
            def trueResult = twitterPreprocessorService.isTruncated(trueStatus)
            def falseResult = twitterPreprocessorService.isTruncated(falseStatus)

        then:
            trueResult
            !falseResult
    }

    def "HasCountryData"() {
        given:
            def trueStatus = new StatusJSONImpl()
            trueStatus.place = Mock(Place)

            def falseStatus = new StatusJSONImpl()
            falseStatus.place = null
        when:
            def trueResult = twitterPreprocessorService.hasCountryData(trueStatus)
            def falseResult = twitterPreprocessorService.hasCountryData(falseStatus)

        then:
            trueResult
            !falseResult
    }

    def "IsRetweet"() {
        given:
            def trueStatus = new StatusJSONImpl()
            trueStatus.retweetedStatus = Mock(Status)

            def falseStatus = new StatusJSONImpl()
            falseStatus.retweetedStatus = null
        when:
            def trueResult = twitterPreprocessorService.isRetweet(trueStatus)
            def falseResult = twitterPreprocessorService.isRetweet(falseStatus)

        then:
            trueResult
            !falseResult
    }
}
