# openai-embedding
This project helps you to create posts on a certain topic and find the least related post on that topic using OpenAI's embedding model.

# Technologies
* [Liquibase](https://docs.liquibase.com/home.html)
* [OpenAPI](https://springdoc.org/)
* [Spring WebFlux](https://spring.io/guides/gs/reactive-rest-service/)
* [pgvector](https://github.com/pgvector/pgvector)

# Setup
Clone [pgvector repository](https://github.com/pgvector/pgvector) and create image using particular Dockerfile. Create docker container for pgvector. After creating container, setup db. Replace the environment variables in application.yml with your own.

# API Documentation

## Create Topic 

### Request 
POST /v1/api/topic

### Request Body
{
  "topicName": "fruits",
  "posts": ["apple", "banana", "grape", "watermelon", "hamburger"]
}

### Response
{
  "topicId": "ea187b1ef0c34aa9a485cb0c1f02dabe"
}

## Detect anomaly in Topic

### Request 
GET /v1/api/topic/{id}/detect-anomaly

### Parameter

id: ea187b1ef0c34aa9a485cb0c1f02dabe

### Response
{
  "post": "hamburger"
}

