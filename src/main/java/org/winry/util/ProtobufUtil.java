package org.winry.util;

import com.google.protobuf.MessageLite;
import org.winry.proto.CommonProtos.ProtoMessage;

public class ProtobufUtil {

    public static ProtoMessage toMessage(String cmd, MessageLite message) {
        return ProtoMessage.newBuilder().setCmd(cmd).setData(message.toByteString()).build();
    }
}
