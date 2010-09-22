package be.cegeka;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class BuildLightListener {

	public static void main(String[] args) throws IOException {
		int port = getPort(args);
		System.out.println("Listening for UDP packets on port " + port);
		DatagramSocket serverSocket = new DatagramSocket(port);
		byte[] buffer = new byte[3];
		
		while(true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			serverSocket.receive(packet);
			
			String command = createCommand(packet);
			System.out.println(command);
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println();
			}
		}
	}
	
	private static String createCommand(DatagramPacket packet) {
		byte red = packet.getData()[0];
		byte green = packet.getData()[1];
		byte blue = packet.getData()[2];
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
