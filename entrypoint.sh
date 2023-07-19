#!/bin/bash

# Start the Redis server in the background
redis-server &

# Wait for Redis to initialize
sleep 5

# Execute the Java application
java -jar hackerNewsService-0.0.1-SNAPSHOT.jar
