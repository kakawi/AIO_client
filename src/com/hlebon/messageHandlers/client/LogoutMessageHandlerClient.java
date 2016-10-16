package com.hlebon.messageHandlers.client;

import com.hlebon.client.RouteServiceClient;
import com.hlebon.client.SenderServiceClient;
import com.hlebon.message.LogoutMessageClient;
import com.hlebon.message.Message;

public class LogoutMessageHandlerClient implements MessageHandlerClient {
    private final SenderServiceClient senderServiceClient;
    private final RouteServiceClient routeServiceClient;

    public LogoutMessageHandlerClient(SenderServiceClient senderServiceClient, RouteServiceClient routeServiceClient) {
        this.senderServiceClient = senderServiceClient;
        this.routeServiceClient = routeServiceClient;
    }

    @Override
    public void handle(Message message) {
        if (message instanceof LogoutMessageClient) {
            LogoutMessageClient logoutMessageClient = (LogoutMessageClient) message;
            routeServiceClient.getChatController().removeClient(logoutMessageClient);
        }
    }
}
