package org.winry.pojo;

import com.google.protobuf.MessageLite;

public class MyMessage {

    private final String cmd;

    private final byte[] bytes;

    public MyMessage(String cmd, MessageLite messageLite) {
        this(cmd, messageLite.toByteArray());
    }

    public MyMessage(String cmd, byte[] bytes) {
        this.cmd = cmd;
        this.bytes = bytes;
    }

    public String getCmd() {
        return cmd;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
