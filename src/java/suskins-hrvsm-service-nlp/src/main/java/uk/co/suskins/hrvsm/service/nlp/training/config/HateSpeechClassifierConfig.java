package uk.co.suskins.hrvsm.service.nlp.training.config;

import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
public class HateSpeechClassifierConfig {
    private final Properties properties;

    public HateSpeechClassifierConfig() {
        this.properties = new Properties();

        //Final Classifier Configuration
        //Word N-grams
        properties.put("1.splitWordsRegexp", "\\s+");
        properties.put("1.useSplitWordNGrams", "true");
        properties.put("1.useSplitWords", "true");
        properties.put("1.minWordNGramLeng", "1");
        properties.put("1.maxWordNGramLeng", "4");

        //Character N-grams
        properties.put("1.useNGrams", "true");
        properties.put("1.maxNGramLeng", "4");
        properties.put("1.minNGramLeng", "2");

        //Other
        properties.put("useClassFeature", "true");
        properties.put("printClassifierParam", "200");
        properties.put("intern", "true");
        properties.put("sigma", "0.75 ");
        properties.put("crossValidationFolds", "8");
        properties.put("shuffleTrainingData", "true");
        properties.put("lowercase", "true");
    }

    public Properties getProperties() {
        return properties;
    }
}
