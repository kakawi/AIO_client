package com.hlebon.message;

import java.io.Serializable;
import java.util.ArrayList;

public class AnswerLoginMessage extends Message implements Serializable {
    private static final long serialVersionUID = 594657340918289324L;
    private final ArrayList<String> existedClients;

    public AnswerLoginMessage(ArrayList<String> existedClients) {
        this.existedClients = existedClients;
    }

    public ArrayList<String> getExistedClients() {

        return existedClients;
    }
}
