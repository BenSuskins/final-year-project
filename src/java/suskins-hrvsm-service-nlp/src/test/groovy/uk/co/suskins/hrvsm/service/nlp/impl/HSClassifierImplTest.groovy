package uk.co.suskins.hrvsm.service.nlp.impl

import edu.stanford.nlp.classify.ColumnDataClassifier
import edu.stanford.nlp.ling.BasicDatum
import edu.stanford.nlp.ling.Datum
import edu.stanford.nlp.stats.ClassicCounter
import spock.lang.Specification
import uk.co.suskins.hrvsm.model.exception.HSSMNLPServiceException
import uk.co.suskins.hrvsm.model.models.ProcessedStatus

import java.util.concurrent.LinkedBlockingQueue

class HSClassifierImplTest extends Specification {
    def hsClassifier
    def mockClassifierProcessor

    void setup() {
        mockClassifierProcessor = Mock(HSClassifierImpl.ClassifierProcessor)
        hsClassifier = new HSClassifierImpl()
        hsClassifier.classifierQueue = new LinkedBlockingQueue<>(1500)
    }

    def "CreateClassifier, exception thrown with invalid file"() {
        given:
            hsClassifier.classifier = "invalid file path"
        when:
            hsClassifier.createClassifier()
        then:
            thrown HSSMNLPServiceException
    }

    def "AddToQueue, succeeds"() {
        when:
            hsClassifier.addToQueue(new ProcessedStatus())
        then:
            hsClassifier.classifierQueue.size() == 1
    }

    def "GetProbability, with stubbed classifier return 0"() {
        given:
            def mockClassifier = Mock(ColumnDataClassifier)
            hsClassifier.columnDataClassifier = mockClassifier
            Datum<String, String> datum = new BasicDatum<>()
            mockClassifier.scoresOf(datum) >> new ClassicCounter<String>()
        when:
            def result = hsClassifier.getProbability(datum)
        then:
            result == 0
    }
}
