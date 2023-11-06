package lk.dialog.pe.scheduler.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
@Configuration
public class CcbsDataSourceConfig {

    @Bean(name = "readPostgreConfig")
    @ConfigurationProperties(prefix = "spring.datasource-ccbs.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }


    @Bean(name = "readPostgreDataSource")
    public HikariDataSource dataSource(@Qualifier("readPostgreConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }


    @Bean(name = "ccbsJdbcTemplate")
    public JdbcTemplate writeJdbcTemplate(@Qualifier("readPostgreDataSource")HikariDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}

