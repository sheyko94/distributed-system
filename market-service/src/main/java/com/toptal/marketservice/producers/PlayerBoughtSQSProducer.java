package com.toptal.marketservice.producers;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.marketservice.clients.SQSClient;
import com.toptal.marketservice.producers.dtos.PlayerBoughtSQSProducerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerBoughtSQSProducer {

  @Value("${aws.sqs.queues.player-bought-queue-url}")
  private String playerBoughtQueueURL;

  private final SQSClient sqsClient;
  private final ObjectMapper objectMapper;

  public void send(final PlayerBoughtSQSProducerDTO playerBoughtSQSProducerDTO) throws JsonProcessingException {

    final AmazonSQS amazonSQS = sqsClient.newClient();

    final SendMessageRequest sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setMessageBody(objectMapper.writeValueAsString(playerBoughtSQSProducerDTO));
    sendMessageRequest.setQueueUrl(playerBoughtQueueURL);

    log.info("Sending message to queue {} for player ID {}", playerBoughtQueueURL, playerBoughtSQSProducerDTO.getPlayerId());

    final SendMessageResult sendMessageResult = amazonSQS.sendMessage(sendMessageRequest);

    log.info("Successfully sent SQS message with ID {} to queue {}", sendMessageResult.getMessageId(), playerBoughtQueueURL);

  }

}
