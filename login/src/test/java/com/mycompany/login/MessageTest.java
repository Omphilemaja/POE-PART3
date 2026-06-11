package com.mycompany.login;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckMessageLength_Success() {
        Message sample = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight");
        assertEquals("Message ready to send", sample.validateMessageLength());
    }

    @Test
    public void testCheckMessageLength_Failure() {
        StringBuilder longMsgSb = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            longMsgSb.append("abcdefghij"); 
        }
        
        Message sample = new Message(0, "+27718693002", longMsgSb.toString());
        String result = sample.validateMessageLength();
        
        assertTrue(result.contains("Message exceeds 250 characters by 10"));
    }

    @Test
    public void testRecipientFormatting_Success() {
        Message sample = new Message(0, "+27718693002", "Valid Content");
        assertEquals("Cell phone number successfully captured.", sample.checkRecipientCell());
    }

    @Test
    public void testRecipientFormatting_Failure() {
        Message sample = new Message(0, "0718693002", "Valid Content");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", sample.checkRecipientCell());
    }

    @Test
    public void testMessageHashCreation_Verification() {
        Message sample = new Message(0, "+27718693002", "Hello World");
        
        String actualHash = sample.createMessageHash();
        String generatedPrefix = sample.getMessageID().substring(0, 2);
        
        String expectedTargetSequence = generatedPrefix + ":0:HELLOWORLD";
        assertEquals(expectedTargetSequence, actualHash);
    }

    @Test
    public void testMessageID_ConstraintBounds() {
        Message sample = new Message(0, "+27718693002", "Testing tracking keys");
        assertTrue(sample.checkMessageID());
        assertEquals(10, sample.getMessageID().length());
    }
}
