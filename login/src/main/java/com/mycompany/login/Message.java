package com.mycompany.login;

import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageContent;
    private String messageHash;
    private String sendStatus;

    // Constructor method
    public Message(int messageNumber, String recipientCell, String messageContent) {
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageContent = messageContent;
        this.messageID = generateRandomMessageID();
        this.messageHash = createMessageHash();
    }

    // Generates a random 10-digit numeric ID string
    private String generateRandomMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    // Confirms if message ID is within 10 characters length bound
    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }

    // Assures phone prefix starts with +27 and length criteria rules pass
    public String checkRecipientCell() {
        if (this.recipientCell == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        
        boolean hasInternationalCode = this.recipientCell.startsWith("+27");
        // Strip out the '+' character to calculate raw numeric length
        int lengthWithoutPlus = this.recipientCell.replace("+", "").length();

        // Updated to <= 11 to correctly accommodate international standard codes (+27 followed by 9 digits)
        if (hasInternationalCode && lengthWithoutPlus <= 11) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Creates the message identification layout string signature tracking token
    public final String createMessageHash() {
        if (this.messageID == null || this.messageID.length() < 2 || this.messageContent == null || this.messageContent.trim().isEmpty()) {
            return "00:0:EMPTY";
        }

        String idPrefix = this.messageID.substring(0, 2);
        String cleanedContent = this.messageContent.trim();
        String[] words = cleanedContent.split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        String rawHash = idPrefix + ":" + this.messageNumber + ":" + firstWord + lastWord;
        return rawHash.toUpperCase();
    }

    // Evaluates length criteria requirements below 250 character ceiling limit constraints
    public String validateMessageLength() {
        if (this.messageContent == null) {
            return "Success";
        }
        
        if (this.messageContent.length() <= 250) {
            return "Message ready to send";
        } else {
            int exceededBy = this.messageContent.length() - 250;
            return "Message exceeds 250 characters by " + exceededBy + "; please reduce the size.";
        }
    }

    // Standard variable accessor hooks definitions
    public String getMessageID() { 
        return messageID; 
    }
    
    public int getMessageNumber() { 
        return messageNumber; 
    }
    
    public String getRecipientCell() { 
        return recipientCell; 
    }
    
    public String getMessageContent() { 
        return messageContent; 
    }
    
    public String getMessageHash() { 
        return messageHash; 
    }
    
    public String getSendStatus() { 
        return sendStatus; 
    }
    
    public void setSendStatus(String sendStatus) { 
        this.sendStatus = sendStatus; 
    }
}
