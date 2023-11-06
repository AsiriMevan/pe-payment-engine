package lk.dialog.pe.scheduler.soap.payment;

import lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub;

/**
 * NullParameterExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */


public class NullParameterExceptionError extends java.lang.Exception{

    private static final long serialVersionUID = 1484958975501L;
    
    private ECAPaymentStub.NullParameterExceptionError faultMessage;

    
        public NullParameterExceptionError() {
            super("NullParameterExceptionError");
        }

        public NullParameterExceptionError(java.lang.String s) {
           super(s);
        }

        public NullParameterExceptionError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public NullParameterExceptionError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(ECAPaymentStub.NullParameterExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.NullParameterExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    