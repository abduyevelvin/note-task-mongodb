# v1: note-task
Maven is used as a build automation tool. 
There are 2 ways to run the application:
    Using local MongoDD and run application locally, 
    then below commands could be used to clean/install and 
    run the application using the command line respectively:
 - "Path\mongod.exe" --dbpath="db-path" (need provide mongo db exe file location and db path location)
 - mvn clean install
 - mvn spring-boot:run

Or using docker-compose command (before need uncomment following line in application.properties file "#spring.data.mongodb.host=note_db"):
 - docker pull mongo:latest
 - docker build -t note-task-mongodb:1.0 . (can be named as wish, but based on it docker-compose.yml file should be updated)
 - docker-compose up

MongoDB is used for storing data, verify data from MongoDB, "mongosh" (Mongo Shell) could be used:
- mongosh command in cmd.exe
- show dbs
- use note-task-db
- show collections
- db.Note.find().pretty() / db.User.find().pretty() (we can see collections in DB)
 
Implemented Swagger UI, could be reached from the below link:
http://localhost:8080/swagger-ui/#/

For authorization: JWT token is used (token based configs stored in property file)
For mapping models to DTOs mapstruct is used.
SLF4J Logger is used for logging purposes.
Mock is used for testing purposes.

All configurations could be found in the resources: application.properties
