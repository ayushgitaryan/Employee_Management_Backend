package com.example.Emp.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    @Value("${SPRING_DATASOURCE_URL:${DATABASE_URL:}}")
    private String databaseUrl;

    @Value("${SPRING_DATASOURCE_USERNAME:${DATABASE_USERNAME:}}")
    private String username;

    @Value("${SPRING_DATASOURCE_PASSWORD:${DATABASE_PASSWORD:}}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        if (databaseUrl != null && !databaseUrl.trim().isEmpty() &&
                (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"))) {
            try {
                // Parse Render/Heroku Postgres URL: postgres://username:password@host:port/database
                URI uri = new URI(databaseUrl);
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();
                String query = uri.getQuery();

                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
                if (query != null && !query.isEmpty()) {
                    jdbcUrl += "?" + query;
                }

                String userInfo = uri.getUserInfo();
                String parsedUsername = username;
                String parsedPassword = password;

                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    parsedUsername = parts[0];
                    parsedPassword = parts[1];
                }

                dataSource.setDriverClassName("org.postgresql.Driver");
                dataSource.setJdbcUrl(jdbcUrl);
                if (parsedUsername != null && !parsedUsername.isEmpty()) {
                    dataSource.setUsername(parsedUsername);
                }
                if (parsedPassword != null && !parsedPassword.isEmpty()) {
                    dataSource.setPassword(parsedPassword);
                }

                return dataSource;
            } catch (URISyntaxException e) {
                throw new RuntimeException("Failed to parse database URI: " + databaseUrl, e);
            }
        } else if (databaseUrl != null && databaseUrl.startsWith("jdbc:postgresql:")) {
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setJdbcUrl(databaseUrl);
            if (username != null && !username.isEmpty()) {
                dataSource.setUsername(username);
            }
            if (password != null && !password.isEmpty()) {
                dataSource.setPassword(password);
            }
            return dataSource;
        } else if (databaseUrl != null && !databaseUrl.trim().isEmpty()) {
            dataSource.setJdbcUrl(databaseUrl);
            if (username != null && !username.isEmpty()) {
                dataSource.setUsername(username);
            }
            if (password != null && !password.isEmpty()) {
                dataSource.setPassword(password);
            }
            return dataSource;
        } else {
            // Local fallback to in-memory H2
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setJdbcUrl("jdbc:h2:mem:empdb;DB_CLOSE_DELAY=-1");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
            return dataSource;
        }
    }
}
