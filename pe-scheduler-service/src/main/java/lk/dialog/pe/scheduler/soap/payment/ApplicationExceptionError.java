
/**
 * ApplicationExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package lk.dialog.pe.scheduler.soap.payment;

import lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub;

public class ApplicationExceptionError extends Exception{

    private static final long serialVersionUID = 1484958975467L;
    
    private lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ApplicationExceptionError faultMessage;

    
        public ApplicationExceptionError() {
            super("ApplicationExceptionError");
        }

        public ApplicationExceptionError(String s) {
           super(s);
        }

        public ApplicationExceptionError(String s, Throwable ex) {
          super(s, ex);
        }

        public ApplicationExceptionError(Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.ApplicationExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.ApplicationExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    