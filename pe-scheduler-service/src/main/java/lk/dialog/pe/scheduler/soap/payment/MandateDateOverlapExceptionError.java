package lk.dialog.pe.scheduler.soap.payment;
/**
 * MandateDateOverlapExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */


public class MandateDateOverlapExceptionError extends Exception{

    private static final long serialVersionUID = 1484958975679L;
    
    private lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.MandateDateOverlapExceptionError faultMessage;

    
        public MandateDateOverlapExceptionError() {
            super("MandateDateOverlapExceptionError");
        }

        public MandateDateOverlapExceptionError(String s) {
           super(s);
        }

        public MandateDateOverlapExceptionError(String s, Throwable ex) {
          super(s, ex);
        }

        public MandateDateOverlapExceptionError(Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.MandateDateOverlapExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.MandateDateOverlapExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    