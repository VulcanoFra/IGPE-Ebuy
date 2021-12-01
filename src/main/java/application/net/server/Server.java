package application.net.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private HashMap<String, ObjectOutputStream> utentiLoggati;
	
	
	public Server() {
		executorService = Executors.newCachedThreadPool();
		utentiLoggati = new HashMap<String, ObjectOutputStream>();
	}
	
	public boolean startServer() {
		try {
			serverSocket = new ServerSocket(8000);
			
			Thread thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
			
			return true;
		}
		catch(IOException e) {
			serverSocket = null;
			return false;
		}
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				System.out.println("[SERVER] Waiting connections...");
				Socket socket = serverSocket.accept();
				System.out.println("[SERVER] Client is connected");
				CommunicationHandler handler = new CommunicationHandler(socket, this);
				executorService.submit(handler);
			} catch (IOException e) {
				System.out.println("[SERVER] Close of server...");
				return;
			}
		}
	}
	
	public void addActiveUser(String username, ObjectOutputStream out) {
		if(username == null || out == null)
			return;
		utentiLoggati.put(username, out);		
	}
	
	public void removeActiveUser(String username) {
		if(username == null)
			return;
		utentiLoggati.remove(username);
	}
	
	public ObjectOutputStream isLogged(String username) {
		return utentiLoggati.get(username);
	}

}
