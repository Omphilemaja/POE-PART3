package com.mycompany.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

/**
 * TaskTest — Part 3 unit tests: Store Data and Display Task.
 *
 * All test data sourced directly from the Part 3 specification document.
 * Tests cover: array population, longest message, search by ID,
 * search by recipient, delete by hash, and full report display.
 */
public class MainTest {

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    // =========================================================================
    // TEST FIXTURE SETUP — Populate arrays with prescribed Part 3 test data
    // =========================================================================

    @BeforeEach
    public void setUp() {
        // Clear all runtime logs before each test to guarantee full isolation
        Main.clearAllLogs();

        // =====================================================================
        // Test Data Message 1 — Recipient: +27834557896 | Flag: Sent
        // =====================================================================
        Message testMsg1 = new Message(0, "+27834557896", "Did you get the cake?");
        testMsg1.setSendStatus("Sent Message");
        Main.getSentMessagesLog().add(testMsg1);
        Main.getMessageHashLog().add(testMsg1.getMessageHash());
        Main.getMessageIDLog().add(testMsg1.getMessageID());

        // =====================================================================
        // Test Data Message 2 — Recipient: +27838884567 | Flag: Stored
        // =====================================================================
        Message testMsg2 = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        testMsg2.setSendStatus("Stored");
        Main.getStoredMessagesLog().add(testMsg2);
        Main.getMessageHashLog().add(testMsg2.getMessageHash());
        Main.getMessageIDLog().add(testMsg2.getMessageID());

        // =====================================================================
        // Test Data Message 3 — Recipient: +27834484567 | Flag: Disregard
        // =====================================================================
        Message testMsg3 = new Message(2, "+27834484567", "Yohoooo, I am at your gate.");
        testMsg3.setSendStatus("Disregarded");
        Main.getDisregardedMessagesLog().add(testMsg3);
        Main.getMessageHashLog().add(testMsg3.getMessageHash());
        Main.getMessageIDLog().add(testMsg3.getMessageID());

        // =====================================================================
        // Test Data Message 4 — Developer: 0838884567 | Flag: Sent
        // =====================================================================
        Message testMsg4 = new Message(3, "0838884567", "It is dinner time !");
        testMsg4.setSendStatus("Sent Message");
        Main.getSentMessagesLog().add(testMsg4);
        Main.getMessageHashLog().add(testMsg4.getMessageHash());
        Main.getMessageIDLog().add(testMsg4.getMessageID());

        // =====================================================================
        // Test Data Message 5 — Recipient: +27838884567 | Flag: Stored
        // =====================================================================
        Message testMsg5 = new Message(4, "+27838884567", "Ok, I am leaving without you.");
        testMsg5.setSendStatus("Stored");
        Main.getStoredMessagesLog().add(testMsg5);
        Main.getMessageHashLog().add(testMsg5.getMessageHash());
        Main.getMessageIDLog().add(testMsg5.getMessageID());
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    // =========================================================================
    // 1. ARRAY POPULATION TESTS — Verify arrays hold the correct test data
    // =========================================================================

    @Test
    public void testSentMessagesArray_CorrectlyPopulated() {
        // Sent messages array must contain messages 1 and 4 from the spec test data
        ArrayList<Message> sentLog = Main.getSentMessagesLog();

        assertEquals(2, sentLog.size(), "Sent messages log must contain exactly 2 entries.");
        assertEquals("Did you get the cake?", sentLog.get(0).getMessageContent());
        assertEquals("It is dinner time !", sentLog.get(1).getMessageContent());
    }

    @Test
    public void testStoredMessagesArray_CorrectlyPopulated() {
        // Stored messages array must contain messages 2 and 5 from the spec test data
        ArrayList<Message> storedLog = Main.getStoredMessagesLog();

        assertEquals(2, storedLog.size(), "Stored messages log must contain exactly 2 entries.");
        assertEquals("Where are you? You are late! I have asked you to be on time.", storedLog.get(0).getMessageContent());
        assertEquals("Ok, I am leaving without you.", storedLog.get(1).getMessageContent());
    }

    @Test
    public void testDisregardedMessagesArray_CorrectlyPopulated() {
        // Disregarded messages array must contain only message 3 from the spec test data
        ArrayList<Message> disregardedLog = Main.getDisregardedMessagesLog();

        assertEquals(1, disregardedLog.size(), "Disregarded messages log must contain exactly 1 entry.");
        assertEquals("Yohoooo, I am at your gate.", disregardedLog.get(0).getMessageContent());
    }

    @Test
    public void testMessageHashArray_CorrectlyPopulated() {
        // Hash log must contain one entry per message — 5 total across all flags
        ArrayList<String> hashLog = Main.getMessageHashLog();

        assertEquals(5, hashLog.size(), "Message hash log must track all 5 messages.");
        for (String hash : hashLog) {
            assertNotNull(hash, "Hash entry must not be null.");
            assertFalse(hash.isEmpty(), "Hash entry must not be empty.");
        }
    }

    @Test
    public void testMessageIDArray_CorrectlyPopulated() {
        // ID log must contain one entry per message — 5 total across all flags
        ArrayList<String> idLog = Main.getMessageIDLog();

        assertEquals(5, idLog.size(), "Message ID log must track all 5 messages.");
        for (String id : idLog) {
            assertNotNull(id);
            assertEquals(10, id.length(), "Every message ID must be exactly 10 digits long.");
        }
    }

    // =========================================================================
    // 2. DISPLAY LONGEST MESSAGE — Spec: messages 1-4, longest is message 2
    // =========================================================================

    @Test
    public void testDisplayLongestMessage_ReturnsCorrectContent() {
        // Among messages 1-4, message 2 has the longest content by character count
        String longestResult = Main.displayLongestStoredMessage();

        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            longestResult,
            "The longest message must match the prescribed test data content for message 2."
        );
    }

