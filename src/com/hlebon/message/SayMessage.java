package com.hlebon.message;

public class SayMessage extends Message {
    private static final long serialVersionUID = -1522117679723983086L;

    private final String from;
    private final String to;
    private final String text;

    public SayMessage(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }
}
