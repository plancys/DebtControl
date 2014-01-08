package com.kalandyk.server;

import com.kalandyk.api.model.User;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.service.UserService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
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
