package com.hlebon.client.chat;

import com.hlebon.client.SenderServiceClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Chat extends Application {
    public static SenderServiceClient senderServiceClient;
    public static ChatController chatController;
    public static final Object waiter = new Object();
    public static String myName;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> controllerClass) {
                if (controllerClass == ChatController.class) {
                    ChatController chatController = new ChatController();
                    chatController.setSenderServiceClient(senderServiceClient);
                    chatController.setMyName(myName);
                    return chatController;
                } else {
                    try {
                        return controllerClass.newInstance();
                    } catch (Exception exc) {
                        throw new RuntimeException(exc); // just bail
                    }
                }
            }
        });
        Parent root = fxmlLoader.load();
        chatController = fxmlLoader.getController();
        synchronized (waiter) {
            waiter.notify();
        }
        primaryStage.setTitle("Chat - " + myName);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
