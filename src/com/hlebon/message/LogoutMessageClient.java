package com.hlebon.message;

public class LogoutMessageClient extends Message {
    private static final long serialVersionUID = -5163339307616653063L;

    private final String nameClient;

    public LogoutMessageClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getNameClient() {
        return nameClient;
    }
}
