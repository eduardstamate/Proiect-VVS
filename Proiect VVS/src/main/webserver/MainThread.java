package main.webserver;

import java.io.IOException;
import java.net.ServerSocket;

public class MainThread extends Thread {
	public static int port = 10008;

	ServerSocket serverSocket = null;
	
	public void kill() {
		try {
			serverSocket.close();
			this.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Connection Socket Created");
			try {
				while (true) {
					System.out.println("Waiting for Connection");
					new WebServer(serverSocket.accept());
				}
			} catch (IOException e) {
				System.err.println("Accept failed.");
//				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10008.");
//			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port: 10008.");
				System.exit(1);
			}
		}
	}
	
	public static void main(String[] args) {new MainThread().run();}

}
