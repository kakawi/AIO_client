package com.hlebon.client;

import com.hlebon.Logger;
import com.hlebon.message.*;
import com.hlebon.messageHandlers.client.*;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;

public class AioConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {

    @Override
    public void completed(Void attachment, AsynchronousSocketChannel serverSocket) {
        SenderServiceClient senderServiceClient = new SenderServiceClient(serverSocket);
        new Thread(senderServiceClient).start();
        Logger.info("The SenderServiceClient has started");

        LoginMessage message = new LoginMessage("c", 10);

        Map<Class, MessageHandlerClient> messageHandlers = new HashMap<>();
        RouteServiceClient routeServiceClient = new RouteServiceClient(messageHandlers);

        messageHandlers.put(AnswerLoginMessage.class, new AnswerLoginMessageHandlerServer(message.getName(), senderServiceClient, routeServiceClient));
        messageHandlers.put(SayMessage.class, new SayMessageHandlerClient(senderServiceClient, routeServiceClient));
        messageHandlers.put(NewClientMessage.class, new NewClientMessageHandlerClient(senderServiceClient, routeServiceClient));
        messageHandlers.put(LogoutMessageClient.class, new LogoutMessageHandlerClient(senderServiceClient, routeServiceClient));

        new Thread(routeServiceClient).start();
        Logger.info("The RouteServiceClient has started");

        senderServiceClient.addMessageToSend(message);

        ByteBuffer clientBuffer = ByteBuffer.allocate(1024);
        serverSocket.read(clientBuffer, clientBuffer, new AioReadHandlerClient(serverSocket, routeServiceClient));
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        exc.printStackTrace();
    }
}