# HackerNewsService API

## This project has been deployed on a docker container.
### Steps to Run
```sh
docker pull shob4516/hackernews-service:latest
docker run -p 8080:8080 shob4516/hackernews-service   
```
### Endpoints

| HttpVerb | URL |
| ------ | ------ |
| Get | http://localhost:8080/api/hacker-news/top-stories |
| Get | http://localhost:8080/api/hacker-news/past-stories |
| Get | http://localhost:8080/api/hacker-news/comments/{storyId} |

### Endpoints details -
- http://localhost:8080/api/hacker-news/top-stories - To get the top ten stories based on their score in last 500 mins. (Most of the times external Api was not returning any result for past 15 mins)
- http://localhost:8080/api/hacker-news/past-stories - To get all past stories from an in memory h2 DB.
- http://localhost:8080/api/hacker-news/comments/{storyId} - To get atmost 10 comments for this particular story id, based on the number of child comments.
- http://localhost:8080/h2-console - To access the in memory H2 console.
  - Driver class - org.h2.Driver
  - JDBC URL - jdbc:h2:mem:testdb
  - User name - sa
  - password -

### Please note that this project uses a redis server (integrated in docker container) to leverage caching.

### Steps to run on local
- Download the redis server on your local machine and make sure it is running on port 6379.
- Change the "spring.redis.host" property in application.properties and set it to localhost.
```sh
spring.redis.host=host.docker.internal
```

