package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;


public class PasswordEncodingTest extends BaseTestClass{

    private static final String PASSWORD_ADMIN = "urospass";
    private static final String PASSWORD = "urospass";
    private static final String PASSWORD_USER = "password";
    private static final String PASSWORD_CUSTOMER = "tiger";


    @Test
    void testBcrypt15(){
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(15);
        System.out.println(bcrypt.encode(PASSWORD_CUSTOMER));
        System.out.println(bcrypt.encode(PASSWORD_CUSTOMER));
    }
    @Test
    void testBcrypt(){
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();
        System.out.println(bcrypt.encode(PASSWORD_ADMIN));
        System.out.println(bcrypt.encode(PASSWORD_ADMIN));
    }

    @Test
    void testSHA256(){
        PasswordEncoder sha = new StandardPasswordEncoder();
        System.out.println(sha.encode(PASSWORD_USER));
        System.out.println(sha.encode(PASSWORD_USER));
    }

    @Test
    void testLdap(){
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD_CUSTOMER));
        System.out.println(ldap.encode(PASSWORD_CUSTOMER));
    }
    @Test
    void testNoop(){
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    void hashingExample(){
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes(StandardCharsets.UTF_8)));
        String salted = PASSWORD + "uros";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes(StandardCharsets.UTF_8)));

    }
}
