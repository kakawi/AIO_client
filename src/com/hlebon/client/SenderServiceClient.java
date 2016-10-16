package com.hlebon.client;

import com.hlebon.Constance;
import com.hlebon.Logger;
import com.hlebon.message.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

public class SenderServiceClient implements Runnable {
    private BlockingDeque<Message> queue = new LinkedBlockingDeque<>();
    private final AsynchronousSocketChannel serverSocket;

    public SenderServiceClient(AsynchronousSocketChannel serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void addMessageToSend(Message message) {
        queue.add(message);
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if (queue.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            Message message = queue.poll();

            try {
                byte[] objectInByte = message.toByte();
                int length = objectInByte.length;

                ByteBuffer byteBuffer = ByteBuffer.allocate(length + 1);
                byteBuffer.put(objectInByte);
                byteBuffer.put(Constance.OBJECT_DELIMITER);
                byteBuffer.rewind();

                Logger.info("Send message to Server: " + message);

                do {
                    Future<Integer> future = serverSocket.write(byteBuffer);
                    future.get();
                } while (byteBuffer.position() < byteBuffer.limit());
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
