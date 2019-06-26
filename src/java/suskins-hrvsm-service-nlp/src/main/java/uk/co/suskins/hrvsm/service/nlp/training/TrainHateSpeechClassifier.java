package uk.co.suskins.hrvsm.service.nlp.training;

import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.ling.Datum;
import edu.stanford.nlp.objectbank.ObjectBank;
import edu.stanford.nlp.util.Pair;
import uk.co.suskins.hrvsm.service.nlp.training.config.HateSpeechClassifierConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This Code is based on
 * the Stanford Classifier
 * example code
 * Available:
 * https://nlp.stanford.edu/software/classifier.shtml
 * Version: 3.9.2 2018-10-16
 * <p>
 * This class is only ever run
 * from the IDE to train and
 * serialise a classifier
 * for use in the project.
 * <p>
 * Class Descriptions:
 * 0 - Hate Speech
 * 1 - Offensive Language
 * 3 - Positive Sentiment
 */

class TrainHateSpeechClassifier {
    //Path for dataset
    private static final String FILE_DIR =
            "M:\\bensu\\Documents\\Git\\final-year-project\\src\\java\\suskins-hrvsm-service-nlp\\src\\main\\resources\\hateSpeech\\";

    public static void main(String[] args) throws Exception {
        //ColumnDataClassifier
        ColumnDataClassifier cdc = train();
        evaluate(cdc);
        serialise(cdc);
        //deserialiseEvaluate();
    }

    /*
     Trains the classifier using the provided hateSpeech.train file.
     */
    private static ColumnDataClassifier train() throws IOException {
        HateSpeechClassifierConfig hateSpeechClassifierConfig = new HateSpeechClassifierConfig();
        System.out.println("Training ColumnDataClassifier");
        ColumnDataClassifier cdc = new ColumnDataClassifier(hateSpeechClassifierConfig.getProperties());
        cdc.trainClassifier(FILE_DIR + "hateSpeech.train");
        return cdc;
    }

    /*
      Tests the classifier using the provided hateSpeech.test file.
      Outputs the Accuracy and F1 Score for class and averaged.
      */
    private static void evaluate(ColumnDataClassifier cdc) {
        System.out.println();
        System.out.println("Testing predictions of ColumnDataClassifier");
        for (String line : ObjectBank.getLineIterator(FILE_DIR + "hateSpeech.test", "utf-8")) {
            Datum<String, String> stringDatum = cdc.makeDatumFromLine(line);
            System.out.printf("%s  ==>  %s (%.4f)%n", line, cdc.classOf(stringDatum),
                    cdc.scoresOf(stringDatum).getCount(cdc.classOf(stringDatum)));
        }

        System.out.println();
        System.out.println("Testing accuracy of ColumnDataClassifier");
        Pair<Double, Double> performance = cdc.testClassifier(FILE_DIR + "hateSpeech.test");
        System.out.printf("Accuracy: %.3f; macro-F1: %.3f%n", performance.first(), performance.second());
    }

    /*
      Serealises the classifier to hateSpeechClassifier.dat.
      */
    private static void serialise(ColumnDataClassifier cdc) throws IOException {
        //Serialise Classifier to file
        System.out.println("----Serialising-----");
        FileOutputStream fileOutputStream = new FileOutputStream(new File(FILE_DIR + "hateSpeechClassifier.dat"));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        cdc.serializeClassifier(objectOutputStream);
        objectOutputStream.close();
        System.out.println("----Serialised-----");
    }

    /*
     Tests the serialisd classifer.
     */
    private static void deserialiseEvaluate() throws IOException, ClassNotFoundException {
        //Deserialize classifier from file
        System.out.println("----Deserialising-----");
        FileInputStream fileInputStream = new FileInputStream(new File(FILE_DIR + "hateSpeechClassifier.dat"));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ColumnDataClassifier cdc2 = ColumnDataClassifier.getClassifier(objectInputStream);
        objectInputStream.close();

        //Test Deserialized Version
        System.out.println("----Deserialized CDC Test-----");
        evaluate(cdc2);
    }
}
