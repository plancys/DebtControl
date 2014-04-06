package com.kalandyk.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@ImportResource("classpath:neo4j-config.xml")
public class Neo4jConfig {
}
