package lk.dialog.pe.scheduler.soap.custom;

/**
 * ApplicationExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */


public class ApplicationExceptionError extends java.lang.Exception{

    private static final long serialVersionUID = 1502434786067L;
    
    private lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.ApplicationExceptionError faultMessage;

    
        public ApplicationExceptionError() {
            super("ApplicationExceptionError");
        }

        public ApplicationExceptionError(java.lang.String s) {
           super(s);
        }

        public ApplicationExceptionError(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ApplicationExceptionError(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.ApplicationExceptionError msg){
       faultMessage = msg;
    }
    
    public lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub.ApplicationExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    