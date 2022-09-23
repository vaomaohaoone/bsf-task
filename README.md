# BSF task 
### Build:  
./gradlew clean build 
### Build image manually (there is way to have gradle image too):
mv ./bsf-task-service/libs/bsf-task-service-final.jar ./dockerize/image
cd dockerize/image
docker build -t vaomaohao/bsf-task:latest --build-arg JAR_FILE=bsf-task-service-final.jar .
### Run: 
cd dockerize  
docker-compose up -d  
### Run postman collection (newman should be installed)  
cd ../postman  
newman run BSF_task.postman_collection.json  
