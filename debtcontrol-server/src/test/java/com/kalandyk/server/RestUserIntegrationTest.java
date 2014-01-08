package com.kalandyk.server;
import com.kalandyk.api.model.User;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
/**
 * Created by kamil on 1/8/14.
 */
@Ignore
public class RestUserIntegrationTest {

    private RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/";

    @Before
    public void setUp(){
        restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJacksonHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
    }

    @Test
    public void getUserByUsernameTest() {
        final String url = baseUrl + "users/getUserByName/admin1";
        User user = restTemplate.getForObject(url, User.class);
        assertNotNull(user);
    }

    @Test
    public void addNewUserTest(){
        final String url = baseUrl + "users/createUser";
        User newUser = new User();
        newUser.setLogin("Badass");
        newUser.setEmail("example@o2.pl");
        User createdUser = restTemplate.postForObject(url, newUser, User.class);
        assertNotNull(createdUser);
    }

    @Test
    public void createNewUserFriendsNetworkTest(){

        User johny = new User();
        johny.setEmail("johny@emial.com");
        johny.setName("Johny");

        User richard = new User();
        johny.setEmail("richard@emial.com");
        johny.setName("Richard");

        User anna = new User();
        johny.setEmail("anna@emial.com");
        johny.setName("Anna");

        User marry = new User();
        johny.setEmail("marry@emial.com");
        johny.setName("Marry");








    }

}
