package ex.multisource.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class DataSourceConfig {

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
}
