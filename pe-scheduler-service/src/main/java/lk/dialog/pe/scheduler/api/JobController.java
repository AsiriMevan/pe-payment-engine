package lk.dialog.pe.scheduler.api;

import io.swagger.annotations.*;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.scheduler.dto.JobDetailDto;
import lk.dialog.pe.scheduler.dto.StopStatusDto;
import lk.dialog.pe.scheduler.service.JobConfigService;
import lk.dialog.pe.scheduler.util.JobKeys;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {
    @Autowired
    JobConfigService jobConfigService;

    @ApiOperation(value = "Get All Job details", notes = "The request parameter traceId is mandatory")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @ApiImplicitParams({@ApiImplicitParam(value = "Trace Id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
    @GetMapping(value = "/get-all-job-details")
    public ResponseEntity<?> getAllJobDetails(@RequestParam String traceId) throws DCPEException {
        Instant start = Instant.now();
        log.info("getAllJobDetailsRequest  traceId={}",traceId);
        if(DCPEUtil.traceIdValidation(traceId)){
            log.error("getJobDetailsResponse Invalid traceId={}",traceId);
            throw new DCPEException("Invalid traceId",200);
        }
        MDC.put("traceId",traceId);
        List<JobDetailDto> response = jobConfigService.getAllJobDetails(traceId);
        String responseString = SchUtil.convertToString(response);
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        log.info("getAllJobDetailsResponse success traceId={}|timeTaken={}|response={}",traceId,timeTaken,responseString);
        return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, response));
    }

    @ApiOperation(value = "Update stop staus for a job by its key", notes = "The request parameters user,traceId is mandatory")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @ApiImplicitParams({@ApiImplicitParam(value = "Trace Id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "user name of the requester", paramType = "query", name = "user", required = true, dataType = "string") })
    @PutMapping (value = "/stop-status")
    public ResponseEntity<?> updateStopStatus(@RequestBody StopStatusDto stopStatusDto,@RequestParam String user, @RequestParam String traceId) throws DCPEException {
        Instant start = Instant.now();
        String requestString = SchUtil.convertToString(stopStatusDto);
        log.info("updateStopStatusRequest  traceId={}|request={}",traceId,requestString);
        if(DCPEUtil.traceIdValidation(traceId)){
            log.error("updateStopStatusResponse Invalid traceId={}",traceId);
            throw new DCPEException("Invalid traceId",200);
        }
        MDC.put("traceId",traceId);
        JobKeys jobKey = JobKeys.getJobTypeByKey(stopStatusDto.getJobKey());
        if(jobKey==null){
            log.error("updateStopStatusResponse Invalid jobKey={}",stopStatusDto.getJobKey());
            throw new DCPEException("Invalid jobKey",200);
        }
        Boolean response = jobConfigService.updateStopJobStatus(jobKey,stopStatusDto.getStopStatus(),user);
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        log.info("updateStopStatusResponse success traceId={}|timeTaken={}|response={}",traceId,timeTaken,response);
        return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, Collections.singletonMap("updateStatus",response)));
    }
}
