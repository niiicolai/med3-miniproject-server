package org.example.server;

import java.net.Socket;
import java.net.SocketException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Handler implements Runnable {

	private Socket socket;
	private int id;
	private String formattedName;

	// Added client and server messages
	private String clientMsg;
	private String serverMsg;
	private PrintWriter writer;
	private Lobby lobby = null;
	private static final String handlerWelcomeMsg = "Welcome to the server";
	private static final String serverQuitLogMsg = "Client left the server - id: ";

	Handler(int id, Socket socket) throws IOException {
		this.id = id;
		this.socket = socket;
		this.writer = new PrintWriter(socket.getOutputStream(), true);
	}

	@Override
	public void run() {
		
		var cmdHandler = new CommandHandler();
		DataInputStream inStream = null;
		try {
			// Instanced inStream, outStream and cmdHandler
			inStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Loop for when cmdHandler is stopped
		while (!cmdHandler.isStopped()) {

			if (formattedName == null) {

				String name = "";
				try {
					name = inStream.readUTF();
				} catch (IOException e) {
					cmdHandler.stop(this);
				}

				formattedName = id + "-" + name;
				continue;
			}

			// read client message from instream to clientMsg
			try {
				clientMsg = inStream.readUTF();

				// read severMsg from cmDHandler process to serverMsg
				serverMsg = cmdHandler.process(clientMsg, this);
				// Use outStream to write server message to client
				if (serverMsg.length() > 0) {
					message(serverMsg);
				}
			} catch (IOException e) {
				cmdHandler.stop(this);
			}
		}

		try {
			// Wrap up and close operation
			inStream.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println(serverQuitLogMsg + id);
		}

	}

	public int getID() {
		return id;
	}

	public void message(String msg) throws IOException {
		writer.println(msg);
	}

	public void welcome() throws IOException {
		message(handlerWelcomeMsg);
	}

	public Lobby getLobby() {
		return lobby;
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	public String getFormattedName() {
		return formattedName;
	}
}