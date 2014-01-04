package com.kalandyk.server.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by kamil on 1/4/14.
 */
@Configuration
@ComponentScan
@ImportResource("classpath*:spring-data-context.xml")
@EnableTransactionManagement
public class ApplicationConfig {

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new EmbeddedGraphDatabase("graph.db");
    }
}
