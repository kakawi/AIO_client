package com.hlebon.messageHandlers.client;

import com.hlebon.message.Message;

public interface MessageHandlerClient {
    void handle(Message message);
}
