package ru.antush59.attestation03.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.time.Duration;


@Configuration
@Profile("test")
@EnableTransactionManagement
public class DataSourceConfigIT {


    @Bean(name = "dbDataSource")
    @ConfigurationProperties(prefix = "tires-properties.datasource")
    public DataSource getDataSource() {

        PostgreSQLContainer postgreSQLContainer = createPostgreSQLContainer();
        postgreSQLContainer.start();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        String testContainerDbUrl = String.format("jdbc:postgresql://%s:%s/%s", postgreSQLContainer.getContainerIpAddress(),
                postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgreSQLContainer.getDatabaseName());

        dataSource.setUrl(testContainerDbUrl);

        return dataSource;
    }

    private static PostgreSQLContainer createPostgreSQLContainer() {
        return (PostgreSQLContainer) new PostgreSQLContainer(
                "postgres")
                .withDatabaseName("tires_db")
                .withInitScript("db/migration/V1_0__create_tires_schema.sql")
                .withUsername("postgres")
                .withPassword("postgres")
                .withStartupTimeout(Duration.ofSeconds(600));
    }
}