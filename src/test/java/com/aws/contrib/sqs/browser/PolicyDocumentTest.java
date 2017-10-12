package com.aws.contrib.sqs.browser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Collections;

public class PolicyDocumentTest {

    @Test
    public void testCreatePolicySerialization() throws JsonProcessingException {
        PolicyDocument policyDocument = new PolicyDocument();
        Statement statement = new Statement();
        statement.setResource("arn:aws:sqs:us-east-2:444455556666:queue2");
        policyDocument.setStatements(Collections.singletonList(statement));
        System.err.println(new ObjectMapper().writeValueAsString(policyDocument));
    }

}
