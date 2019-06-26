# Extracting Knowledge About Hate Speech from Social Media - Grafana
This directory contains the files related to the provisioning of Grafana for the project.

#### Starting Grafana Front End
Running the following commands will build the Docker Image and then run it.

```
docker build -t hssmgrafana .
docker run hssmgrafana -p 3000
```

[Local Grafana](http://localhost:3000)
