package be.cegeka;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class BuildLightPublisher extends Notifier {
	
	private static final int SENDER_PORT = 65432;

	@Extension
	public static class BuildLightDescriptor extends BuildStepDescriptor<Publisher> {

		@Override
		public String getDisplayName() {
			return "Build light";
		}
		
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}
	}

	private static final byte[] GREEN = {0,(byte) 255,0};
	private static final byte[] RED = {(byte) 255,0,0};

	private String host;
	private int port;

	@DataBoundConstructor
	public BuildLightPublisher(String host, String port) {
		this.host = host;
		this.port = Integer.parseInt(port);
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		byte[] message = Result.SUCCESS.equals(build.getResult()) ? GREEN : RED;
		listener.getLogger().println("sending " + toString(message) + " to " + host + ":" + port);
		send(message);
		return true;
	}
	
	private String toString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append((int)b);
			sb.append(" ");
		}
		return sb.toString();
	}


	private void send(byte[] message) {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(SENDER_PORT);
			DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(host), port);
			socket.send(packet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}
	public String getHost() {
		return host;
	}
	
	public String getPort() {
		return String.valueOf(port);
	}
	
}
