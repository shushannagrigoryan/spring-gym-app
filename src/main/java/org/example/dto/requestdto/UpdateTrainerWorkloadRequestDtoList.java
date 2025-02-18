package org.example.dto.requestdto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateTrainerWorkloadRequestDtoList {
    private List<UpdateTrainerWorkloadRequestDto> updateTrainerWorkloadRequestDtoList;
}