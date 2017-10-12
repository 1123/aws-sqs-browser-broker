package com.aws.contrib.sqs.browser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
class Statement {

    @JsonProperty("Sid")
    private String sid = "1";

    @JsonProperty("Effect")
    private String effect = "Allow";

    @JsonProperty("Action")
    private List<String> action = Collections.singletonList("sqs:ReceiveMessage");

    @JsonProperty("Resource")
    private String resource;

}
