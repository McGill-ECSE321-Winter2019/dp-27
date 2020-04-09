package ca.mcgill.cooperator.config;

import io.github.cdimascio.dotenv.Dotenv;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * This class sets up a datasource to be used when running tests. Environment variables must be
 * defined in a dotenv (.env) file.
 *
 * @see DataSourceConfig.java for default datasource configuration
 */
@Configuration
@Profile("test")
@PropertySource("classpath:application-test.properties")
public class TestDataSourceConfig {

    @Bean
    public DataSource getTestDataSource() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dotenv.get("TEST_DATASOURCE_URL"));
        dataSourceBuilder.username(dotenv.get("TEST_DATASOURCE_USERNAME"));
        dataSourceBuilder.password(dotenv.get("TEST_DATASOURCE_PASSWORD"));

        return dataSourceBuilder.build();
    }
}
