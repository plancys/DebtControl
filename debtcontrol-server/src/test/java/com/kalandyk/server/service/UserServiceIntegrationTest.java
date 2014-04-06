package com.kalandyk.server.service;

import org.dozer.Mapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/DebtControlTest-context.xml"})
@Transactional
@Ignore//Mock AuthUtil
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;

    @Test
    public void shouldCreateUser() {
    }

}
