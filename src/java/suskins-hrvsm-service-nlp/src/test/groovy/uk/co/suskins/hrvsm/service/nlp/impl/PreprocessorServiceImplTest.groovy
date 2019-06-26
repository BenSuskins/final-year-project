package uk.co.suskins.hrvsm.service.nlp.impl

import spock.lang.Specification
import uk.co.suskins.hrvsm.service.nlp.impl.PreprocessorServiceImpl

class PreprocessorServiceImplTest extends Specification {
    def preprocessorService

    void setup() {
        preprocessorService = new PreprocessorServiceImpl()
    }

    def "RemoveEmojis"() {
        given:
            def text = "Test😂 Sentence with emojis 😂😂😂😂"

        when:
            def result = preprocessorService.removeEmojis(text)

        then:
            result == "Test Sentence with emojis "
    }

    def "RemoveHashtags"() {
        given:
            def text = "Test Sentence with hashtags #amazing#lol#twitter"

        when:
            def result = preprocessorService.removeHashtags(text)

        then:
            result == "Test Sentence with hashtags  amazing lol twitter"
    }

    def "RemoveUrls"() {
        given:
            def text = "Test Sentence with url http://suskins.co.uk"

        when:
            def result = preprocessorService.removeUrls(text)

        then:
            result == "Test Sentence with url"
    }

    def "Lower text"() {
        given:
            def text = "Test SEnTenCe WITH different CASES"

        when:
            def result = preprocessorService.lower(text)

        then:
            result == "test sentence with different cases"
    }

    def "RemovePunctuation"() {
        given:
            def text = "Test...?>< Sentence with punctuation!.!?@#[]"

        when:
            def result = preprocessorService.removePunctuation(text)

        then:
            result == "Test Sentence with punctuation"
    }
}
