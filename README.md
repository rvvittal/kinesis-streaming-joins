# Kinesis Streaming ETL
Demonstrates how to use Kinesis Data Streams and Kinesis Data Analytics applications to  perform streaming joins across related data streams and reference data lookup to create enriched data streams and persist it to DynamoDB

# Architecture

![alt text] (./resources/Kinesis-Streaming-ETL.png)


# How to build this architecture


## Pre-Reqs

1. Setup JDK 8+ and Maven 3.6+ on your development workstation


## Installation

1. Create Data Sets for ingesting events and reference data
     1. Clone/Download this repo to your workstation
     2. From command line navgiate to project root folder i.e. kinesis-streaming-joins
     3. Build the jar file using mvn clean package
     4. Create S3 bucket e.g. <userid>-kinesis-orders and create folders orders, products
     5. C
2. Create Kinesis Data Stream - OrdersStream - with 2 shards
3. 
4. Create Kinesis Data Analytics Application - KDA-OrderProcess - with runtime as SQL
     1. Connect to Streaming Data Source - OrdersStream
     2. Enter 



 







