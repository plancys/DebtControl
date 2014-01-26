package com.kalandyk.server;

import com.kalandyk.api.model.FriendshipRequest;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by kamil on 1/8/14.
 */
@Ignore
public class RestUserIntegrationTest {

    private RestTemplate restTemplate;
    private final String baseUrl = "http://192.168.0.22:8080";

    private final String usernameJohny = "Johny";
    private final String usernameRichard = "Richard";
    private final String usernameAnna = "Anna";
    private final String usernameMarry = "Marry";

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

        final String url = baseUrl + "users/createUser";

        User johny = new User();
        johny.setEmail("johny@emial.com");
        johny.setLogin(usernameJohny);

        User richard = new User();
        richard.setEmail("richard@emial.com");
        richard.setLogin(usernameRichard);


        User anna = new User();
        anna.setEmail("anna@emial.com");
        anna.setLogin(usernameAnna);


        User marry = new User();
        marry.setEmail("marry@emial.com");
        marry.setLogin(usernameMarry);



        richard = restTemplate.postForObject(url, richard, User.class);
        assertNotNull(richard);

        johny = restTemplate.postForObject(url, johny, User.class);
        assertNotNull(johny);

        anna = restTemplate.postForObject(url, anna, User.class);
        assertNotNull(anna);

        marry = restTemplate.postForObject(url, marry, User.class);
        assertNotNull(johny);


        String addFriendUrl = "users/addFriend";
        FriendshipRequest friendshipRequest = new FriendshipRequest();
        friendshipRequest.setRequester(richard);
        friendshipRequest.setTarget(johny);
        Boolean friendRequestAdded = restTemplate.postForObject(baseUrl+addFriendUrl, friendshipRequest, Boolean.class);
        assertTrue(friendRequestAdded);

        friendshipRequest.setRequester(anna);
        friendshipRequest.setTarget(marry);
        friendRequestAdded = restTemplate.postForObject(baseUrl+addFriendUrl, friendshipRequest, Boolean.class);
        assertTrue(friendRequestAdded);

        friendshipRequest.setRequester(richard);
        friendshipRequest.setTarget(marry);
        friendRequestAdded = restTemplate.postForObject(baseUrl+addFriendUrl, friendshipRequest, Boolean.class);
        assertTrue(friendRequestAdded);

        String acceptFriendUrl = "users/acceptFriendshipRequest";
        friendshipRequest.setRequester(richard);
        friendshipRequest.setTarget(marry);
        friendRequestAdded = restTemplate.postForObject(baseUrl+acceptFriendUrl, friendshipRequest, Boolean.class);
        assertTrue(friendRequestAdded);


        String cancelFriendUrl = "users/cancelFriendshipRequest";
        friendshipRequest.setRequester(richard);
        friendshipRequest.setTarget(johny);
        friendRequestAdded = restTemplate.postForObject(baseUrl+cancelFriendUrl, friendshipRequest, Boolean.class);
        assertTrue(friendRequestAdded);





    }

    @Test
    public void addUser(){
        final String url = baseUrl + "users/createUser";

        User johny = new User();
        johny.setEmail("johny@emial.com");
        johny.setLogin(usernameJohny);
        johny = restTemplate.postForObject(url, johny, User.class);
        assertNotNull(johny);


    }

    @Test
    public void login(){
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setLogin("Johny");
        userCredentials.setPassword("");
        User user = restTemplate.postForObject(baseUrl+"users/login", userCredentials, User.class);
        assertNotNull(user);

    }

}
