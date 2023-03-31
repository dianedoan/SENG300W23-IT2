package com.autovend.software.test;

import org.junit.Test;

import com.autovend.software.*;
import java.util.Scanner;
import static org.junit.Assert.*;
public class membershipNumberTest {

        @Test
        public void testParseMembershipNumberValid() {
            CustomerDisplayIO membership = new CustomerDisplayIO();
            int actual = membership.parseMembershipNumber("12345");
            assertEquals(12345, actual);
        }

        @Test
        public void testParseMembershipNumberCancel() {
            CustomerDisplayIO membership = new CustomerDisplayIO();
            int actual = membership.parseMembershipNumber("cancel");
            assertEquals(0, actual);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testParseMembershipNumberInvalid() {
            CustomerDisplayIO membership = new CustomerDisplayIO();
            membership.parseMembershipNumber("dwajdioaw");
        }

        @Test(expected = IllegalArgumentException.class)
        public void testParseMembershipNumberEmpty() {
            CustomerDisplayIO membership = new CustomerDisplayIO();
            membership.parseMembershipNumber("");
        }
    }

