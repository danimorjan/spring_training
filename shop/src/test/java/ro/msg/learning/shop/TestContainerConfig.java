package ro.msg.learning.shop;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.GenericContainer;


import javax.sql.DataSource;

@TestConfiguration
@Profile("test")
@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
public class TestContainerConfig {

    @Bean
    public GenericContainer<?> postgreSQLContainer() {
        GenericContainer<?> container = new GenericContainer<>("postgres:latest")
                .withExposedPorts(5432)
                .withEnv("POSTGRES_DB", "test_db")
                .withEnv("POSTGRES_USER", "test_user")
                .withEnv("POSTGRES_PASSWORD", "test_password");
        container.start();
        return container;
    }

    @Bean
    public DataSource dataSource(GenericContainer<?> postgreSQLContainer) {
        String jdbcUrl = "jdbc:postgresql://" + postgreSQLContainer.getHost() + ":"
                + postgreSQLContainer.getMappedPort(5432) + "/test_db";
        String username = "test_user";
        String password = "test_password";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    @DependsOn("dataSource")
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return flyway;
    }

}

