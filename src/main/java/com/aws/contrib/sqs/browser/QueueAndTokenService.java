package com.aws.contrib.sqs.browser;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
@Slf4j
class QueueAndTokenService {

    private final AWSSecurityTokenService awsSecurityTokenServiceClient;
    private final AmazonSQS amazonSQSClient;

    QueueAndTokenService(@Value("${awsKey}") String awsKey, @Value("${awsSecretKey}") String awsSecretKey) {
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey, awsSecretKey));
        this.amazonSQSClient = AmazonSQSClientBuilder.standard().withCredentials(credentialsProvider).build();
        this.awsSecurityTokenServiceClient = AWSSecurityTokenServiceClientBuilder.standard().withCredentials(credentialsProvider).build();
    }

    private CreateQueueResult createQueue(String name) {
        return amazonSQSClient.createQueue(name);
    }

    CreateUserAndQueueResult createUserAndQueue() throws JsonProcessingException {
        // 1. create queue
        String queueName = UUID.randomUUID().toString();
        CreateQueueResult createQueueResult = this.createQueue(queueName);
        // 2. create federation token
        String userName = UUID.randomUUID().toString().substring(0,31);
        GetFederationTokenResult getFederationTokenResult =
                createFederationTokenWithPolicy(userName, getQueueArn(createQueueResult.getQueueUrl()));
        // 3. build result
        CreateUserAndQueueResult createUserAndQueueResult = new CreateUserAndQueueResult();
        createUserAndQueueResult.setQueueUrl(createQueueResult.getQueueUrl());
        createUserAndQueueResult.setCredentials(getFederationTokenResult.getCredentials());
        return createUserAndQueueResult;
    }

    private GetFederationTokenResult createFederationTokenWithPolicy(String tokenName, String queueArn) throws JsonProcessingException {
        GetFederationTokenRequest getFederationTokenRequest = new GetFederationTokenRequest();
        getFederationTokenRequest.setDurationSeconds(60 * 60);
        getFederationTokenRequest.setName(tokenName);
        PolicyDocument policyDocument = new PolicyDocument();
        Statement statement = new Statement();
        statement.setResource(queueArn);
        policyDocument.setStatements(Collections.singletonList(statement));
        String serializedPolicy = new ObjectMapper().writeValueAsString(policyDocument);
        getFederationTokenRequest.setPolicy(serializedPolicy);
        return awsSecurityTokenServiceClient.getFederationToken(getFederationTokenRequest);
    }

    private String getQueueArn(String queueUrl) {
        GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest();
        getQueueAttributesRequest.setAttributeNames(Collections.singletonList("QueueArn"));
        getQueueAttributesRequest.setQueueUrl(queueUrl);
        GetQueueAttributesResult result = amazonSQSClient.getQueueAttributes(getQueueAttributesRequest);
        return result.getAttributes().get("QueueArn");
    }

    void publishMessages(String queueUrl) {
        log.info("Publishing sample messages to queue {}", queueUrl);
        amazonSQSClient.sendMessage(queueUrl, "sample messsage 1");
        amazonSQSClient.sendMessage(queueUrl, "sample messsage 2");
        amazonSQSClient.sendMessage(queueUrl, "sample messsage 3");
        amazonSQSClient.sendMessage(queueUrl, "sample messsage 4");
    }

}

