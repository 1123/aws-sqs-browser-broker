package com.aws.contrib.sqs.browser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
class PolicyDocument {

    @JsonProperty("Version")
    private String version = "2012-10-17";
    @JsonProperty("Id")
    private String id = "ReadFromQueue";

    @JsonProperty("Statement")
    private List<Statement> statements;

}
