package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadSenderService {
    private static final String TRAINER_WORKLOAD_REQUEST_QUEUE = "trainer-workload-request-queue";

    private final JmsTemplate jmsTemplate;

    /**
     * send method for getting trainer workload.
     */
    public void send(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        log.debug("Sending TrainerWorkloadRequestDto to ActiveMQ");
        jmsTemplate.convertAndSend(TRAINER_WORKLOAD_REQUEST_QUEUE, trainerWorkloadRequestDto);
    }
}