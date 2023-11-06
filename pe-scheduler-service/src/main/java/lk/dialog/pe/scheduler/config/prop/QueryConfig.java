package lk.dialog.pe.scheduler.config.prop;

import lk.dialog.pe.scheduler.util.QUERY;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.HashMap;

@Configuration
@ImportResource({"classpath:job-query.xml","classpath:cache-query.xml"})
public class QueryConfig {

    public static HashMap<String, String> paymentCacheQueryMap;

    public static HashMap<String, String> jobQueryMap;

    public QueryConfig(HashMap<String, String> paymentH, HashMap<String, String> paymentCacheH){
        paymentCacheQueryMap = paymentCacheH;
        jobQueryMap=paymentH;
    }

    public static String getQueryByKey(String key){
        return jobQueryMap.get(key);
    }

    public static String getQuery(QUERY query){
        return jobQueryMap.get(query.name());
    }

    public static String getCacheQueryByKey(String key){
        return paymentCacheQueryMap.get(key);
    }

}
