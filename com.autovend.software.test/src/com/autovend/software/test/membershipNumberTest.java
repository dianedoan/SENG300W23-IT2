/**
 * @author: Abigia Debebe (30134608),
 * @author: Akib Hasan Aryan (30141456),
 * @author: Andy Tran (30125341),
 * @author: Delaram Bahreini Esfahani (30133864),
 * @author: Diane Doan (30052326),
 * @author: Faiyaz Altaf Pranto (30162576),
 * @author: Ishita Chandra (30159580),
 * @author: Nam Nguyen Vu (30154892),
 * @author: River Sanoy (30129508),
 * @author: Ryan Haoping Zheng (30072318),
 * @author: Xinzhou Li (30066080)
 */

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

