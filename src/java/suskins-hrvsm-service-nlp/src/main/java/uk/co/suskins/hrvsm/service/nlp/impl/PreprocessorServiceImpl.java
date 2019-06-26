package uk.co.suskins.hrvsm.service.nlp.impl;

import org.springframework.stereotype.Service;
import uk.co.suskins.hrvsm.service.nlp.PreprocessorService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service implementation for Preprocessor
 */
@Service
public class PreprocessorServiceImpl implements PreprocessorService {
    private static final String EMOJI_PATTERN =
            "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
    private static final String PUNCTUATION_PATTERN =
            "[^a-zA-Z0-9 \\t]";
    private static final String URL_PATTERN =
            "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?+-=\\\\.&]*)";

    public PreprocessorServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeEmojis(String data) {
        Pattern pattern = Pattern.compile(
                EMOJI_PATTERN,
                Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(data);
        return matcher.replaceAll("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeHashtags(String data) {
        return data.replace("#", " ");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeUrls(String data) {
        Pattern pattern = Pattern.compile(
                URL_PATTERN,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(data);
        int count = 0;
        while (matcher.find()) {
            data = data.replaceAll(matcher.group(count), "").trim();
            count++;
        }
        return data;
    }

    @Override
    public String lower(String data) {
        return data.toLowerCase();
    }

    @Override
    public String removePunctuation(String data) {
        Pattern pattern = Pattern.compile(
                PUNCTUATION_PATTERN);
        Matcher matcher = pattern.matcher(data);
        return matcher.replaceAll("");
    }
}
