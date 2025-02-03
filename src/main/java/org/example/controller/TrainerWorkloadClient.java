package org.example.controller;

import java.math.BigDecimal;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("trainer-workload-service")
public interface TrainerWorkloadClient {
    @PutMapping("/workload")
    ResponseEntity<ResponseDto<String>> updateWorkload(@RequestBody UpdateTrainerWorkloadRequestDto request);

    @GetMapping("/workload")
    ResponseEntity<ResponseDto<BigDecimal>> getWorkload(@RequestParam("username") String username,
                                                        @RequestParam("year") String year,
                                                        @RequestParam("month") String month);

}