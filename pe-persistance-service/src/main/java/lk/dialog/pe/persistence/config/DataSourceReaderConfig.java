package lk.dialog.pe.persistence.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "lk.dialog.pe.persistence.reader.repository.impl")
	public class DataSourceReaderConfig {

		@Bean(name = "readPostgreDb")
		@ConfigurationProperties("spring.datasource-read")
		public DataSourceProperties dataSourceProperties() {
			return new DataSourceProperties();
		}

		@Bean(name = "readPostgreDataSource")
		@ConfigurationProperties(prefix = "spring.datasource-read")
		public DataSource dataSource(@Qualifier("readPostgreDb") DataSourceProperties properties) {
			return properties.initializeDataSourceBuilder().build();
		}
		
	    @Bean(name = "readJdbcTemplate")
	    public JdbcTemplate readJdbcTemplate(@Qualifier("readPostgreDataSource")DataSource dataSource) {
	            return new JdbcTemplate(dataSource);
	    }

  }
	
