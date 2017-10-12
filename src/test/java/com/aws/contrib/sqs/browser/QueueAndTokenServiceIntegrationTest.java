package com.aws.contrib.sqs.browser;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class QueueAndTokenServiceIntegrationTest {

    @Autowired
    private QueueAndTokenService manager;

    @Test
    public void testCreateQueueAndUserAndSendMessagesAndReceiveAsNewUser() throws JsonProcessingException {
        CreateUserAndQueueResult result = manager.createUserAndQueue();
        System.err.println(result);
        manager.publishMessages(result.getQueueUrl());
        BasicSessionCredentials basicSessionCredentials =
                new BasicSessionCredentials(result.getCredentials().getAccessKeyId(),
                        result.getCredentials().getSecretAccessKey(),
                        result.getCredentials().getSessionToken());
        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard().withCredentials(
                (new AWSStaticCredentialsProvider(basicSessionCredentials))
        ).build();
        ReceiveMessageResult receivedMessage = amazonSQS.receiveMessage(result.getQueueUrl());
        System.err.println(receivedMessage);
    }

}

