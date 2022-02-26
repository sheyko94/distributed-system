package com.toptal.playersservice.clients;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SQSClient {

  @Value("${aws.sqs.accessKeyId}")
  private String accessKeyId;

  @Value("${aws.sqs.secretAccessKey}")
  private String secretAccessKey;

  @Value("${aws.sqs.host}")
  private String host;

  @Value("${aws.sqs.region}")
  private String region;

  public AmazonSQS newClient() {
    return AmazonSQSClientBuilder
      .standard()
      .withEndpointConfiguration(new AmazonSQSClientBuilder.EndpointConfiguration(host, region))
      .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
      .build();
  }

}
