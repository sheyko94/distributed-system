package com.toptal.playersservice.consumers;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.playersservice.clients.SQSClient;
import com.toptal.playersservice.consumers.dtos.UserSignedUpSQSConsumerDTO;
import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.handlers.CreatedTeamEventHandler;
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
public class UserSignedUpSQSConsumer {

  @Value("${aws.sqs.queues.user-signed-up-queue-url}")
  private String userSignedUpQueueURL;

  private final SQSClient sqsClient;
  private final ObjectMapper objectMapper;
  private final CreatedTeamEventHandler createdTeamEventHandler;

  @Async
  @Transactional
  @Scheduled(fixedRate = 10000) // every 10 seconds
  public void startConsuming() throws IOException {

    final AmazonSQS amazonSQS = sqsClient.newClient();

    final List<Message> messages = amazonSQS.receiveMessage(userSignedUpQueueURL).getMessages();

    for (Message message : messages) {

      final String messageId = message.getMessageId();

      log.info("Consuming message with ID {} from queue {}", messageId, userSignedUpQueueURL);

      final UserSignedUpSQSConsumerDTO userSignedUpSQSConsumerDTO = objectMapper.readValue(message.getBody(), UserSignedUpSQSConsumerDTO.class);
      final String userId = userSignedUpSQSConsumerDTO.getId();

      TeamEvent createdTeamEvent = createdTeamEventHandler.generate(userId);

      log.info("Successfully created Team with ID {} for user with ID {}", createdTeamEvent.getId(), userId);

      amazonSQS.deleteMessage(userSignedUpQueueURL, message.getReceiptHandle());

      log.info("Deleted message with ID {}", messageId);
    }
  }

}
