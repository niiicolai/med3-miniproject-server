package org.example.server;

import java.net.ServerSocket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class NetworkService implements Runnable {
	
	public static void main(String[] args) {

		try {
			var networkService = new NetworkService (6666, 10);
			var serverThread = new Thread(networkService);
			
			serverThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int nextID = 0;
	private ServerSocket serverSocket;
	private ExecutorService executorService;

	public NetworkService(int port, int poolSize)
			throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(poolSize);
	}


	public void run() {
		try {

			for(;;) {
				var nextSocket = serverSocket.accept();
				var handler = new Handler(nextID, nextSocket);				
				executorService.execute(handler);
				handler.message("Welcome to the server, Client no."+nextID+" - Current number of lobbies: " + Lobby.lobbies.size());
				nextID = nextID+1;	
			}
		} catch(IOException e) {
			executorService.shutdown();
		}
	}

}