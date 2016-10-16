package com.hlebon.message;

import java.nio.channels.AsynchronousSocketChannel;

public class MessageWrapper extends Message {
    private Message message;
    private AsynchronousSocketChannel from;

    public MessageWrapper(Message message, AsynchronousSocketChannel from) {
        this.message = message;
        this.from = from;
    }

    public Message getMessage() {
        return message;
    }

    public AsynchronousSocketChannel getFrom() {
        return from;
    }
}
