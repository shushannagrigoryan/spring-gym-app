package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateTrainerWorkloadSenderService {
    private static final String UPDATE_TRAINER_WORKLOAD_QUEUE = "update-trainer-workload-queue";

    private final JmsTemplate jmsTemplate;

    /** send method for updating trainer workload. */
    @Transactional
    public void send(UpdateTrainerWorkloadRequestDto updateTrainerWorkloadRequestDto) {
        log.debug("Sending UpdateTrainerWorkloadRequestDto to ActiveMQ");
        log.debug("jmsTemplate converter = {}", jmsTemplate.getMessageConverter());
        System.out.println(updateTrainerWorkloadRequestDto);
        jmsTemplate.convertAndSend(UPDATE_TRAINER_WORKLOAD_QUEUE, updateTrainerWorkloadRequestDto);
        log.debug("Successfully sent UpdateTrainerWorkloadRequestDto to ActiveMQ.");
    }
}
