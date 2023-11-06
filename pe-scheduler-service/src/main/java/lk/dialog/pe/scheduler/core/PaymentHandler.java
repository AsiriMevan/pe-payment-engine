package lk.dialog.pe.scheduler.core;

import lk.dialog.pe.scheduler.config.prop.SmsConfig;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.service.PeDbIntegrationService;
import lk.dialog.pe.scheduler.service.SmsService;
import lk.dialog.pe.scheduler.service.SoapIntegrationService;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
/*base class for all payment handlers */
@Slf4j
public abstract class PaymentHandler implements PaymentJob {
	@Autowired
	protected PeDbIntegrationService peIntegrationService;
	@Autowired
	protected SoapIntegrationService soapIntegrationService;
	@Autowired
	protected SmsService smsService;

	@Autowired
	protected SmsConfig smsConfig;

	protected String traceId;

	public void logPayment(Payment paymentDTO) {
		log.info("Payemnt sent :" + paymentDTO);
	}


	protected void loggerInitTraceId(){
		this.traceId = SchUtil.generateTraceId();
		MDC.put("traceId",traceId);
	}
	protected void loggerAppendTrxId(Long trxId){
		MDC.put("traceId",traceId+"-"+trxId);
	}

	protected void loggerClearTraceId(){
		MDC.clear();
	}

	protected String getFailMessageWithCause(Exception ex){
		if(ex.getCause()!=null)
		return "Error:"+ex.getMessage()+",Cause :"+ex.getCause().toString();
		return  getFailMessage(ex);
	}

	protected String getFailMessage(Exception ex){
		return "Error:"+ex.getMessage();
	}

}
