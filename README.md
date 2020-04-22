# Kinesis Streaming ETL
Demonstrates how to use Kinesis Data Streams and Kinesis Data Analytics applications to  perform streaming joins across related data streams and reference data lookup to create enriched data streams and persist it to DynamoDB

# Architecture

![alt text] (https://rvvittal-aws-immersion-day.s3.amazonaws.com/GitResources/Kinesis-Streaming-ETL.png)


# How to build this architecture


## Pre-Reqs

1. Setup JDK 8+ and Maven 3.6+ on your development workstation


## Installation

1. Create Kinesis Data Stream - OrdersStream - with 2 shards
1. Create Kinesis Data Analytics Application - KDA-OrderProcess with runtime as SQL
  1. Connect to Streaming Data Source - OrdersStream



 







