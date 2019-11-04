package ca.mcgill.cooperator.config;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

	@Primary
    @Bean
    public HikariDataSource getDataSource() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        
        HikariConfig jdbcConfig = new HikariConfig();
        jdbcConfig.setJdbcUrl(dotenv.get("SPRING_DATASOURCE_URL"));
        jdbcConfig.setUsername(dotenv.get("SPRING_DATASOURCE_USERNAME"));
        jdbcConfig.setPassword(dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        
        return new HikariDataSource(jdbcConfig);
    }
}
