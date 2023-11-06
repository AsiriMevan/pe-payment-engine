
/**
 * AmbiguousSearchCriteriaExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package lk.dialog.pe.scheduler.soap.payment;

import lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub;

public class AmbiguousSearchCriteriaExceptionError extends Exception{

    private static final long serialVersionUID = 1484958975662L;
    
    private lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.AmbiguousSearchCriteriaExceptionError faultMessage;

    
        public AmbiguousSearchCriteriaExceptionError() {
            super("AmbiguousSearchCriteriaExceptionError");
        }

        public AmbiguousSearchCriteriaExceptionError(String s) {
           super(s);
        }

        public AmbiguousSearchCriteriaExceptionError(String s, Throwable ex) {
          super(s, ex);
        }

        public AmbiguousSearchCriteriaExceptionError(Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.AmbiguousSearchCriteriaExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.AmbiguousSearchCriteriaExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    