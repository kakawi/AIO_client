package com.hlebon.client;

import com.hlebon.message.AnswerLoginMessage;
import com.hlebon.message.LogoutMessageClient;
import com.hlebon.message.NewClientMessage;
import com.hlebon.message.SayMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SwingControl {
    private SenderServiceClient senderServiceClient;
    private final String myName;

    private JFrame mainFrame;
    private JPanel controlPanel;
    private JTextField textField;
    private JTextArea textArea;
    private JComboBox<String> comboBox;

    private final int TEXT_AREA_ROWS = 8;
    private final int TEXT_AREA_COLUMNS = 30;

    private final int TEXT_FIELD_COLUMNS = 24;

    public SwingControl(String myName, SenderServiceClient senderServiceClient) {
        this.myName = myName;
        this.senderServiceClient = senderServiceClient;
        prepareGUI();
        showEventDemo();
    }

    public void connectedToChat(AnswerLoginMessage answerLoginMessage) {
        addInTextAread("We're in the chat");
        ArrayList<String> existedClients = answerLoginMessage.getExistedClients();
        for (String existedClient: existedClients) {
            comboBox.addItem(existedClient);
        }
    }

    private void addInTextAread(String text) {
        Date dateNow = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss - ");
        textArea.append("\n" + simpleDateFormat.format(dateNow) + text);
    }

    public void newClient(NewClientMessage newClientMessage) {
        String nameNewClient = newClientMessage.getName();
        addInTextAread("New client has come: " + nameNewClient);
        comboBox.addItem(nameNewClient);
    }

    public void logoutClient(LogoutMessageClient logoutMessageClient) {
        String nameClient = logoutMessageClient.getNameClient();
        addInTextAread("Client " + nameClient + " logout");
        comboBox.removeItem(nameClient);
    }

    public void addMessage(SayMessage sayMessage) {
        String from = sayMessage.getFrom();
        String text = sayMessage.getText();
        addInTextAread("[" + from + "] " + text);
    }

    private void sendMessage(SayMessage sayMessage) {
        senderServiceClient.addMessageToSend(sayMessage);
    }

    private void prepareGUI(){
        mainFrame = new JFrame("Chat :: " + myName);
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(3, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        comboBox = new JComboBox<>();
        topPanel.add(comboBox);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        textField = new JTextField(TEXT_FIELD_COLUMNS);
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(new ButtonClickListener());
        bottomPanel.add(textField);
        bottomPanel.add(okButton);

        mainFrame.add(topPanel);
        mainFrame.add(controlPanel);

        mainFrame.add(bottomPanel);
        mainFrame.setVisible(true);
    }

    private void showEventDemo(){
        textArea = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        controlPanel.add(scrollPane);

        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals( "OK" ))  {
                String nameDestinationClient = (String)comboBox.getSelectedItem();
                if(nameDestinationClient == null) {
                    addInTextAread("Choose the client");
                    return;
                }

                String text = textField.getText();
                if("".equals(text)) {
                    addInTextAread("Write the message");
                    return;
                }

                textField.setText("");
                SayMessage sayMessage = new SayMessage(myName, nameDestinationClient, text);
                senderServiceClient.addMessageToSend(sayMessage);
            }
        }
    }
}
