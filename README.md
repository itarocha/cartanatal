# cartanatal

docker build . -t cartanatal-docker

docker run --name cartanatal -p 8080:8080 cartanatal-docker 
    