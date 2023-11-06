package lk.dialog.pe.scheduler.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobConfig {

    protected String jobKey;
    protected Boolean stopJob;
    protected String stopJobUpdatedBy;
    protected String stopJobUpdateTime;
}
