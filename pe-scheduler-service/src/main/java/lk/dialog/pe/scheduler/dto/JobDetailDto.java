package lk.dialog.pe.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lk.dialog.pe.scheduler.domain.QuartzJobDetail;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDetailDto{

    protected String jobKey;
    protected Boolean stopJob;
    private String stopJobLastUpdatedBy;
    private String stopJobLastUpdatedDate;
    private String threadPoolName;
    private Integer configuredThreads;
    private Integer activeThreads;
    private QuartzJobDetail quartzJobDetail;



}
