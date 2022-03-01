package com.toptal.authservice.clients;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SQSClientTest {

  private final static String ACCESS_KEY_ID = "123";
  private final static String SECRET_ACCESS_KEY = "234";
  private final static String HOST = "http://localhost:4566";
  private final static String REGION = "eu-west-1";

  @InjectMocks
  private SQSClient sqsClient;

  @Test
  public void newClientTest() {

    ReflectionTestUtils.setField(sqsClient, "accessKeyId", ACCESS_KEY_ID);
    ReflectionTestUtils.setField(sqsClient, "secretAccessKey", SECRET_ACCESS_KEY);
    ReflectionTestUtils.setField(sqsClient, "host", HOST);
    ReflectionTestUtils.setField(sqsClient, "region", REGION);

    final AmazonSQS result = sqsClient.newClient();

    assertEquals(Objects.requireNonNull(ReflectionTestUtils.getField(result, AmazonSQSClient.class, "endpoint")).toString(), HOST);
  }

}
