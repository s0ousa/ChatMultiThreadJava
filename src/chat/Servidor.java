package chat;

import java.net.*;
import java.io.*;

public class Servidor {
	
	private ServerSocket serverSocket;
	
	
	public Servidor (ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void iniciarServidor() {
		
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("Um novo cliente se conectou");
				
				ClientHandler clientHandler = new ClientHandler(socket);
				Thread thread = new Thread (clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			
		}
	}
	
	public void fecharServerSocket() {
		try {
			if (serverSocket !=null) {
				serverSocket.close();
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(1234);
		Servidor servidor = new Servidor(serverSocket);
		
		servidor.iniciarServidor();
	}
}
