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
    protected static final PostgreSQLContainer CONTAINER;
    protected static final JdbcTemplate JDBC_TEMPLATE;

    static {
        CONTAINER = new PostgreSQLContainer("postgres:14")
            .withDatabaseName("scrapper")
            .withUsername("EuphoriaV")
            .withPassword("1234");
        CONTAINER.start();
        try {
            Connection connection = CONTAINER.createConnection("");
            Liquibase liquibase = new Liquibase("master.xml", new DirectoryResourceAccessor(
                Path.of(".").toAbsolutePath().getParent().getParent().resolve("migrations")),
                new JdbcConnection(connection)
            );
            liquibase.update();
            JDBC_TEMPLATE = new JdbcTemplate(new SingleConnectionDataSource(
                CONTAINER.createConnection(""), true));
        } catch (SQLException | LiquibaseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
