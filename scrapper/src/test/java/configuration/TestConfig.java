package configuration;

import environment.IntegrationEnvironment;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@TestConfiguration
public class TestConfig extends IntegrationEnvironment {
    @SneakyThrows
    @Bean
    public DataSource dataSource() {
        return new SingleConnectionDataSource(container.createConnection(""), false);
    }
}
