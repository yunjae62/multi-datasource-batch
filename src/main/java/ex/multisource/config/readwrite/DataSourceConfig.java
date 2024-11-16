package ex.multisource.config.readwrite;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class DataSourceConfig {

    /**
     * 메인 DB 데이터소스 (읽기 + 쓰기)
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.main")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 서브 DB 데이터소스 (읽기 전용)
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.sub")
    public DataSource subDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * 라우팅 데이터소스 - readOnly 속성에 따라 라우팅
     */
    @Bean
    @DependsOn({"mainDataSource", "subDataSource"})
    public DataSource routingDataSource(
        @Qualifier("mainDataSource") DataSource mainDataSource,
        @Qualifier("subDataSource") DataSource subDataSource
    ) {
        TransactionRoutingDataSource routingDataSource = new TransactionRoutingDataSource();

        Map<Object, Object> dataSourceMap = Map.of(
            DataSourceType.READ_WRITE, mainDataSource,
            DataSourceType.READ_ONLY, subDataSource
        );

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(mainDataSource);

        return routingDataSource;
    }

    /**
     * 데이터소스 프록시 - 트랜잭션 진입 후 readOnly 속성에 따라 데이터소스 결정
     */
    @Bean
    @Primary
    @DependsOn({"routingDataSource"})
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}
