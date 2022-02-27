package com.toptal.playersservice.consumers;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.playersservice.clients.SQSClient;
import com.toptal.playersservice.consumers.dtos.PlayerBoughtSQSConsumerDTO;
import com.toptal.playersservice.handlers.PlayerBoughtEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerBoughtSQSConsumer {

  @Value("${aws.sqs.queues.player-bought-queue-url}")
  private String playerBoughtQueueURL;

  private final SQSClient sqsClient;
  private final ObjectMapper objectMapper;
  private final PlayerBoughtEventHandler playerBoughtEventHandler;

  @Async
  @Transactional
  @Scheduled(fixedRate = 10000) // every 10 seconds
  public void startConsuming() throws IOException {

    final AmazonSQS amazonSQS = sqsClient.newClient();

    final List<Message> messages = amazonSQS.receiveMessage(playerBoughtQueueURL).getMessages();

    for (Message message : messages) {

      final String messageId = message.getMessageId();

      log.info("Consuming message with ID {} from queue {}", messageId, playerBoughtQueueURL);

      final PlayerBoughtSQSConsumerDTO playerBoughtSQSConsumerDTO = objectMapper.readValue(message.getBody(), PlayerBoughtSQSConsumerDTO.class);

      playerBoughtEventHandler.handle(messageId, playerBoughtSQSConsumerDTO);

      amazonSQS.deleteMessage(playerBoughtQueueURL, message.getReceiptHandle());

      log.info("Deleted message with ID {}", messageId);
    }
  }

}
