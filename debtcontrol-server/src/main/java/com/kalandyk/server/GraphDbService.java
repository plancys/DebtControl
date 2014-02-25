package com.kalandyk.server;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

/**
 * Created by kamil on 2/24/14.
 */

@Configuration
@EnableNeo4jRepositories(basePackages = "org.springframework.data.neo4j.repository")
class GraphDbService extends Neo4jConfiguration {

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase("http://localhost:7474/db/data/");
    }
}