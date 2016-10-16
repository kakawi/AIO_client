package com.hlebon.messageHandlers.client;

import com.hlebon.client.RouteServiceClient;
import com.hlebon.client.SenderServiceClient;
import com.hlebon.message.Message;
import com.hlebon.message.SayMessage;

public class SayMessageHandlerClient implements MessageHandlerClient {
    private final SenderServiceClient senderServiceClient;
    private final RouteServiceClient routeServiceClient;

    public SayMessageHandlerClient(SenderServiceClient senderServiceClient, RouteServiceClient routeServiceClient) {
        this.senderServiceClient = senderServiceClient;
        this.routeServiceClient = routeServiceClient;
    }

    @Override
    public void handle(Message message) {
        if (message instanceof SayMessage) {
            SayMessage sayMessage = (SayMessage) message;
            routeServiceClient.getChatController().addMessage(sayMessage);
        }
    }
}
