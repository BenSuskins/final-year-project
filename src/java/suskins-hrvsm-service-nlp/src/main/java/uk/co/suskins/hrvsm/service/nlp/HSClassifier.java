package uk.co.suskins.hrvsm.service.nlp;

import edu.stanford.nlp.ling.Datum;
import uk.co.suskins.hrvsm.model.models.ProcessedStatus;

/**
 * Hate Speech Classifier interface.
 */
public interface HSClassifier {

    /**
     * Adds the processed tweet to the
     * TwitterQueue for classification later.
     *
     * @param processedStatus ProcessedStatus to classify later
     */
    void addToQueue(ProcessedStatus processedStatus);

    /**
     * Called in the Constructor to start
     * the thread for the ClassifierProcessor class
     * to constantly poll the Queue for items
     * to classify.
     * Pre-processed Tweets classed as Hate Speech
     * have their probability calculated and
     * are stored in the Database.
     */
    void classify();

    /**
     * Using the previously serialised classifier
     * deserialize it and store this classifier
     * for use.
     */
    void createClassifier();

    /**
     * Returns the probability of the given
     * tweetDatum being of class 0.
     *
     * @return Double probability
     */
    double getProbability(Datum<String, String> tweetDatum);
}
