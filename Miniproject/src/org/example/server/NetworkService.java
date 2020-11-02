package org.example.server;

import java.net.ServerSocket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class NetworkService implements Runnable {
	
	private static final int threadPoolSize = 10;
	private static final int port = 6666;
	private static final String serverWelcomeMsg = "Welcome to the server, Client no.";
	private static final String serverNumLobbiesMsg = " - Current number of lobbies: ";
	
	public static void main(String[] args) {

		try {
			var networkService = new NetworkService (port, threadPoolSize);
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

	@Override
	public void run() {
		try {

			for(;;) {
				var nextSocket = serverSocket.accept();
				var handler = new Handler(nextID, nextSocket);				
				executorService.execute(handler);
				handler.message(serverWelcomeMsg+nextID+serverNumLobbiesMsg + Lobby.lobbies.size());
				nextID = nextID+1;	
			}
		} catch(IOException e) {
			executorService.shutdown();
		}
	}

}