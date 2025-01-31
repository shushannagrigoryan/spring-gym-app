package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TrainingEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateTrainerWorkloadService {
    private static final String BASE_URL = "http://localhost:8090";
    private static final String URI = "/updateWorkload";
    private final WebClient.Builder webClientBuilder;

    /**
     * Calling TrainerWorkloadService to update trainer's workload after adding/deleting a training.
     *
     * @param trainingEntity training added/deleted
     * @param actionType Add/Delete
     */
    public void updateTrainerWorkload(TrainingEntity trainingEntity, ActionType actionType) {
        TrainerWorkloadRequestDto workloadDto = getTrainerWorkloadRequestDto(trainingEntity, actionType);

        Mono<ResponseEntity<ResponseDto<String>>> res = webClientBuilder
            .baseUrl(BASE_URL)
            .build()
            .post()
            .uri(URI)
            .body(Mono.just(workloadDto), TrainerWorkloadRequestDto.class)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<>() {
            });


        ResponseEntity<ResponseDto<String>> result = res.block();
        if (result != null) {
            log.debug("Status code: {}", result.getStatusCode());
            if (result.getBody() != null) {
                log.debug(result.getBody().getMessage());
            }

        }
    }

    private static TrainerWorkloadRequestDto getTrainerWorkloadRequestDto(TrainingEntity trainingEntity,
                                                                          ActionType actionType) {
        TrainerWorkloadRequestDto workloadDto = new TrainerWorkloadRequestDto();
        workloadDto.setUsername(trainingEntity.getTrainer().getUser().getUsername());
        workloadDto.setFirstName(trainingEntity.getTrainer().getUser().getFirstName());
        workloadDto.setLastName(trainingEntity.getTrainer().getUser().getLastName());
        workloadDto.setIsActive(trainingEntity.getTrainer().getUser().isActive());
        workloadDto.setTrainingDate(trainingEntity.getTrainingDate());
        workloadDto.setTrainingDuration(trainingEntity.getTrainingDuration());
        workloadDto.setActionType(actionType);
        return workloadDto;
    }
}
