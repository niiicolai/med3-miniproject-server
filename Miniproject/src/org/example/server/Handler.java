package org.example.server;

import java.net.Socket;

public class Handler implements Runnable {
	
	private Socket socket;
	
	Handler(Socket socket) {this.socket = socket; }

	
	public void run() {
		

	}

}
