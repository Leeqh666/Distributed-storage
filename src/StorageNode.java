import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class StorageNode {
	private String nodeName;
	private String nodeIP;
	private String nodePort;
	private String rootFolder;
	private String volume;
	private String fileServerIP;
	private String fileServerPort;
	private File propertyFile;
	private boolean useable;
	private Properties properties = new Properties();
	
	public static void main(String args[]) {
		StorageNode node = new StorageNode(args[0]);
	}
	
	public StorageNode getStorageNode() {
		return this;
	}
	
	public StorageNode() {
		// TODO 自动生成的构造函数存根		
	}
	
	public StorageNode(String file) {
		propertyFile = new File(".",file);
		loadProperties();
		useable = true;
		Thread thread = new Thread(){
			public void run() {
				while(true){
					sendUDP();
					//System.out.println("1111");
					try {
						sleep(2000);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		int port = new Integer(nodePort);
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = serverSocket.accept();
				Thread thread2 = new Thread(){
					public void run() {
						useable = false;
						sendUDP();
						StorageNodeProtocol SNP = new StorageNodeProtocol(socket,getStorageNode());
						useable = true;
						sendUDP();
					}
				};
				thread2.start();
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public boolean isUseable() {
		return useable;
	}

	public void setUseable(boolean useable) {
		this.useable = useable;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeIP() {
		return nodeIP;
	}

	public void setNodeIP(String nodeIP) {
		this.nodeIP = nodeIP;
	}

	public String getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	public String getNodePort() {
		return nodePort;
	}

	public void setNodePort(String nodePort) {
		this.nodePort = nodePort;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public void sendUDP() {
		try {
			DatagramSocket dSocket = new DatagramSocket();
			String string = "";
			string = nodeName + "_" + nodeIP + "_" + nodePort + "_" + volume + "_" + useable+"_";
			byte[] outBuffer = string.getBytes();
			InetAddress ip = InetAddress.getByName(fileServerIP);
//			System.out.println(ip);
			int port = new Integer(fileServerPort);
			DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, ip, port);
			dSocket.send(outPacket);
			dSocket.close();
			properties.setProperty("Volume", volume);
			FileOutputStream outFile = new FileOutputStream(propertyFile);
			properties.store(outFile, "Comment");
			outFile.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void loadProperties() {
		try {
			properties.load(new BufferedInputStream(new FileInputStream(propertyFile)));
			nodeName = properties.getProperty("NodeName");
			nodeIP = properties.getProperty("NodeIP");
			nodePort = properties.getProperty("NodePort");
			rootFolder = properties.getProperty("RootFolder");
			volume = properties.getProperty("Volume");
			fileServerIP = properties.getProperty("FileServerIP");
			fileServerPort = properties.getProperty("FileServerPort");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public long changeVolume() {
		String length = "";
		for (int i = 0; i < volume.length(); i++) {
			if (volume.charAt(i) >= '0' && volume.charAt(i) <= '9') {
				length = length + volume.charAt(i);
			}
		}
		return new Long(length);
	}

	public String changeLength(long length) {
		return String.valueOf(length) + "MB";
	}
}
