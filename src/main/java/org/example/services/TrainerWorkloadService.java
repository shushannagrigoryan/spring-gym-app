package org.example.services;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
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
public class TrainerWorkloadService {
    private static final String BASE_URL = "http://localhost:8090";
    private static final String UPDATE_WORKLOAD_URI = "/updateWorkload";
    private static final String GET_WORKLOAD_URI = "/workload";
    private final WebClient.Builder webClientBuilder;

    private static UpdateTrainerWorkloadRequestDto getTrainerWorkloadRequestDto(TrainingEntity trainingEntity,
                                                                                ActionType actionType) {
        UpdateTrainerWorkloadRequestDto workloadDto = new UpdateTrainerWorkloadRequestDto();
        workloadDto.setUsername(trainingEntity.getTrainer().getUser().getUsername());
        workloadDto.setFirstName(trainingEntity.getTrainer().getUser().getFirstName());
        workloadDto.setLastName(trainingEntity.getTrainer().getUser().getLastName());
        workloadDto.setIsActive(trainingEntity.getTrainer().getUser().isActive());
        workloadDto.setTrainingDate(trainingEntity.getTrainingDate());
        workloadDto.setTrainingDuration(trainingEntity.getTrainingDuration());
        workloadDto.setActionType(actionType);
        return workloadDto;
    }

    /**
     * Calling TrainerWorkloadService to update trainer's workload after adding/deleting a training.
     *
     * @param trainingEntity training added/deleted
     * @param actionType     Add/Delete
     */
    public void updateTrainerWorkload(TrainingEntity trainingEntity, ActionType actionType) {
        UpdateTrainerWorkloadRequestDto workloadDto = getTrainerWorkloadRequestDto(trainingEntity, actionType);

        Mono<ResponseEntity<ResponseDto<String>>> res = webClientBuilder
            .baseUrl(BASE_URL)
            .build()
            .post()
            .uri(UPDATE_WORKLOAD_URI)
            .body(Mono.just(workloadDto), UpdateTrainerWorkloadRequestDto.class)
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

    /**
     * Calling TrainerWorkloadService to get trainer's workload after adding/deleting a training.
     */

    public BigDecimal getTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        //UpdateTrainerWorkloadRequestDto workloadDto = getTrainerWorkloadRequestDto(trainingEntity, actionType);

        Mono<ResponseEntity<ResponseDto<BigDecimal>>> res = webClientBuilder
            .baseUrl(BASE_URL)
            .build()
            .get()
            //            .uri(GET_WORKLOAD_URI)
            //            //.body(Mono.just(trainerWorkloadRequestDto), TrainerWorkloadRequestDto.class)
            .uri(uriBuilder -> uriBuilder.path(GET_WORKLOAD_URI)
                .queryParam("username", trainerWorkloadRequestDto.getUsername())
                .queryParam("year", trainerWorkloadRequestDto.getTrainingYear())
                .queryParam("month", trainerWorkloadRequestDto.getTrainingMonth())
                .build())
            .retrieve()
            .toEntity(new ParameterizedTypeReference<>() {
            });


        ResponseEntity<ResponseDto<BigDecimal>> result = res.block();
        if (result != null) {
            log.debug("Status code: {}", result.getStatusCode());
            if (result.getBody() != null) {
                log.debug(result.getBody().getMessage());
                log.debug(result.getBody().getPayload().toString());
            }
        }
        assert result != null;
        return result.getBody().getPayload();
    }
}
