package ca.mcgill.cooperator.config;

import io.github.cdimascio.dotenv.Dotenv;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class DataSourceConfig {

    @Primary
    @Bean
    public DataSource getDataSource() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dotenv.get("DATASOURCE_URL"));
        dataSourceBuilder.username(dotenv.get("DATASOURCE_USERNAME"));
        dataSourceBuilder.password(dotenv.get("DATASOURCE_PASSWORD"));

        return dataSourceBuilder.build();
    }
}
