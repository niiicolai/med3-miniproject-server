package org.example.server;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Handler implements Runnable {
    
    private Socket socket;
// Added client and server messages
    private String clientMsg;
    private String serverMsg;
    
    Handler(Socket socket) {this.socket = socket; }

    
    public void run() {
        try {
            // Instanced inStream, outStream and cmdHandler
            var inStream = new DataInputStream(socket.getInputStream());
            var outStream = new DataOutputStream(socket.getOutputStream());
            var cmdHandler = new CommandHandler();
            
            //Loop for when cmdHandler is stopped
            while (!cmdHandler.isStopped()) {    
                // read client message from instream to clientMsg 
                clientMsg = inStream.readUTF();
                // read severMsg from cmDHandler process to serverMsg
                serverMsg = cmdHandler.process(clientMsg);
                // Use outStream to write server message to client
                outStream.writeUTF(serverMsg);
                // Flush
                outStream.flush();
            }
            
            // Wrap up and close operation
            inStream.close();
            outStream.close();
            socket.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
    }
}