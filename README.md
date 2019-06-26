# Extracting Knowledge About Hate Speech from Social Media
This project uses Social Media sites such as Twitter to extract knowledge about the usage of Hate Speech online.

The system is developed in Java using the Spring framework, running on Amazon Web Services and backed by a PostgreSQL database feeding data into Grafana for monitoring and visualisation.  

The full system is available online at [Here](www.hatespeech.suskins.co.uk).

## Getting Started 

### Prerequisites
Software:
* Java 8
* Maven
* Docker
* Docker Compose

### Running Application
The application runs in Docker.

The provided Docker Compose file will setup the PostgreSQL database, the Java Back End and the Grafana Front End.


```
cd src/java/
mvn package
cd ..
cd ..
docker-compose up
```

### Versioning Strategy
Using Semantic Versioning:

Given a version number MAJOR.MINOR.PATCH, increment the:

    MAJOR version when you make incompatible API changes,
    MINOR version when you add functionality in a backwards-compatible manner, and
    PATCH version when you make backwards-compatible bug fixes.

My Minimum Viable Product is tagged as 1.0.0

### Version History
1.0.0 Initial Release | Twitter Integration | Grafana Visualisation | Hate Speech Trained Classifier. - 08/12/2018.

2.0.0 Reworked API | Control Interface | Retrained Hate Speech Model. - 23/01/2019.

2.1.0 Refactor to Hate Speech from Human Rights | Confidence Measure | Improved Control Interface | Improved Classifier. - 10/4/2019

## Authors
* Ben Suskins 

## References
* [Twitter Hosebird Client](https://github.com/twitter/hbc)
* [Spring Framework](https://spring.io/)
* [Apache Maven](https://maven.apache.org/)
* [Stanford CoreNLP](https://stanfordnlp.github.io/CoreNLP/)
* [Grafana](https://grafana.com/)
* [Hate Speech Training Data](https://github.com/t-davidson/hate-speech-and-offensive-language)
* [Sentiment 140 Training Data](http://help.sentiment140.com/for-students/)