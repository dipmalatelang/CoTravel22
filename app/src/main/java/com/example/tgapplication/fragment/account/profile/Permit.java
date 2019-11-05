package com.example.tgapplication.fragment.account.profile;

public class Permit{
    String sender, receiver;
    int status;

    public Permit() {
    }

    public Permit(String sender, String receiver, int status) {
        this.sender = sender;
        this.receiver=receiver;
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
