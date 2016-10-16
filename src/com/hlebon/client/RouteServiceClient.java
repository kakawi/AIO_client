package com.hlebon.client;

import com.hlebon.client.chat.ChatController;
import com.hlebon.message.Message;
import com.hlebon.messageHandlers.client.MessageHandlerClient;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RouteServiceClient implements Runnable {
    private BlockingDeque<Message> receivedMessageQueue = new LinkedBlockingDeque<>();
    private final Map<Class, MessageHandlerClient> messageHandlers;
    private SwingControl swingControl;
    private ChatController chatController;

    public RouteServiceClient(Map<Class, MessageHandlerClient> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public ChatController getChatController() {
        return chatController;
    }

    public void addMessageToHandle(Message message) {
        receivedMessageQueue.add(message);
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            if(receivedMessageQueue.isEmpty()) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Message message = receivedMessageQueue.poll();

            MessageHandlerClient messageHandlerClient = messageHandlers.get(message.getClass());
            messageHandlerClient.handle(message);
        }
    }
}
