package lk.dialog.pe.scheduler.soap.stub.factory;

import lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub;
import org.apache.axis2.transport.http.impl.httpclient3.HttpTransportPropertiesImpl;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CUSTOMECADBTCustomECAStubFactory extends BasePooledObjectFactory<CUSTOMECADBTCustomECAStub> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CUSTOMECADBTCustomECAStubFactory.class);

    private String endPoint;
    private String username;
    private String password;
    private String timeOutInMilliSeconds;
    public CUSTOMECADBTCustomECAStubFactory(String endPoint){
        this.endPoint = endPoint;

    }
    public CUSTOMECADBTCustomECAStubFactory(String endPoint, String username, String password, String timeOutInMilliSeconds){
        this.endPoint = endPoint;
        this.username = username;
        this.password = password;
        this.timeOutInMilliSeconds = timeOutInMilliSeconds;
    }

    @Override
    public CUSTOMECADBTCustomECAStub create() throws Exception {
        CUSTOMECADBTCustomECAStub stubObj = new CUSTOMECADBTCustomECAStub(endPoint);
        HttpTransportPropertiesImpl.Authenticator basicAuthentication = new HttpTransportPropertiesImpl.Authenticator();
        basicAuthentication.setPassword(password);
        basicAuthentication.setUsername(username);
        stubObj._getServiceClient().getOptions()
                .setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, basicAuthentication);
        stubObj._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,
                Boolean.FALSE);
        stubObj._getServiceClient().getOptions()
                .setTimeOutInMilliSeconds(Integer.parseInt(timeOutInMilliSeconds));

        LOGGER.info("CUSTOMECADBTCustomECAStub Instance Created :"+stubObj);
        return stubObj;
    }


    @Override
    public PooledObject<CUSTOMECADBTCustomECAStub> wrap(CUSTOMECADBTCustomECAStub stubObj) {
        return new DefaultPooledObject<CUSTOMECADBTCustomECAStub>(stubObj);
    }

    /*
     * nothing to do when an object destroy
     */
    @Override
    public void destroyObject(PooledObject<CUSTOMECADBTCustomECAStub> poolStubObj) throws Exception {
        LOGGER.info("CUSTOMECADBTCustomECAStub Instance destroy called :"+poolStubObj);
        super.destroyObject(poolStubObj);

    }
    /*
     * false if obj is not valid and should be dropped from the pool, true otherwise
     */
    @Override
    public boolean validateObject(PooledObject<CUSTOMECADBTCustomECAStub> poolStubObj) {
        LOGGER.info("CUSTOMECADBTCustomECAStub validation called :"+poolStubObj);
        return super.validateObject(poolStubObj);
    }

    /*
     * Reinitialise an instance to be returned by the pool
     */
    @Override
    public void activateObject(PooledObject<CUSTOMECADBTCustomECAStub> poolStubObj) throws Exception {
        super.activateObject(poolStubObj);
    }

    /*
     * nothing to do when an object returned to pool
     */
    @Override
    public void passivateObject(PooledObject<CUSTOMECADBTCustomECAStub> poolStubObj) throws Exception {
        super.passivateObject(poolStubObj);
    }
}
