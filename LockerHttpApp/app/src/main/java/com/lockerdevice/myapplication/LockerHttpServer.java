package com.lockerdevice.myapplication;


import fi.iki.elonen.NanoHTTPD;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

public class LockerHttpServer extends NanoHTTPD {

    private Rs485Controller rs485Controller;

    public LockerHttpServer(int port, Rs485Controller rs485Controller) throws IOException {
        super(port);
        this.rs485Controller = rs485Controller;
        start(SOCKET_READ_TIMEOUT, false);
        Log.i("LockerHttpServer", "Server started on port " + port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Log.i("LockerHttpServer", "Request: " + uri);

        if (session.getMethod() == Method.POST && uri.startsWith("/open/")) {
            String boxIdStr = uri.replace("/open/", "");
            try {
                int boxId = Integer.parseInt(boxIdStr);
                byte[] cmd = rs485Controller.buildCommand(3, true);
                rs485Controller.sendToSerialPort(cmd);
                return newFixedLengthResponse("Box " + boxId + " opened.");
            } catch (NumberFormatException e) {
                return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid boxId");
            }
        }

        return newFixedLengthResponse("Smart Locker Server");
    }
}