package com.aws.contrib.sqs.browser;

import com.amazonaws.services.securitytoken.model.Credentials;
import lombok.Data;

@Data
class CreateUserAndQueueResult {
    private String queueUrl;
    private Credentials credentials;
}
