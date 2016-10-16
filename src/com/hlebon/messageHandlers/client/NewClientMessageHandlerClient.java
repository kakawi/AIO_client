package com.hlebon.messageHandlers.client;

import com.hlebon.client.RouteServiceClient;
import com.hlebon.client.SenderServiceClient;
import com.hlebon.message.Message;
import com.hlebon.message.NewClientMessage;

public class NewClientMessageHandlerClient implements MessageHandlerClient {
    private final SenderServiceClient senderServiceClient;
    private final RouteServiceClient routeServiceClient;

    public NewClientMessageHandlerClient(SenderServiceClient senderServiceClient, RouteServiceClient routeServiceClient) {
        this.senderServiceClient = senderServiceClient;
        this.routeServiceClient = routeServiceClient;
    }

    @Override
    public void handle(Message message) {
        if (message instanceof NewClientMessage) {
            NewClientMessage newClientMessage = (NewClientMessage) message;
            routeServiceClient.getChatController().addNewClient(newClientMessage);
        }
    }
}
