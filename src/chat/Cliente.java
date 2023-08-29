package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	public Socket socket;
	private BufferedReader bufferedReader ;
	private BufferedWriter bufferedWriter ;
	private String nickCliente;
	
	
	public Cliente(Socket socket,  String nickCliente) {
		
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.nickCliente = nickCliente; // mudar aqui também
			
			
		} catch (IOException e) {
			fechaTudo(socket, bufferedReader,bufferedWriter);
		}
	}
	
	public void enviaMensagem() {
		
		try {
			bufferedWriter.write(nickCliente);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			// depois que pegar da GUI, pode apagar essas linhas acima
			
			Scanner scanner = new Scanner(System.in);
			
			while(socket.isConnected()) {
				String msg = scanner.nextLine();
				bufferedWriter.write(nickCliente + ": " + msg);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		
		} catch(IOException e) {
			fechaTudo(socket, bufferedReader,bufferedWriter);
		}
	}
	
	
	public void esperaPelaMessagem() {
		new Thread(new Runnable() {
			
			
			public void run() {
				
				String msgVindaDaSala;
				
				while (socket.isConnected()) {
					try {
						msgVindaDaSala = bufferedReader.readLine();
						System.out.println(msgVindaDaSala); // mudar para entrar no textarea
					} catch (Exception e) {
						fechaTudo(socket, bufferedReader,bufferedWriter);
					}
				}
			}

			
		}).start();
	}

	private void fechaTudo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		
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
	
	public static void main(String[] args) throws IOException {
		
		// pode botar tudo isso abaixo na gui
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Digite seu nome para entrar na sala"); 
		String nick = scanner.nextLine();
		Socket socket = new Socket("localhost", 1234); // alterar na hora de mostrar na sala
		Cliente cliente = new Cliente(socket, nick);
		cliente.esperaPelaMessagem();
		cliente.enviaMensagem(); // chamar ao clicar em enviar na GUI , alterar no metodo oq for necessario
	}
}
