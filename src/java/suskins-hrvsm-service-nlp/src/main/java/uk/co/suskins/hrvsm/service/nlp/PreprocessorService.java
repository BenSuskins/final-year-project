package uk.co.suskins.hrvsm.service.nlp;

/**
 * Service interface for Preprocessing
 */
public interface PreprocessorService {
    /**
     * Removes Emojis from the provided string.
     *
     * @param data String to remove emojis from
     * @return String with emojis removed
     */
    String removeEmojis(String data);

    /**
     * Removes Hashtags from the provided string.
     *
     * @param data String to remove Hashtags from
     * @return String with Hashtags removed
     */
    String removeHashtags(String data);

    /**
     * Removes Urls from the provided string.
     *
     * @param data String to remove Urls from
     * @return String with Urls removed
     */
    String removeUrls(String data);

    /**
     * Converts the string to lower case.
     *
     * @param data String to lower case
     * @return String in lower case
     */
    String lower(String data);

    /**
     * Converts the string to lower case.
     *
     * @param data String to remove punctuation
     * @return String with no punctuation
     */
    String removePunctuation(String data);
}
