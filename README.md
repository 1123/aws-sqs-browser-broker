# aws-sqs-browser-broker
Broker for Temporary SQS Queues And Credentials 

Browsers and backend applications need to exchange information. This is usually done using REST requests (POST/PUT/GET) or using web sockets.

Some environments may restrict the number of API calls to backend applications or the number of concurrent connections. In this scenario it
may help to push information from the backend to dedicated temporary AWS SQS queues, which the browsers subscribe to. 

This project provides a backend application to create these kinds of dedicated queues and credentials upon request by a browser. 

