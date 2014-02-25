package com.kalandyk.server;

import org.junit.After;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.rest.graphdb.RestGraphDatabase;

/**
 * Created by kamil on 2/23/14.
 */
public class DatabaseConnectionTest {

    private GraphDatabaseService gds;

    @Test
    public void connectToDatabase(){
      gds = new RestGraphDatabase("http://localhost:7474/db/data");

    }

    @After
    public void shutdown(){
       // gds.
    }
}
