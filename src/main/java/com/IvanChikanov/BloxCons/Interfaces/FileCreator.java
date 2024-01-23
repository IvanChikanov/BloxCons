package com.IvanChikanov.BloxCons.Interfaces;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface FileCreator {

    default byte[] fileCreator(String insideText) throws IOException {
        byte[] buffer;
        try(ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            stream.write(insideText.getBytes("UTF-8"));
            buffer = stream.toByteArray();
        }
        return buffer;
    }
}
