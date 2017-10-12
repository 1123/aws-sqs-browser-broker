package com.aws.contrib.sqs.browser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot application that generates temporary queues and credentials for browsers to receive information from
 * a backend service.
 *
 * This way the backend service can publish messages to the queue instead of the browser directly polling the backend.
 *
 * For each browser instance a separate pair of (queue, credentials) is generated. The contents of the queue can only
 * be read by the particular browser instance that the queue was generated for.
 */
@SpringBootApplication
public class Application {

    public static void main(String ... args) {
        SpringApplication.run(Application.class, args);
    }
}
