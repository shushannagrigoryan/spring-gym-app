package org.example.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDtoList;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateTrainerWorkloadSenderService {
    private static final String UPDATE_TRAINER_WORKLOAD_QUEUE = "update-trainer-workload-queue";
    private static final String UPDATE_TRAINER_WORKLOAD_QUEUE_TRAININGS_LIST =
        "update-trainer-workload-queue-trainings-list";

    private final JmsTemplate jmsTemplate;

    /**
     * Sends message to ActiveMQ UPDATE_TRAINER_WORKLOAD_QUEUE_TRAININGS_LIST queue.
     *
     * @param updateTrainerWorkloadRequestDto {@code UpdateTrainerWorkloadRequestDto}
     */
    public void send(UpdateTrainerWorkloadRequestDto updateTrainerWorkloadRequestDto) {
        log.debug("Sending UpdateTrainerWorkloadRequestDto to ActiveMQ");
        log.debug("jmsTemplate converter = {}", jmsTemplate.getMessageConverter());
        System.out.println(updateTrainerWorkloadRequestDto);
        jmsTemplate.convertAndSend(UPDATE_TRAINER_WORKLOAD_QUEUE, updateTrainerWorkloadRequestDto);
        log.debug("Successfully sent UpdateTrainerWorkloadRequestDto to ActiveMQ.");
    }

    /**
     * Sends message to ActiveMQ UPDATE_TRAINER_WORKLOAD_QUEUE_TRAININGS_LIST queue.
     *
     * @param updateTrainerWorkloadRequestDtoList {@code List<UpdateTrainerWorkloadRequestDto>}
     */
    public void send(List<UpdateTrainerWorkloadRequestDto> updateTrainerWorkloadRequestDtoList) {
        log.debug("Sending UpdateTrainerWorkloadRequestDto to ActiveMQ");
        log.debug("jmsTemplate converter = {}", jmsTemplate.getMessageConverter());
        System.out.println(updateTrainerWorkloadRequestDtoList);
        jmsTemplate.convertAndSend(UPDATE_TRAINER_WORKLOAD_QUEUE_TRAININGS_LIST,
            new UpdateTrainerWorkloadRequestDtoList(updateTrainerWorkloadRequestDtoList));
        log.debug("Successfully sent UpdateTrainerWorkloadRequestDto to ActiveMQ.");
    }
}
