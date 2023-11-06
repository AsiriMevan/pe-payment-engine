package lk.dialog.pe.scheduler.soap.stub.pool;

import lk.dialog.pe.scheduler.soap.custom.CUSTOMECADBTCustomECAStub;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class CUSTOMECADBTCustomECAStubPool extends GenericObjectPool<CUSTOMECADBTCustomECAStub> {

     /**
     * It uses the default configuration for pool provided by apache-commons-pool2
	 * @param factory
	 */
    public CUSTOMECADBTCustomECAStubPool(PooledObjectFactory<CUSTOMECADBTCustomECAStub> factory) {
        super(factory);
    }

    /**
     * This can be used to have full control over the pool using configuration
     * @param factory
     */
    public CUSTOMECADBTCustomECAStubPool(PooledObjectFactory<CUSTOMECADBTCustomECAStub> factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }

    /**
     * This can be used to have full control over the pool using configuration as well as abandon objects
     * @param factory
     */
    public CUSTOMECADBTCustomECAStubPool(PooledObjectFactory<CUSTOMECADBTCustomECAStub> factory, GenericObjectPoolConfig config,
                                         AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
