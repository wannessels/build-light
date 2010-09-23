package be.cegeka;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import org.kohsuke.stapler.DataBoundConstructor;

public class BuildLightPublisher extends Notifier {
	
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
		send(message, listener.getLogger());
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


	private void send(byte[] message, PrintStream logger) {
		Socket socket = null;
		try {
			logger.println("sending " + toString(message) + " to " + host + ":" + port);
			socket = new Socket(InetAddress.getByName(host), port);
			socket.getOutputStream().write(message);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace(logger);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace(logger);
				}
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
