package ex.multisource.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    @Primary
    public DataSource mainDataSourceProxy(@Qualifier("mainDataSource") DataSource mainDataSource) {
        return new LazyConnectionDataSourceProxy(mainDataSource);
    }

    @Bean
    public DataSource subDataSourceProxy(@Qualifier("subDataSource") DataSource subDataSource) {
        return new LazyConnectionDataSourceProxy(subDataSource);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.main")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.sub")
    public DataSource subDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public DataSourceInitializer batchDataSourceInitializer(@Qualifier("subDataSourceProxy") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        if (isBatchTablesPresent(jdbcTemplate)) {
            return null;
        }

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(resourceLoader.getResource("classpath:org/springframework/batch/core/schema-postgresql.sql"));

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);

        return initializer;
    }

    private boolean isBatchTablesPresent(JdbcTemplate jdbcTemplate) {
        try {
            jdbcTemplate.execute("SELECT 1 FROM BATCH_JOB_INSTANCE LIMIT 1");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
