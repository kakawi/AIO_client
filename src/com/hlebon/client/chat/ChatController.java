package com.hlebon.client.chat;

import com.hlebon.client.SenderServiceClient;
import com.hlebon.message.AnswerLoginMessage;
import com.hlebon.message.LogoutMessageClient;
import com.hlebon.message.NewClientMessage;
import com.hlebon.message.SayMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private ListView<String> listOfClients;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea sendInput;

    @FXML
    private TextArea chatArea;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    private SenderServiceClient senderServiceClient;
    private String myName;

    public void setSenderServiceClient(SenderServiceClient senderServiceClient) {
        this.senderServiceClient = senderServiceClient;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void connectedToChat(AnswerLoginMessage answerLoginMessage) {
        addTextInChat("We're in the chat");
        ArrayList<String> existedClients = answerLoginMessage.getExistedClients();
        for (String existedClient: existedClients) {
            listOfClients.getItems().add(existedClient);
        }
    }

    @FXML
    public void push(ActionEvent event) {
        String textToSend = sendInput.getText();
        if ("".equals(textToSend)) {
            showAlert("alart", "achtung", "input the text");
            return;
        }
        String clientName = listOfClients.getSelectionModel().getSelectedItem();
        if (clientName == null) {
            showAlert("alart", "achtung", "choose client");
            return;
        }

        sendInput.setText("");
        SayMessage sayMessage = new SayMessage(myName, clientName, textToSend);
        senderServiceClient.addMessageToSend(sayMessage);
    }

    private void showAlert(String title, String header, String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public void addMessage(SayMessage sayMessage) {
        String from = sayMessage.getFrom();
        String text = sayMessage.getText();
        addTextInChat("[" + from + "] " + text);
    }

    public void addTextInChat(String text) {
        String lineSeparator = "\n";
        if ("".equals(chatArea.getText())) {
            lineSeparator = "";
        }
        Date dateNow = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss - ");
        chatArea.appendText(lineSeparator + simpleDateFormat.format(dateNow) + text);
    }

    public void addNewClient(NewClientMessage newClientMessage) {
        String newClientName = newClientMessage.getName();
        Platform.runLater(() -> listOfClients.getItems().addAll(newClientName));
    }

    public void removeClient(LogoutMessageClient logoutMessageClient) {
        String nameClient = logoutMessageClient.getNameClient();
        Platform.runLater(() -> listOfClients.getItems().remove(nameClient));
    }

}
