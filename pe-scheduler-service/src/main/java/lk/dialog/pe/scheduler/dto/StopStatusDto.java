package lk.dialog.pe.scheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StopStatusDto {
    private String jobKey;
    private Boolean stopStatus;
}
