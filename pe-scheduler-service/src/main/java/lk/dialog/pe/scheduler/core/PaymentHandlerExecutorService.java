package lk.dialog.pe.scheduler.core;

import lk.dialog.pe.scheduler.dto.JobDetailDto;

import java.util.List;

public interface PaymentHandlerExecutorService {

    void submitRbmHandler(String traceId);

    void submitRbmRetryHandler(String traceId);

    void submitOcsHandler(String traceId);

    void submitCancelHandler(String traceId);

    void submitForceFulCheckRealizeHandler(String traceId);

    void submitForceFulCheckRealizeFailHandler(String traceId);

    List<JobDetailDto> setThreadDetails(List<JobDetailDto> jobDetailDtoList,String traceId);
}
