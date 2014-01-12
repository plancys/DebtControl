package com.kalandyk.server;

import com.kalandyk.server.service.UserService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by kamil on 1/7/14.
 */
@ContextConfiguration(locations =   "classpath:*/WEB-INF/application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional("transactionManager")
@Configurable
@Ignore
public class Neo4jConnectionTest {

    @Autowired
    UserService userService;
//
//    @Autowired
//    private Neo4jTemplate neo4jTemplate;

    @Test
    public void neo4jConnectionTest() {



    }

}
