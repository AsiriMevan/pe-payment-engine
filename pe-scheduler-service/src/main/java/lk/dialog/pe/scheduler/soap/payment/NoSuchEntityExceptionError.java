
/**
 * NoSuchEntityExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package lk.dialog.pe.scheduler.soap.payment;

import lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub;

public class NoSuchEntityExceptionError extends Exception{

    private static final long serialVersionUID = 1484958975527L;
    
    private lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.NoSuchEntityExceptionError faultMessage;

    
        public NoSuchEntityExceptionError() {
            super("NoSuchEntityExceptionError");
        }

        public NoSuchEntityExceptionError(String s) {
           super(s);
        }

        public NoSuchEntityExceptionError(String s, Throwable ex) {
          super(s, ex);
        }

        public NoSuchEntityExceptionError(Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.NoSuchEntityExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.NoSuchEntityExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    