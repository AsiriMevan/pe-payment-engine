package lk.dialog.pe.scheduler.soap.payment;

/**
 * ParameterExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */


public class ParameterExceptionError extends java.lang.Exception{

    private static final long serialVersionUID = 1484958975643L;
    
    private ECAPaymentStub.ParameterExceptionError faultMessage;

    
        public ParameterExceptionError() {
            super("ParameterExceptionError");
        }

        public ParameterExceptionError(java.lang.String s) {
           super(s);
        }

        public ParameterExceptionError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ParameterExceptionError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(ECAPaymentStub.ParameterExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.ParameterExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    