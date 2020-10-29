package org.example.server;

import java.net.Socket;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Handler implements Runnable {
    
    private Socket socket;
// Added client and server messages
    private String clientMsg;
    private String serverMsg;
    private PrintWriter out;
    private int id;
    
   

    private Lobby lobby = null;
    
    public Handler(int id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;  
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    
    public void run() {
        try {
            // Instanced inStream, outStream and cmdHandler
            var inStream = new DataInputStream(socket.getInputStream());
           
           
            var cmdHandler = new CommandHandler();
            
            //Loop for when cmdHandler is stopped
            while (!cmdHandler.isStopped()) {    
                // read client message from instream to clientMsg 
                clientMsg = inStream.readUTF();
                // read severMsg from cmDHandler process to serverMsg
                serverMsg = cmdHandler.process(clientMsg, this);
                // Use outStream to write server message to client
                out.println(serverMsg);
            }
            
            // Wrap up and close operation
            inStream.close();
           
            socket.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
    }
    
    public Lobby getLobby() {
    	return lobby;
    }
   public PrintWriter getWriter() {
	   return out;
   }
   
   public void setLobby(Lobby lobby) {
	   this.lobby = lobby;
   }
   public int getID() {
	    return id;
	  }
   
    
}