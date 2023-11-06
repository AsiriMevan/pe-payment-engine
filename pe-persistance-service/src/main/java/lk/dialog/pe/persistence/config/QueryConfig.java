package lk.dialog.pe.persistence.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
public class QueryConfig {

	private ApplicationContext context;

	QueryConfig() {
		this.context = new ClassPathXmlApplicationContext("sql-query-context.xml", "cache-query.xml");
	}

	@Bean(name = "queryMap")
	public Map<String, String> getBean() {
		return (HashMap<String, String>) this.context.getBean("queryHandler");
	}

	@Bean(name = "cacheMap")
	public Map<String, String> cacheMapBean() {
		return (HashMap<String, String>) context.getBean("paymentCacheH");
	}

	@Bean(name = "paymentMap")
	public Map<String, String> paymentBean() {
		return (HashMap<String, String>) context.getBean("paymentHandler");
	}
}
