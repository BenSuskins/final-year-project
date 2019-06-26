package uk.co.suskins.hrvsm.service;

/**
 * Service interface for the Twitter streaming api
 */
public interface TwitterService {
    /**
     * Checks if Application twitterStatus to the Twitter
     * Streaming Endpoint.
     *
     * @return Boolean Twitter Connection Status
     */
    boolean getTwitterConnectionStatus();

    /**
     * Initiates connection to Twitter Streaming
     * endpoint using HBC.
     */
    void twitterStartStream();

    /**
     * Closes connection to Twitter Streaming
     * endpoint using HBC.
     */
    void twitterStopStream();

    /**
     * Configures the Twitter4j Client
     * with the relevant variables.
     */
    void configureTwitterClient();
}
