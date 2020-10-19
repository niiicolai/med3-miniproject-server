package org.example.server;

import java.net.ServerSocket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkService implements Runnable {

	private ServerSocket serverSocket;
	private ExecutorService executorService;
	
	public NetworkService(int port, int poolSize)
	
			throws IOException {
		
		serverSocket = new ServerSocket();
		executorService = Executors.newFixedThreadPool(poolSize);
	}
	

	public void run() {
		try {
			
			for(;;) {
				executorService.execute(new Handler(serverSocket.accept()));
			}
		
		} catch(IOException e) {
			executorService.shutdown();
		}
	}

}

