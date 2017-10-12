package com.aws.contrib.sqs.browser;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
@Slf4j
public class QueueAndTokenController {

    @Autowired
    private QueueAndTokenService queueAndTokenService;

    @RequestMapping(value = "/queue", method = RequestMethod.GET)
    public CreateUserAndQueueResult getQueueAndToken() throws JsonProcessingException {
        return queueAndTokenService.createUserAndQueue();
    }

    @RequestMapping(value = "messages/publish", method = RequestMethod.GET)
    public boolean publishMessages(String queueUrl) {
        log.info("Request received for publishing sample messages to queue");
        queueAndTokenService.publishMessages(queueUrl.substring(1, queueUrl.length() - 1));
        return true;
    }

}
