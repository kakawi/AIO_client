package com.hlebon.messageHandlers.client;

import com.hlebon.client.RouteServiceClient;
import com.hlebon.client.SenderServiceClient;
import com.hlebon.client.chat.Chat;
import com.hlebon.message.AnswerLoginMessage;
import com.hlebon.message.Message;
import javafx.application.Application;

public class AnswerLoginMessageHandlerServer implements MessageHandlerClient {
    private final SenderServiceClient senderServiceClient;
    private final RouteServiceClient routeServiceClient;
    private String myName;

    public AnswerLoginMessageHandlerServer(String myName, SenderServiceClient senderServiceClient, RouteServiceClient routeServiceClient) {
        this.senderServiceClient = senderServiceClient;
        this.routeServiceClient = routeServiceClient;
        this.myName = myName;
    }

    @Override
    public void handle(Message message) {
        if (message instanceof AnswerLoginMessage) {
            AnswerLoginMessage answerLoginMessage = (AnswerLoginMessage) message;
            Chat.senderServiceClient = senderServiceClient;
            Chat.myName = myName;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Application.launch(Chat.class);
                }
            }).start();

//            SwingControl swingControl = new SwingControl(myName, senderServiceClient);
            try {
                synchronized (Chat.waiter) {
                    Chat.waiter.wait();
                }

                routeServiceClient.setChatController(Chat.chatController);
                routeServiceClient.getChatController().connectedToChat(answerLoginMessage);
//            swingControl.connectedToChat(answerLoginMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
