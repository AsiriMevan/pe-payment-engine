package lk.dialog.pe.scheduler.soap.payment;

/**
 * InvalidParameterCombinationError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */


public class InvalidParameterCombinationError extends java.lang.Exception{

    private static final long serialVersionUID = 1484958975624L;
    
    private lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.InvalidParameterCombinationError faultMessage;

    
        public InvalidParameterCombinationError() {
            super("InvalidParameterCombinationError");
        }

        public InvalidParameterCombinationError(java.lang.String s) {
           super(s);
        }

        public InvalidParameterCombinationError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public InvalidParameterCombinationError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.InvalidParameterCombinationError msg){
       faultMessage = msg;
    }
    
    public lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.InvalidParameterCombinationError getFaultMessage(){
       return faultMessage;
    }
}
    