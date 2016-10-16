package com.hlebon.message;

import java.io.Serializable;

public class NewClientMessage extends Message implements Serializable {
    private static final long serialVersionUID = 594657311118289324L;
    private final String name;

    public NewClientMessage(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
