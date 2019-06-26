# Extracting Knowledge About the Usage of Hate Speech from Social Media- Java
This directory contains the Java source code for the project.

### Training the Classifier
The serialised classifier is not provided in this repo due to its size (~1GB).

To train and serialise the classifier run the following Java file:

```bash
src\java\suskins-hrvsm-service-nlp\src\main\java\uk\co\suskins\hrvsm\service\nlp\training\TrainHateSpeechClassifier.java
```

This process will likely take >12 hours due to the size of the training dataset.

The serialised classifer will be saved to:

```bash
src\java\suskins-hrvsm-service-nlp\src\main\resources\hateSpeech\hateSpeechClassifier.dat
```

### Starting Java Service
Running the following commands will build the project Jar and then run it.

```bash
mvn clean install
java -jar suskins-hrvsm-api-app/target/suskins-hrvsm-api-app-develop-SNAPSHOT.jar
```

OR

```bash
docker build -t hatespeech-java .
docker run -p 80 hatespeech-java 
```


[Local API Documentation](http://localhost:80/swagger-ui.html#/)

### Running Tests
Running the following command in the project root will run all of the Groovy Spock tests.

```bash
mvn test
```