    @Test
    public void testDisplayLongestMessage_IsActuallyLongest() {
        // Confirm no other message in the combined arrays exceeds the returned result
        String longestResult = Main.displayLongestStoredMessage();

        ArrayList<Message> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(Main.getSentMessagesLog());
        combinedMessages.addAll(Main.getStoredMessagesLog());

        for (Message msg : combinedMessages) {
            assertTrue(
                msg.getMessageContent().length() <= longestResult.length(),
                "No message should be longer than the identified longest message."
            );
        }
    }

    // =========================================================================
    // 3. SEARCH BY MESSAGE ID — Spec: message 4 → "It is dinner time !"
    // =========================================================================

    @Test
    public void testSearchMessageByID_ReturnsCorrectMessage() {
        // Retrieve the actual generated ID of test message 4 to drive the search call
        String msg4ID = Main.getSentMessagesLog().get(1).getMessageID();

        String searchResult = Main.searchMessageByID(msg4ID);

        assertEquals(
            "It is dinner time !",
            searchResult,
            "Searching by message 4's ID must return its exact message content."
        );
    }

    @Test
    public void testSearchMessageByID_NotFound_ReturnsNotFoundString() {
        // Searching for a non-existent ID must return the not-found indicator string
        String searchResult = Main.searchMessageByID("9999999999");

        assertEquals("Message not found", searchResult);
    }

    // =========================================================================
    // 4. SEARCH BY RECIPIENT — Spec: +27838884567 → messages 2 and 5
    // =========================================================================

    @Test
    public void testSearchMessagesByRecipient_ReturnsCorrectMessages() {
        // Recipient +27838884567 appears in message 2 (Stored) and message 5 (Stored)
        ArrayList<String> results = Main.searchMessagesByRecipient("+27838884567");

        assertEquals(2, results.size(), "Must return exactly 2 messages for recipient +27838884567.");
        assertTrue(results.contains("Where are you? You are late! I have asked you to be on time."),
            "Result set must include the message 2 content.");
        assertTrue(results.contains("Ok, I am leaving without you."),
            "Result set must include the message 5 content.");
    }

    @Test
    public void testSearchMessagesByRecipient_NoMatch_ReturnsEmptyList() {
        // Searching for a recipient that does not exist must return an empty list
        ArrayList<String> results = Main.searchMessagesByRecipient("+27000000000");

        assertTrue(results.isEmpty(), "Search for unknown recipient must return an empty list.");
    }

    // =========================================================================
    // 5. DELETE BY HASH — Spec: Test Message 2 deleted by its hash
    // =========================================================================

    @Test
    public void testDeleteMessageByHash_SuccessfulDeletion() {
        // Retrieve the generated hash of test message 2 to use in the deletion call
        String msg2Hash = Main.getStoredMessagesLog().get(0).getMessageHash();
        int storedCountBefore = Main.getStoredMessagesLog().size();

        boolean deletionResult = Main.deleteMessageByHash(msg2Hash);

        assertTrue(deletionResult, "Deletion must return true when a matching hash is found.");
        assertEquals(
            storedCountBefore - 1,
            Main.getStoredMessagesLog().size(),
            "Stored messages array must shrink by 1 after successful deletion."
        );
    }

    @Test
    public void testDeleteMessageByHash_MessageContentVerification() {
        // Confirm message 2 content is no longer present in stored log after deletion
        String msg2Hash = Main.getStoredMessagesLog().get(0).getMessageHash();

        Main.deleteMessageByHash(msg2Hash);

        for (Message msg : Main.getStoredMessagesLog()) {
            assertNotEquals(
                "Where are you? You are late! I have asked you to be on time.",
                msg.getMessageContent(),
                "Message 2 content must not remain in the stored log after deletion."
            );
        }
    }

