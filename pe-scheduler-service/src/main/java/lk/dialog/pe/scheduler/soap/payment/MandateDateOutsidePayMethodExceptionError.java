
/**
 * MandateDateOutsidePayMethodExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package lk.dialog.pe.scheduler.soap.payment;

import lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub;

public class MandateDateOutsidePayMethodExceptionError extends Exception{

    private static final long serialVersionUID = 1484958975697L;
    
    private lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.MandateDateOutsidePayMethodExceptionError faultMessage;

    
        public MandateDateOutsidePayMethodExceptionError() {
            super("MandateDateOutsidePayMethodExceptionError");
        }

        public MandateDateOutsidePayMethodExceptionError(String s) {
           super(s);
        }

        public MandateDateOutsidePayMethodExceptionError(String s, Throwable ex) {
          super(s, ex);
        }

        public MandateDateOutsidePayMethodExceptionError(Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(lk.dialog.pe.scheduler.soap.payment.ECAPaymentStub.MandateDateOutsidePayMethodExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.MandateDateOutsidePayMethodExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    