package uk.co.suskins.hrvsm.service.nlp.impl;

import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.ling.Datum;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.suskins.hrvsm.model.exception.HSSMNLPServiceException;
import uk.co.suskins.hrvsm.model.models.ProcessedStatus;
import uk.co.suskins.hrvsm.model.models.Tweet;
import uk.co.suskins.hrvsm.repository.TweetsRepository;
import uk.co.suskins.hrvsm.service.nlp.HSClassifier;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of Hate Speech Classifier.
 */
@Service
public class HSClassifierImpl implements HSClassifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(HSClassifierImpl.class);
    private static final String HATE_SPEECH_CLASS = "0";
    private TweetsRepository tweetsRepository;
    private String classifier = "hateSpeech/hateSpeechClassifier.dat";
    private BlockingQueue<ProcessedStatus> classifierQueue;
    private ColumnDataClassifier columnDataClassifier;
    private ClassifierProcessor classifierProcessor;


    public HSClassifierImpl() {
    }

    @Autowired
    public HSClassifierImpl(TweetsRepository tweetsRepository) {
        this.tweetsRepository = tweetsRepository;
        this.classifierQueue = new LinkedBlockingQueue<>(1500);
        this.classifierProcessor = new ClassifierProcessor();

        //If deserialising fails exit the application
        try {
            createClassifier();
        } catch (HSSMNLPServiceException ex) {
            System.exit(-1);
        }

        classify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createClassifier() {
        LOGGER.debug("HRVClassifierImpl: createClassifier deserializing classifier");
        try {
            //Read Classifier from classpath
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream fileInputStream = classloader.getResourceAsStream(classifier);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            //Deserialize classifier
            columnDataClassifier = ColumnDataClassifier.getClassifier(objectInputStream);
            objectInputStream.close();
            LOGGER.debug("HRVClassifierImpl: createClassifier Deserialized classifier successfully");
        } catch (Exception exception) {
            LOGGER.error("HRVClassifierImpl: createClassifier Threw Exception {}", exception.toString());
            throw new HSSMNLPServiceException("Error Creating Classifier"); //Throw for Test
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToQueue(ProcessedStatus processedStatus) {
        classifierQueue.offer(processedStatus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void classify() {
        classifierProcessor.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getProbability(Datum<String, String> tweetDatum) {
        Counter<String> counter = columnDataClassifier.scoresOf(tweetDatum);
        counter = Counters.asNormalizedCounter(counter);
        double probability = counter.getCount(HATE_SPEECH_CLASS);
        probability = Math.abs(probability);
        return probability;
    }

    /**
     * Inner class used for Queue Processing.
     * This class is an infinite loop
     * which will try take an item from the
     * classifier queue, classify it and
     * save to the database if Hate Speech.
     */
    class ClassifierProcessor extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    //Create Datum from tweet for classification
                    ProcessedStatus processedStatus = classifierQueue.take();
                    Datum<String, String> tweetDatum = columnDataClassifier
                            .makeDatumFromLine("\t" + Objects.requireNonNull(processedStatus).getProcessedText());

                    //If class of tweet is Hate Speech save to database
                    if (columnDataClassifier.classOf(tweetDatum).equals(HATE_SPEECH_CLASS)) {
                        tweetsRepository.save(new Tweet(processedStatus, getProbability(tweetDatum)));
                        LOGGER.debug("HRVClassifierImpl: " +
                                "classify Saved Tweet ID {} to database", processedStatus.getId());
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("ClassifierProcessor: " +
                            "Interrupted Exception {}", e.toString());
                }
            }
        }
    }
}