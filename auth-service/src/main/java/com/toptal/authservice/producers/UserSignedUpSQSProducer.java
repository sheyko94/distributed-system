package com.toptal.authservice.producers;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.authservice.clients.SQSClient;
import com.toptal.authservice.producers.dtos.UserSignedUpSQSProducerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserSignedUpSQSProducer {

  @Value("${aws.sqs.queues.user-signed-up-queue-url}")
  private String userSignedUpQueueURL;

  private final SQSClient sqsClient;
  private final ObjectMapper objectMapper;

  public void send(final UserSignedUpSQSProducerDTO userSignedUpSQSProducerDTO) throws JsonProcessingException {

    final AmazonSQS amazonSQS = sqsClient.newClient();

    final SendMessageRequest sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setMessageBody(objectMapper.writeValueAsString(userSignedUpSQSProducerDTO));
    sendMessageRequest.setQueueUrl(userSignedUpQueueURL);

    log.info("Sending message to queue {} for user ID {}", userSignedUpQueueURL, userSignedUpSQSProducerDTO.getId());

    final SendMessageResult sendMessageResult = amazonSQS.sendMessage(sendMessageRequest);

    log.info("Successfully sent SQS message with ID {} to queue {}", sendMessageResult.getMessageId(), userSignedUpQueueURL);

  }

}
