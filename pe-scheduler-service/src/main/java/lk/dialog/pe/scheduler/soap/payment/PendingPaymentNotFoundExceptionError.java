package lk.dialog.pe.scheduler.soap.payment;

/**
 * PendingPaymentNotFoundExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */


public class PendingPaymentNotFoundExceptionError extends java.lang.Exception{

    private static final long serialVersionUID = 1484958975604L;
    
    private ECAPaymentStub.PendingPaymentNotFoundExceptionError faultMessage;

    
        public PendingPaymentNotFoundExceptionError() {
            super("PendingPaymentNotFoundExceptionError");
        }

        public PendingPaymentNotFoundExceptionError(java.lang.String s) {
           super(s);
        }

        public PendingPaymentNotFoundExceptionError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public PendingPaymentNotFoundExceptionError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(ECAPaymentStub.PendingPaymentNotFoundExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.PendingPaymentNotFoundExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    