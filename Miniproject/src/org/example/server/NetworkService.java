package org.example.server;

import java.net.ServerSocket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkService implements Runnable {

	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private static int nextID = 0;

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
	            nextID = nextID+1;    
	        }
	    } catch(IOException e) {
	      executorService.shutdown();
	    }
	}

}
