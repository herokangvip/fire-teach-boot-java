package com.example.demo.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author king
 **/
@Configuration
@MapperScan(value = {"com.example.demo.dao",
        "com.example.demo.dao.extend"})
public class DataSourceConfig {


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public MasterDataSourceProperties masterDataSourceProperties() {
        return new MasterDataSourceProperties();
    }

    static class MasterDataSourceProperties extends DataSourceProperties {
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public SlaveDataSourceProperties slaveDataSourceProperties() {
        return new SlaveDataSourceProperties();
    }

    static class SlaveDataSourceProperties extends DataSourceProperties {
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public DataSource masterDataSource(
            @Qualifier("masterDataSourceProperties") MasterDataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    public DataSource slaveDataSource(
            @Qualifier("slaveDataSourceProperties") SlaveDataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory mdbSqlSessionFactoryBean(
            @Qualifier("masterDataSource") DataSource datasource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(datasource);
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath*:mapper/mapping/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }


    /**
     * 事务管理者
     *
     * @param datasource datasource
     * @return PlatformTransactionManager
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager mdbTransactionManager(
            @Qualifier("masterDataSource") DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

}
