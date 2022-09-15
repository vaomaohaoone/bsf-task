# BSF task 
### Build:  
./gradlew clean build  
### Run: 
cd dockerize  
docker-compose up -d  
### Run postman collection (newman should be installed)  
cd ../postman  
newman run BSF_task.postman_collection.json  
