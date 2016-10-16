package com.hlebon.message;

import java.io.*;

public abstract class Message implements Serializable {
    public byte[] toByte() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(this);
        return bos.toByteArray();
    }

    public static Message toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bis);
        Object object = in.readObject();
        if (object instanceof Message) {
            return (Message) object;
        } else {
            throw new IOException("This is not Message Class");
        }
    }
}
