package be.cegeka;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BuildLightListener {

	public static void main(String[] args) throws IOException {
		int port = getPort(args);
		System.out.println("Listening on TCP port " + port);
		
		ServerSocket serverSocket = new ServerSocket(port);
		
		
		byte[] buffer = new byte[3];
		
		while(true) {
			Socket socket = serverSocket.accept();
			socket.getInputStream().read(buffer);
			socket.close();
			
			String command = createCommand(buffer);
			System.out.println(command);
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println();
			}
		}
	}
	
	private static String createCommand(byte[] data) {
		byte red = data[0];
		byte green = data[1];
		byte blue = data[2];
		StringBuilder sb = new StringBuilder();
		sb.append("set-led.exe");
		sb.append(" rgb ");
		sb.append((int)red);
		sb.append(" ");
		sb.append((int)green);
		sb.append(" ");
		sb.append((int)blue);
		return sb.toString();
	}

	private static int getPort(String[] args) {
		return Integer.parseInt(args[0]);
	}
}
