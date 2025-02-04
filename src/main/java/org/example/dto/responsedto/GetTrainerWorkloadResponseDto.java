package org.example.dto.responsedto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetTrainerWorkloadResponseDto {
    private String username;
    private String year;
    private String month;
    private BigDecimal workload;

    /**
     * Constructor.
     */
    public GetTrainerWorkloadResponseDto(String username, String year, String month) {
        this.username = username;
        this.year = year;
        this.month = month;
    }
}
