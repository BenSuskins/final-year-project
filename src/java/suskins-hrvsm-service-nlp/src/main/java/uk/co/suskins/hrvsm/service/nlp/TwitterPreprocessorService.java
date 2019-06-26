package uk.co.suskins.hrvsm.service.nlp;

import twitter4j.Status;

/**
 * Service interface for Preprocessing Twitter
 */
public interface TwitterPreprocessorService {
    /**
     * Offers the status to the TwitterQueue.
     *
     * @param status Status to offer to queue
     */
    void addToQueue(Status status);

    /**
     * Called in the Constructor to start
     * the thread for the TwitterPreprocessor class
     * to constantly poll the Queue for items
     * to pre-process.
     */
    void preprocessQueue();

    /**
     * Applies the preprocessing techniques
     * such as removing emojis, hashtags etc.
     *
     * @param data String to preprocessDataItem
     * @return String preprocessed
     */
    String preprocessDataItem(String data);

    /**
     * Checks if the tweet should be processed
     * Ignores retweets
     * Ignores tweets with no country code.
     *
     * @param status Status to check
     * @return Boolean whether tweet should be processed
     */
    boolean processTweet(Status status);

    /**
     * Removes the @{User} from the String.
     *
     * @param data String to remove @{User} from
     * @return String with @{User} removed
     */
    String removeAts(String data);
}
