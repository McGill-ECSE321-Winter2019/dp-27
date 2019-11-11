package ca.mcgill.cooperator.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class TestDataSourceConfig {

    @Bean
    @Profile("test")
    public DataSource getTestDataSource() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dotenv.get("TEST_DATASOURCE_URL"));
        dataSourceBuilder.username(dotenv.get("TEST_DATASOURCE_USERNAME"));
        dataSourceBuilder.password(dotenv.get("TEST_DATASOURCE_PASSWORD"));

        return dataSourceBuilder.build();
    }
}