    @Test
    public void testDeleteMessageByHash_HashNotFound_ReturnsFalse() {
        // Attempting deletion with a non-existent hash must return false cleanly
        boolean deletionResult = Main.deleteMessageByHash("FAKEHASH999");

        assertFalse(deletionResult, "Deletion must return false when hash is not found.");
    }

    // =========================================================================
    // 6. DISPLAY REPORT — Verify report executes without errors across all arrays
    // =========================================================================

    @Test
    public void testDisplayFullMessageReport_DoesNotThrow() {
        // Report generation must complete without throwing any runtime exceptions
        assertDoesNotThrow(() -> Main.displayFullMessageReport(),
            "Full message report display must execute without any runtime errors.");
    }

    @Test
    public void testReturnTotalMessages_MatchesSentLogSize() {
        // returnTotalMessages() must equal the actual sent messages array size exactly
        assertEquals(
            Main.getSentMessagesLog().size(),
            Main.returnTotalMessages(),
            "returnTotalMessages() must equal the actual sent messages log size."
        );
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Main.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of returnTotalMessages method, of class Main.
     */
    @Test
    public void testReturnTotalMessages() {
        System.out.println("returnTotalMessages");
        int expResult = 0;
        int result = Main.returnTotalMessages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleStoredMessagesMenu method, of class Main.
     */
    @Test
    public void testHandleStoredMessagesMenu() {
        System.out.println("handleStoredMessagesMenu");
        Scanner input = null;
        Main.handleStoredMessagesMenu(input);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayAllStoredSenderRecipient method, of class Main.
     */
    @Test
    public void testDisplayAllStoredSenderRecipient() {
        System.out.println("displayAllStoredSenderRecipient");
        Main.displayAllStoredSenderRecipient();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayLongestStoredMessage method, of class Main.
     */
    @Test
    public void testDisplayLongestStoredMessage() {
        System.out.println("displayLongestStoredMessage");
        String expResult = "";
        String result = Main.displayLongestStoredMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchMessageByID method, of class Main.
     */
    @Test
    public void testSearchMessageByID() {
        System.out.println("searchMessageByID");
        String targetID = "";
        String expResult = "";
        String result = Main.searchMessageByID(targetID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchMessagesByRecipient method, of class Main.
     */
    @Test
    public void testSearchMessagesByRecipient() {
        System.out.println("searchMessagesByRecipient");
        String recipientCell = "";
        ArrayList<String> expResult = null;
        ArrayList<String> result = Main.searchMessagesByRecipient(recipientCell);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteMessageByHash method, of class Main.
     */
    @Test
    public void testDeleteMessageByHash() {
        System.out.println("deleteMessageByHash");
        String targetHash = "";
        boolean expResult = false;
        boolean result = Main.deleteMessageByHash(targetHash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayFullMessageReport method, of class Main.
     */
    @Test
    public void testDisplayFullMessageReport() {
        System.out.println("displayFullMessageReport");
        Main.displayFullMessageReport();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSentMessagesLog method, of class Main.
     */
    @Test
    public void testGetSentMessagesLog() {
        System.out.println("getSentMessagesLog");
        ArrayList<Message> expResult = null;
        ArrayList<Message> result = Main.getSentMessagesLog();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStoredMessagesLog method, of class Main.
     */
    @Test
    public void testGetStoredMessagesLog() {
        System.out.println("getStoredMessagesLog");
        ArrayList<Message> expResult = null;
        ArrayList<Message> result = Main.getStoredMessagesLog();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisregardedMessagesLog method, of class Main.
     */
    @Test
    public void testGetDisregardedMessagesLog() {
        System.out.println("getDisregardedMessagesLog");
        ArrayList<Message> expResult = null;
        ArrayList<Message> result = Main.getDisregardedMessagesLog();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageHashLog method, of class Main.
     */
    @Test
    public void testGetMessageHashLog() {
        System.out.println("getMessageHashLog");
        ArrayList<String> expResult = null;
        ArrayList<String> result = Main.getMessageHashLog();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageIDLog method, of class Main.
     */
    @Test
    public void testGetMessageIDLog() {
        System.out.println("getMessageIDLog");
        ArrayList<String> expResult = null;
        ArrayList<String> result = Main.getMessageIDLog();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearAllLogs method, of class Main.
     */
    @Test
    public void testClearAllLogs() {
        System.out.println("clearAllLogs");
        Main.clearAllLogs();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
