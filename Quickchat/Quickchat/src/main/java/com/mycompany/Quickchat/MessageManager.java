package com.mycompany.Quickchat;

import java.util.List;

public class MessageManager {

    public String displayAllStoredMessageDetails() {
        List<Message> stored = Message.getStoredMessages();
        if (stored.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nStored Message Details\n");
        for (Message msg : stored) {
            sb.append("Recipient: ").append(msg.getRecipient()).append("\n");
            sb.append("Message: ").append(msg.getMessageText()).append("\n");
        }
        return sb.toString();
    }

    public String displayLongestMessage() {
        List<Message> sent = Message.getSentMessages();
        List<Message> stored = Message.getStoredMessages();
        List<Message> disregarded = Message.getDisregardedMessages();

        String longest = "";
        for (Message msg : sent) {
            if (msg.getMessageText() != null && msg.getMessageText().length() > longest.length()) {
                longest = msg.getMessageText();
            }
        }
        for (Message msg : stored) {
            if (msg.getMessageText() != null && msg.getMessageText().length() > longest.length()) {
                longest = msg.getMessageText();
            }
        }
        for (Message msg : disregarded) {
            if (msg.getMessageText() != null && msg.getMessageText().length() > longest.length()) {
                longest = msg.getMessageText();
            }
        }

        if (longest.isEmpty()) {
            return "No messages found.";
        }
        return longest;
    }


    public String searchByMessageID(String searchID) {
        List<Message> sent = Message.getSentMessages();
        List<Message> stored = Message.getStoredMessages();
        List<Message> disregarded = Message.getDisregardedMessages();

        for (Message msg : sent) {
            if (msg.getMessageID() != null && msg.getMessageID().equals(searchID)) {
                return msg.getMessageText();
            }
        }
        for (Message msg : stored) {
            if (msg.getMessageID() != null && msg.getMessageID().equals(searchID)) {
                return msg.getMessageText();
            }
        }
        for (Message msg : disregarded) {
            if (msg.getMessageID() != null && msg.getMessageID().equals(searchID)) {
                return msg.getMessageText();
            }
        }
        return "Message ID not found.";
    }

    public String searchByRecipient(String recipientNumber) {
        List<Message> sent = Message.getSentMessages();
        List<Message> stored = Message.getStoredMessages();

        StringBuilder sb = new StringBuilder();
        for (Message msg : sent) {
            if (msg.getRecipient() != null && msg.getRecipient().equals(recipientNumber)) {
                sb.append(msg.getMessageText()).append("\n");
            }
        }
        for (Message msg : stored) {
            if (msg.getRecipient() != null && msg.getRecipient().equals(recipientNumber)) {
                sb.append(msg.getMessageText()).append("\n");
            }
        }

        if (sb.length() == 0) {
            return "No messages found for recipient: " + recipientNumber;
        }
        return sb.toString().trim();
    }

    public String deleteMessageByHash(String hash) {
        List<Message> stored = Message.getStoredMessages();
        List<String> hashes = Message.getMessageHashes();

        for (int i = 0; i < stored.size(); i++) {
            if (stored.get(i).getMessageHash() != null && stored.get(i).getMessageHash().equals(hash)) {
                String deletedText = stored.get(i).getMessageText();
                stored.remove(i);
                hashes.remove(hash);
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }

        List<Message> sent = Message.getSentMessages();
        for (int i = 0; i < sent.size(); i++) {
            if (sent.get(i).getMessageHash() != null && sent.get(i).getMessageHash().equals(hash)) {
                String deletedText = sent.get(i).getMessageText();
                sent.remove(i);
                hashes.remove(hash);
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }

        return "Message hash not found.";
    }


    public String displayReport() {
        List<Message> sent = Message.getSentMessages();
        List<Message> stored = Message.getStoredMessages();

        if (sent.isEmpty() && stored.isEmpty()) {
            return "No messages to display in report.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\nMessage Report\n");

        sb.append("\nSent Messages\n");
        if (sent.isEmpty()) {
            sb.append("No sent messages.\n");
        } else {
            for (Message msg : sent) {
                sb.append("Message Hash: ").append(msg.getMessageHash()).append("\n");
                sb.append("Recipient: ").append(msg.getRecipient()).append("\n");
                sb.append("Message: ").append(msg.getMessageText()).append("\n");
            }
        }

        sb.append("\nStored Messages \n");
        if (stored.isEmpty()) {
            sb.append("No stored messages.\n");
        } else {
            for (Message msg : stored) {
                sb.append("Message Hash: ").append(msg.getMessageHash()).append("\n");
                sb.append("Recipient: ").append(msg.getRecipient()).append("\n");
                sb.append("Message: ").append(msg.getMessageText()).append("\n");
            }
        }

        return sb.toString();
    }
}

