package com.rusefi.io.commands;

import com.opensr5.Logger;
import com.rusefi.binaryprotocol.BinaryProtocolCommands;
import com.rusefi.binaryprotocol.IncomingDataBuffer;
import com.rusefi.config.generated.Fields;
import com.rusefi.io.IoStream;
import com.rusefi.io.tcp.BinaryProtocolServer;
import org.jetbrains.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;

import static com.rusefi.binaryprotocol.IoHelper.checkResponseCode;

public class HelloCommand implements Command {
    private final Logger logger;
    private final String tsSignature;

    public HelloCommand(Logger logger, String tsSignature) {
        this.logger = logger;
        this.tsSignature = tsSignature;
    }

    public static void send(IoStream stream, Logger logger) throws IOException {
        stream.sendPacket(new byte[]{Fields.TS_HELLO_COMMAND}, logger);
    }

    @Nullable
    public static String getHelloResponse(IncomingDataBuffer incomingData, Logger logger) throws EOFException {
        byte[] response = incomingData.getPacket(logger, "[hello]", false);
        if (!checkResponseCode(response, BinaryProtocolCommands.RESPONSE_OK))
            return null;
        return new String(response, 1, response.length - 1);
    }

    @Override
    public byte getCommand() {
        return Fields.TS_HELLO_COMMAND;
    }

    @Override
    public void handle(BinaryProtocolServer.Packet packet, IoStream stream) throws IOException {
        stream.sendPacket((BinaryProtocolServer.TS_OK + tsSignature).getBytes(), logger);
    }
}