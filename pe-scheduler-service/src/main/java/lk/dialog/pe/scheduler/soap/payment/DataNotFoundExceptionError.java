package lk.dialog.pe.scheduler.soap.payment;



/**
 * DataNotFoundExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */


public class DataNotFoundExceptionError extends java.lang.Exception{

    private static final long serialVersionUID = 1484958975544L;
    
    private ECAPaymentStub.DataNotFoundExceptionError faultMessage;

    
        public DataNotFoundExceptionError() {
            super("DataNotFoundExceptionError");
        }

        public DataNotFoundExceptionError(java.lang.String s) {
           super(s);
        }

        public DataNotFoundExceptionError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public DataNotFoundExceptionError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(ECAPaymentStub.DataNotFoundExceptionError msg){
       faultMessage = msg;
    }
    
    public ECAPaymentStub.DataNotFoundExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    