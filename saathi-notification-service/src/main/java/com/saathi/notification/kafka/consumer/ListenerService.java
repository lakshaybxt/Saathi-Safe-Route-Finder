package com.saathi.notification.kafka.consumer;

import com.saathi.notification.kafka.events.EmailEvent;
import com.saathi.notification.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import static com.saathi.notification.kafka.topics.KafkaTopics.NOTIFICATION_EMAIL_SEND;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListenerService {

  private final EmailService emailService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = NOTIFICATION_EMAIL_SEND, groupId = "notification-service-group")
  public void handleEmail(String message, Acknowledgment ack) throws MessagingException {

    EmailEvent event = null;
    try {
      event = objectMapper.readValue(message, EmailEvent.class);

      log.info("Received email event userId={} email={}", event.getUserId(), event.getTo());

      emailService.sendEmail(
          event.getTo(),
          event.getSubject(),
          event.getBody()
      );

      ack.acknowledge();

    } catch (Exception e) {

      log.error("Email sending failed userId={} error={}", event.getUserId(), e.getMessage(), e);
      throw e;
    }
  }
}