package lk.dialog.pe.scheduler.soap.stub.invoker;

import lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub;
import lk.dialog.pe.scheduler.soap.stub.factory.CUSTOMECADBTCustomECAStubFactory;
import lk.dialog.pe.scheduler.soap.stub.pool.CUSTOMECADBTCustomECAStubPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class CUSTOMECADBTCustomECAStubInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(CUSTOMECADBTCustomECAStubInvoker.class);

    static CUSTOMECADBTCustomECAStubPool stubPool;

    public void setUp(String customEP, String userName, String password, String timeOutInMilliSeconds, String maxIdle, String maxTotal, String minIdle, String maxWait,
                      String testOnBorrow, String testOnReturn) {
        if (stubPool == null) {
            /* generic pool configuration */
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxIdle(Integer.parseInt(maxIdle));
            config.setMaxTotal(Integer.parseInt(maxTotal));
            config.setMinIdle(Integer.parseInt(minIdle));
            config.setMaxWaitMillis(Long.parseLong(maxWait));
            config.setTestOnBorrow(Boolean.getBoolean(testOnBorrow));
            config.setTestOnReturn(Boolean.getBoolean(testOnReturn));
            stubPool = new CUSTOMECADBTCustomECAStubPool(new CUSTOMECADBTCustomECAStubFactory(customEP, userName, password, timeOutInMilliSeconds), config);
            LOGGER.info("Pool Initated :" + "Max:" + maxTotal + ",MaxIdel:" + maxIdle + ",MinIdel" + minIdle + ",MaxWait:"
                    + maxWait);
        }
    }


    public CUSTOMECADBTCustomECAStub getCUSTOMECADBTCustomECAStub() throws Exception {
        CUSTOMECADBTCustomECAStub stubObj = null;
        try {
            LOGGER.info("Pool Statistics Before Borrow ->" + "Active:" + stubPool.getNumActive() + ",Idle:"
                    + stubPool.getNumIdle());
            stubObj = stubPool.borrowObject();
            LOGGER.info("Pool Object Received :" + stubObj);
        } catch (RemoteException e) {
            LOGGER.error("Remote Err:", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Err:", e);
            throw e;
        }
        return stubObj;
    }

    public void returnCUSTOMECADBTCustomECAStub(CUSTOMECADBTCustomECAStub stubObj) {
        if (stubObj != null) {
            try {
                stubPool.returnObject(stubObj);
                LOGGER.info("Pool Statistics After Return ->" + "Active:" + stubPool.getNumActive() + ",Idle:"
                        + stubPool.getNumIdle());
            } catch (Exception e) {
                LOGGER.error("Error while returning object to pool: " + e.getMessage());
            }
        }
    }
}
