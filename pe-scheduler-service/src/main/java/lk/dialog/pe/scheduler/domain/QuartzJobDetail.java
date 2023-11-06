package lk.dialog.pe.scheduler.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzJobDetail {

    private String startTime;
    private String nextFireTime;
    private String previousFireTime;
    private String triggerState;
}
