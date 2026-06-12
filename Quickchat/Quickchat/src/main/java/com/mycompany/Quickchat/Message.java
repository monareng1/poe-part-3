package com.mycompany.Quickchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String messageStatus;

    private static int totalMessagesSent = 0;
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIDs = new ArrayList<>();

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.messageStatus = "PENDING";
    }

    public Message() {
        this.messageID = "";
        this.messageNumber = 0;
        this.recipient = "";
        this.messageText = "";
        this.messageHash = "";
        this.messageStatus = "PENDING";
    }

    private String generateMessageID() {
        Random random = new Random();
        long id = (long)(random.nextDouble() * 9000000000L) + 1000000000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        if (messageID == null) {
            return false;
        }
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        String regex = "^\\+[0-9]{10,12}$";
        if (recipient.matches(regex)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String createMessageHash() {
        if (messageID == null || messageID.isEmpty() || messageText == null || messageText.trim().isEmpty()) {
            return "";
        }
        String firstTwoDigits = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        String hash = firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    public String checkMessageLength(String text) {
        if (text == null) {
            return "Message exceeds 250 characters by an unknown amount; please reduce the size.";
        }
        if (text.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = text.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                messageStatus = "SENT";
                sentMessages.add(this);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                messageStatus = "DISREGARDED";
                disregardedMessages.add(this);
                return "Press 0 to delete the message.";
            case 3:
                messageStatus = "STORED";
                storedMessages.add(this);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    public void storeMessage() {
        try {
            java.io.File file = new java.io.File("stored_messages.json");
            StringBuilder jsonContent = new StringBuilder();

            if (file.exists() && file.length() > 0) {
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                String line;
                StringBuilder existing = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    existing.append(line);
                }
                reader.close();

                String existingStr = existing.toString().trim();
                if (existingStr.startsWith("[") && existingStr.endsWith("]")) {
                    jsonContent.append(existingStr, 0, existingStr.length() - 1);
                    jsonContent.append(",");
                } else {
                    jsonContent.append("[");
                }
            } else {
                jsonContent.append("[");
            }

            jsonContent.append("{");
            jsonContent.append("\"messageID\":\"").append(messageID).append("\",");
            jsonContent.append("\"messageNumber\":").append(messageNumber).append(",");
            jsonContent.append("\"recipient\":\"").append(recipient).append("\",");
            jsonContent.append("\"messageText\":\"").append(messageText.replace("\"", "\\\"")).append("\",");
            jsonContent.append("\"messageHash\":\"").append(messageHash).append("\",");
            jsonContent.append("\"messageStatus\":\"").append(messageStatus).append("\"");
            jsonContent.append("}]");

            java.io.FileWriter writer = new java.io.FileWriter(file, false);
            writer.write(jsonContent.toString());
            writer.close();
        } catch (java.io.IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nSent Messages\n");
        for (Message msg : sentMessages) {
            sb.append("Message ID: ").append(msg.getMessageID()).append("\n");
            sb.append("Message Hash: ").append(msg.getMessageHash()).append("\n");
            sb.append("Recipient: ").append(msg.getRecipient()).append("\n");
            sb.append("Message: ").append(msg.getMessageText()).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public static void loadStoredMessagesFromJSON() {
        try {
            java.io.File file = new java.io.File("stored_messages.json");
            if (!file.exists() || file.length() == 0) {
                return;
            }
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            String json = content.toString().trim();
            if (json.isEmpty() || json.equals("[]")) {
                return;
            }

            json = json.substring(1, json.length() - 1);
            String[] entries = json.split("\\},\\{");

            for (String entry : entries) {
                entry = entry.replace("{", "").replace("}", "");
                String[] fields = entry.split(",(?=\")");
                Message msg = new Message();
                for (String field : fields) {
                    String[] kv = field.split(":", 2);
                    if (kv.length == 2) {
                        String key = kv[0].replace("\"", "").trim();
                        String value = kv[1].replace("\"", "").trim();
                        switch (key) {
                            case "messageID": msg.setMessageID(value); break;
                            case "messageNumber": msg.setMessageNumber(Integer.parseInt(value)); break;
                            case "recipient": msg.setRecipient(value); break;
                            case "messageText": msg.setMessageText(value); break;
                            case "messageHash": msg.setMessageHash(value); break;
                            case "messageStatus": msg.setMessageStatus(value); break;
                        }
                    }
                }
                storedMessages.add(msg);
                if (msg.getMessageID() != null && !msg.getMessageID().isEmpty()) {
                    messageIDs.add(msg.getMessageID());
                }
                if (msg.getMessageHash() != null && !msg.getMessageHash().isEmpty()) {
                    messageHashes.add(msg.getMessageHash());
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading stored messages: " + e.getMessage());
        }
    }

    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIDs() { return messageIDs; }

    public static void addToSentMessages(Message msg) {
        sentMessages.add(msg);
        messageHashes.add(msg.getMessageHash());
        messageIDs.add(msg.getMessageID());
        totalMessagesSent++;
    }

    public static void addToStoredMessages(Message msg) {
        storedMessages.add(msg);
        messageHashes.add(msg.getMessageHash());
        messageIDs.add(msg.getMessageID());
    }

    public static void addToDisregardedMessages(Message msg) {
        disregardedMessages.add(msg);
    }

    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessagesSent = 0;
    }

    public String getMessageID() { return messageID; }
    public void setMessageID(String messageID) { this.messageID = messageID; }

    public int getMessageNumber() { return messageNumber; }
    public void setMessageNumber(int messageNumber) { this.messageNumber = messageNumber; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public String getMessageHash() { return messageHash; }
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }

    public String getMessageStatus() { return messageStatus; }
    public void setMessageStatus(String messageStatus) { this.messageStatus = messageStatus; }
}
