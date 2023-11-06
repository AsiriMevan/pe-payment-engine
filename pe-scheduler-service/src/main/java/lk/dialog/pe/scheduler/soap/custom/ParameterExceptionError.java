
/**
 * ParameterExceptionError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package lk.dialog.pe.scheduler.soap.custom;

public class ParameterExceptionError extends Exception{

    private static final long serialVersionUID = 1502434786095L;
    
    private CUSTOMECADBTCustomECAStub.ParameterExceptionError faultMessage;

    
        public ParameterExceptionError() {
            super("ParameterExceptionError");
        }

        public ParameterExceptionError(String s) {
           super(s);
        }

        public ParameterExceptionError(String s, Throwable ex) {
          super(s, ex);
        }

        public ParameterExceptionError(Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(CUSTOMECADBTCustomECAStub.ParameterExceptionError msg){
       faultMessage = msg;
    }
    
    public CUSTOMECADBTCustomECAStub.ParameterExceptionError getFaultMessage(){
       return faultMessage;
    }
}
    