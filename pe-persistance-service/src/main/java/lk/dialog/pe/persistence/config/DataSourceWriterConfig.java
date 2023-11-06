package lk.dialog.pe.persistence.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "lk.dialog.pe.persistence.writer.repository.impl")
public class DataSourceWriterConfig {

	@Primary
	@Bean(name = "writePostgreDb")
	@ConfigurationProperties("spring.datasource-write")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "writePostgreDataSource")
	@ConfigurationProperties(prefix = "spring.datasource-write")
	public DataSource dataSource(@Qualifier("writePostgreDb") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().build();
	}

	@Primary
	@Bean(name = "writeJdbcTemplate")
	public JdbcTemplate writeJdbcTemplate(@Qualifier("writePostgreDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
