package lk.dialog.pe.scheduler.config.db;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class PeDataSourceConfig {

	@Bean(name = "writePostgreConfig")
	@ConfigurationProperties(prefix = "spring.datasource-pe.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Primary
	@Bean(name = "writePostgreDataSource")
	public HikariDataSource dataSource(@Qualifier("writePostgreConfig") HikariConfig hikariConfig) {
		return new HikariDataSource(hikariConfig);
	}

	@Primary
	@Bean(name = "peJdbcTemplate")
	public JdbcTemplate writeJdbcTemplate(@Qualifier("writePostgreDataSource")HikariDataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
