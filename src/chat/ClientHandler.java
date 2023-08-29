package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();	
	public Socket socket;
	private BufferedReader bufferedReader ;
	private BufferedWriter bufferedWriter ;
	private String nickCliente;

	
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.nickCliente = bufferedReader.readLine(); // pegando do terminal, porem quero pegar da GUI
			clientHandlers.add(this);
			enviaParaTodos("Servidor: " + nickCliente + "entrou no chat.");
			
		} catch (IOException e) {
			fechaTudo(socket, bufferedReader,bufferedWriter);
		}
		
		
	}



	@Override
	public void run() {
		String msgDoCliente;
		
		while(socket.isConnected()) {
			try {
				msgDoCliente = bufferedReader.readLine();
				enviaParaTodos(msgDoCliente); 
			} catch(IOException e) {
				fechaTudo(socket, bufferedReader,bufferedWriter);
				break;
			}
		}
		
	}
	


	public void enviaParaTodos(String mensagem) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {
				if(!clientHandler.nickCliente.equals(nickCliente)) {
					clientHandler.bufferedWriter.write(mensagem);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
				}
			} catch (IOException e) {
				fechaTudo(socket, bufferedReader,bufferedWriter);			
				
			}
		}
	}
	
	private void fechaTudo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		removeClientHandler();
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (socket != null) {
				socket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
		enviaParaTodos("Servidor: " + nickCliente + "saiu do chat");
	}
	
}
