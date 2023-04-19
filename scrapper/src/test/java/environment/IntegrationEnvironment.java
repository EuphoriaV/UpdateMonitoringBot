package environment;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;


public abstract class IntegrationEnvironment {
    protected static final PostgreSQLContainer container;
    protected static final JdbcTemplate jdbcTemplate;

    static {
        container = new PostgreSQLContainer("postgres:14")
                .withDatabaseName("scrapper")
                .withUsername("EuphoriaV")
                .withPassword("1234");
        container.start();
        try {
            Connection connection = container.createConnection("");
            Liquibase liquibase = new Liquibase("master.xml", new DirectoryResourceAccessor(
                    Path.of(".").toAbsolutePath().getParent().getParent().resolve("migrations")),
                    new JdbcConnection(connection));
            liquibase.update();
            jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(
                    container.createConnection(""), true));
        } catch (SQLException | LiquibaseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
