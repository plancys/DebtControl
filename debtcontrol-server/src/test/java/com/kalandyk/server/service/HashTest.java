package com.kalandyk.server.service;

import com.kalandyk.api.model.security.PasswordUtils;
import org.junit.Test;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import static org.junit.Assert.*;
/**
 * Created by kamil on 3/28/14.
 */
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
