# Extracting Knowledge About the Usage of Hate Speech from Social Media - Grafana
This directory contains the files related to the provisioning of Grafana for the project.

#### Starting Grafana Front End
Running the following commands will build the Docker Image and then run it.

```bash
docker build -t hatespeech-grafana .
docker run -p 3000 hatespeech-grafana 
```

[Local Grafana](http://localhost:3000)
