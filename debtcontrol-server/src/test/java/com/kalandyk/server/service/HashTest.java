package com.kalandyk.server.service;

import com.kalandyk.api.model.security.PasswordUtils;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.assertEquals;


public class HashTest {

    @Test
    public void hashTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "a";
        String encodedPassword = PasswordUtils.getHashFromPassword(password);
        assertEquals(encodedPassword, PasswordUtils.getHashFromPassword(password));
        assertEquals(encodedPassword, PasswordUtils.getHashFromPassword(password));
        assertEquals(encodedPassword, PasswordUtils.getHashFromPassword(password));

    }


}
