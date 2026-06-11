package com.mycompany.login;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // Global in-memory storage log to track sent messages across runtime loops
    private static final ArrayList<Message> sentMessagesLog = new ArrayList<>();

    // Part 3 — Additional array logs for all message state classifications
    private static final ArrayList<Message> disregardedMessagesLog = new ArrayList<>();
    private static final ArrayList<Message> storedMessagesLog      = new ArrayList<>();
    private static final ArrayList<String>  messageHashLog         = new ArrayList<>();
    private static final ArrayList<String>  messageIDLog           = new ArrayList<>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login authSystem = new Login();

        System.out.println("=== CREATE YOUR ACCOUNT ===");
        System.out.print("Simulate user sign-up setup? (yes/no): ");
        String setupChoice = input.nextLine();

        if (setupChoice.equalsIgnoreCase("yes")) {
            authSystem.registerUser("johndoe123", "P@ssword1", "+27123456789");
            System.out.println("Default login credentials created: user: johndoe123 | pass: P@ssword1");
        }

        System.out.println("\n=== AUTHENTICATION ACCESS LOG ===");
        boolean authenticated = false;

        // Loop continuously until login validation clears successfully
        while (!authenticated) {
            System.out.print("Enter Username: ");
            String user = input.nextLine();
            System.out.print("Enter Password: ");
            String pass = input.nextLine();

            authenticated = authSystem.loginUser(user, pass);
            if (!authenticated) {
                System.out.println("Access Denied. Invalid credentials. Please try again.");
            }
        }

        // Welcome banner message requirement
        System.out.println("\nWelcome to QuickChat.");

        // Capture allocation parameters safely
        int targetMessageCount = 0;
        while (true) {
            try {
                System.out.print("Please enter the total number of messages you wish to enter: ");
                targetMessageCount = Integer.parseInt(input.nextLine());
                if (targetMessageCount > 0) {
                    break;
                }
                System.out.println("Please enter a value greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter an integer.");
            }
        }

        boolean applicationRunning = true;
        int messagesProcessedCounter = 0;

        // Primary application operational loop logic
        while (applicationRunning) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");
            System.out.print("Choose an option: ");

            String menuChoice = input.nextLine();

            switch (menuChoice) {
                case "1":
                    // Check allocation constraints before letting user type inputs
                    if (messagesProcessedCounter >= targetMessageCount) {
                        System.out.println("Allocation limit reached. You cannot enter more than the set number of messages.");
                        break;
                    }

                    System.out.println("\n--- Message Entry Slot " + (messagesProcessedCounter + 1) + " of " + targetMessageCount + " ---");

                    System.out.print("Enter Recipient Cell Number (e.g., +27718693002): ");
                    String cellInput = input.nextLine();

                    System.out.print("Enter Message (Max 250 characters): ");
                    String contentInput = input.nextLine();

                    // Instantiate message class object footprint
                    Message activeMsg = new Message(messagesProcessedCounter, cellInput, contentInput);

                    String cellValidation = activeMsg.checkRecipientCell();
                    String lengthValidation = activeMsg.validateMessageLength();

                    System.out.println("Status: " + cellValidation);
                    System.out.println("Length check: " + lengthValidation);

                    // Stop operational run execution cleanly if length rules are breached
                    if (!lengthValidation.equals("Message ready to send")) {
                        System.out.println("Please enter a message of less than 250 characters.");
                        break;
                    }

                    System.out.println("Message ID generated: " + activeMsg.getMessageID());
                    System.out.println("Message Hash: " + activeMsg.getMessageHash());

                    // Choice Selection Sub-Menu processing
                    System.out.println("\nChoose an option for this message:");
                    System.out.println("1 - Send Message");
                    System.out.println("2 - Disregard Message");
                    System.out.println("3 - Store Message to send later");
                    System.out.print("Selection: ");
                    String actionChoice = input.nextLine();

                    if (actionChoice.equals("1")) {
                        activeMsg.setSendStatus("Sent Message");
                        sentMessagesLog.add(activeMsg);
                        messageHashLog.add(activeMsg.getMessageHash());
                        messageIDLog.add(activeMsg.getMessageID());
                        System.out.println("Message successfully sent.");

                        // Display message metadata receipt summary format output
                        System.out.println("\n--- Message Summary Receipt ---");
                        System.out.println(activeMsg.getMessageID() + ", " +
                                           activeMsg.getMessageHash() + ", " +
                                           activeMsg.getRecipientCell() + ", " +
                                           activeMsg.getMessageContent());

                    } else if (actionChoice.equals("2")) {
                        activeMsg.setSendStatus("Disregarded");
                        disregardedMessagesLog.add(activeMsg);
                        messageHashLog.add(activeMsg.getMessageHash());
                        messageIDLog.add(activeMsg.getMessageID());
                        System.out.println("Press 0 to delete the message.");
                    } else if (actionChoice.equals("3")) {
                        activeMsg.setSendStatus("Stored");
                        storedMessagesLog.add(activeMsg);
                        messageHashLog.add(activeMsg.getMessageHash());
                        messageIDLog.add(activeMsg.getMessageID());
                        System.out.println("Message successfully stored.");
                        storeMessageInJson(activeMsg);
                    } else {
                        System.out.println("Invalid selection. Message skipped.");
                    }

                    // Increment loop counter tracking variables
                    messagesProcessedCounter++;
                    break;

                case "2":
                    // Display all messages sent during this session
                    if (sentMessagesLog.isEmpty()) {
                        System.out.println("No messages have been sent during this session.");
                    } else {
                        System.out.println("\n--- Recently Sent Messages ---");
                        for (Message msg : sentMessagesLog) {
                            System.out.println(msg.getMessageID() + ", " +
                                               msg.getMessageHash() + ", " +
                                               msg.getRecipientCell() + ", " +
                                               msg.getMessageContent());
                        }
                    }
                    break;

                case "3":
                    // Part 3 — Stored Messages sub-menu for full array operations
                    handleStoredMessagesMenu(input);
                    break;

                case "4":
                    applicationRunning = false;
                    System.out.println("Exiting application.");
                    System.out.println("Total messages sent successfully during session: " + returnTotalMessages());
                    break;

                default:
                    System.out.println("Invalid option selection. Please enter 1, 2, 3, or 4.");
                    break;
            }
        }
        input.close();
    }

    public static int returnTotalMessages() {
        return sentMessagesLog.size();
    }

    private static void storeMessageInJson(Message msg) {
        System.out.println("\n--- Storing Message to JSON File ---");
        String jsonOutput = "{\n"
                + "  \"messageID\": \"\"" + msg.getMessageID() + "\"\",\n"
                + "  \"messageHash\": \"\"" + msg.getMessageHash() + "\"\",\n"
                + "  \"recipient\": \"\"" + msg.getRecipientCell() + "\"\",\n"
                + "  \"message\": \"\"" + msg.getMessageContent() + "\"\"\n"
                + "}";
        System.out.println(jsonOutput);
        System.out.println("Message successfully stored in JSON.");
    }

    // =========================================================================
    // Part 3 — STORED MESSAGES MENU: Sub-menu for all array management operations
    // =========================================================================
    public static void handleStoredMessagesMenu(Scanner input) {
        boolean storedMenuRunning = true;

        while (storedMenuRunning) {
            System.out.println("\n=== STORED MESSAGES MENU ===");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message by ID");
            System.out.println("d) Search all messages for a particular recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display full message report");
            System.out.println("0) Return to Main Menu");
            System.out.print("Choose an option: ");

            String storedChoice = input.nextLine().trim().toLowerCase();

            switch (storedChoice) {

                case "a":
                    displayAllStoredSenderRecipient();
                    break;

                case "b":
                    displayLongestStoredMessage();
                    break;

                case "c":
                    System.out.print("Enter Message ID to search: ");
                    String searchID = input.nextLine().trim();
                    searchMessageByID(searchID);
                    break;

                case "d":
                    System.out.print("Enter recipient cell number to search: ");
                    String searchRecipient = input.nextLine().trim();
                    searchMessagesByRecipient(searchRecipient);
                    break;

                case "e":
                    System.out.print("Enter message hash to delete: ");
                    String deleteHash = input.nextLine().trim();
                    deleteMessageByHash(deleteHash);
                    break;

                case "f":
                    displayFullMessageReport();
                    break;

                case "0":
                    storedMenuRunning = false;
                    System.out.println("Returning to Main Menu.");
                    break;

                default:
                    System.out.println("Invalid option. Please enter a valid choice (a-f or 0).");
                    break;
            }
        }
    }

    // =========================================================================
    // Part 3 — DISPLAY: Sender and recipient of all stored messages
    // =========================================================================
    public static void displayAllStoredSenderRecipient() {
        // Combine sent and stored arrays to display full cross-status message list
        ArrayList<Message> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(sentMessagesLog);
        combinedMessages.addAll(storedMessagesLog);

        if (combinedMessages.isEmpty()) {
            System.out.println("No messages found across sent and stored logs.");
            return;
        }

        System.out.println("\n--- Sender and Recipient Details for All Messages ---");
        for (Message msg : combinedMessages) {
            System.out.println("Message #" + msg.getMessageNumber() +
                               " | Recipient: " + msg.getRecipientCell() +
                               " | Status: " + msg.getSendStatus() +
                               " | Message: " + msg.getMessageContent());
        }
    }

    // =========================================================================
    // Part 3 — DISPLAY: Find and output the single longest message by character count
    // =========================================================================
    public static String displayLongestStoredMessage() {
        // Search both sent and stored arrays for the globally longest message content
        ArrayList<Message> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(sentMessagesLog);
        combinedMessages.addAll(storedMessagesLog);

        if (combinedMessages.isEmpty()) {
            System.out.println("No messages available to compare.");
            return "";
        }

        Message longestMessage = combinedMessages.get(0);

        for (Message msg : combinedMessages) {
            if (msg.getMessageContent().length() > longestMessage.getMessageContent().length()) {
                longestMessage = msg;
            }
        }

        System.out.println("\n--- Longest Message ---");
        System.out.println("Recipient: " + longestMessage.getRecipientCell());
        System.out.println("Message: " + longestMessage.getMessageContent());
        System.out.println("Character Count: " + longestMessage.getMessageContent().length());

        return longestMessage.getMessageContent();
    }

    // =========================================================================
    // Part 3 — SEARCH: Locate a message by its unique generated ID
    // =========================================================================
    public static String searchMessageByID(String targetID) {
        // Traverse both sent and stored message arrays to locate any matching ID
        ArrayList<Message> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(sentMessagesLog);
        combinedMessages.addAll(storedMessagesLog);

        for (Message msg : combinedMessages) {
            if (msg.getMessageID().equals(targetID)) {
                System.out.println("\n--- Message Found ---");
                System.out.println("Recipient: " + msg.getRecipientCell());
                System.out.println("Message: " + msg.getMessageContent());
                return msg.getMessageContent();
            }
        }

        System.out.println("No message found with ID: " + targetID);
        return "Message not found";
    }

    // =========================================================================
    // Part 3 — SEARCH: Retrieve all messages for a particular recipient number
    // =========================================================================
    public static ArrayList<String> searchMessagesByRecipient(String recipientCell) {
        ArrayList<String> matchedMessages = new ArrayList<>();

        // Cross-search both sent and stored arrays for any matching recipient cell
        ArrayList<Message> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(sentMessagesLog);
        combinedMessages.addAll(storedMessagesLog);

        System.out.println("\n--- Messages for Recipient: " + recipientCell + " ---");

        for (Message msg : combinedMessages) {
            if (msg.getRecipientCell().equals(recipientCell)) {
                System.out.println("Message: " + msg.getMessageContent() +
                                   " | Status: " + msg.getSendStatus());
                matchedMessages.add(msg.getMessageContent());
            }
        }

        if (matchedMessages.isEmpty()) {
            System.out.println("No messages found for recipient: " + recipientCell);
        }

        return matchedMessages;
    }

    // =========================================================================
    // Part 3 — DELETE: Remove a message from storage using its hash identifier
    // =========================================================================
    public static boolean deleteMessageByHash(String targetHash) {
        // Check sent messages array first for matching hash entry
        for (int i = 0; i < sentMessagesLog.size(); i++) {
            if (sentMessagesLog.get(i).getMessageHash().equals(targetHash)) {
                String deletedContent = sentMessagesLog.get(i).getMessageContent();
                sentMessagesLog.remove(i);
                messageHashLog.remove(targetHash);
                System.out.println("Message: \"" + deletedContent + "\" successfully deleted.");
                return true;
            }
        }

        // Check stored messages array if not found in sent messages array
        for (int i = 0; i < storedMessagesLog.size(); i++) {
            if (storedMessagesLog.get(i).getMessageHash().equals(targetHash)) {
                String deletedContent = storedMessagesLog.get(i).getMessageContent();
                storedMessagesLog.remove(i);
                messageHashLog.remove(targetHash);
                System.out.println("Message: \"" + deletedContent + "\" successfully deleted.");
                return true;
            }
        }

        System.out.println("No message found with hash: " + targetHash);
        return false;
    }

    // =========================================================================
    // Part 3 — REPORT: Full details for all messages across all status arrays
    // =========================================================================
    public static void displayFullMessageReport() {
        System.out.println("\n========================================");
        System.out.println("         FULL MESSAGE REPORT            ");
        System.out.println("========================================");

        // Section 1: Print all messages with Sent status
        System.out.println("\n--- SENT MESSAGES (" + sentMessagesLog.size() + ") ---");
        if (sentMessagesLog.isEmpty()) {
            System.out.println("No sent messages recorded.");
        } else {
            for (Message msg : sentMessagesLog) {
                printSingleMessageReportEntry(msg);
            }
        }

        // Section 2: Print all messages with Stored status
        System.out.println("\n--- STORED MESSAGES (" + storedMessagesLog.size() + ") ---");
        if (storedMessagesLog.isEmpty()) {
            System.out.println("No stored messages recorded.");
        } else {
            for (Message msg : storedMessagesLog) {
                printSingleMessageReportEntry(msg);
            }
        }

        // Section 3: Print all messages with Disregarded status
        System.out.println("\n--- DISREGARDED MESSAGES (" + disregardedMessagesLog.size() + ") ---");
        if (disregardedMessagesLog.isEmpty()) {
            System.out.println("No disregarded messages recorded.");
        } else {
            for (Message msg : disregardedMessagesLog) {
                printSingleMessageReportEntry(msg);
            }
        }

        System.out.println("\n========================================");
        System.out.println("Total Messages Sent:        " + sentMessagesLog.size());
        System.out.println("Total Messages Stored:      " + storedMessagesLog.size());
        System.out.println("Total Messages Disregarded: " + disregardedMessagesLog.size());
        System.out.println("========================================");
    }

    // =========================================================================
    // HELPER: Formats a single message entry in a consistent report layout block
    // =========================================================================
    private static void printSingleMessageReportEntry(Message msg) {
        System.out.println("  Message Hash : " + msg.getMessageHash());
        System.out.println("  Recipient    : " + msg.getRecipientCell());
        System.out.println("  Message      : " + msg.getMessageContent());
        System.out.println("  Status       : " + msg.getSendStatus());
        System.out.println("  ---");
    }

    // =========================================================================
    // TEST ACCESSOR HOOKS — Expose array state for unit test validation purposes
    // =========================================================================
    public static ArrayList<Message> getSentMessagesLog() {
        return sentMessagesLog;
    }

    public static ArrayList<Message> getStoredMessagesLog() {
        return storedMessagesLog;
    }

    public static ArrayList<Message> getDisregardedMessagesLog() {
        return disregardedMessagesLog;
    }

    public static ArrayList<String> getMessageHashLog() {
        return messageHashLog;
    }

    public static ArrayList<String> getMessageIDLog() {
        return messageIDLog;
    }

    // =========================================================================
    // TEST HELPER: Clear all runtime arrays for isolated test environments
    // =========================================================================
    public static void clearAllLogs() {
        sentMessagesLog.clear();
        storedMessagesLog.clear();
        disregardedMessagesLog.clear();
        messageHashLog.clear();
        messageIDLog.clear();
    }
}
