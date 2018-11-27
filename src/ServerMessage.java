import java.io.Serializable;

public class ServerMessage extends ClientMessage implements Serializable{
	private long length;
	private String mainNodeIP;
	private String mainNodePort;
	private String backUpNodeIP;
	private String backUpNodePort;

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getMainNodeIP() {
		return mainNodeIP;
	}

	public void setMainNodeIP(String mainNodeIP) {
		this.mainNodeIP = mainNodeIP;
	}

	public String getMainNodePort() {
		return mainNodePort;
	}

	public void setMainNodePort(String mainNodePort) {
		this.mainNodePort = mainNodePort;
	}

	public String getBackupNodeIP() {
		return backUpNodeIP;
	}

	public void setBackupNodeIP(String backupNodeIP) {
		backUpNodeIP = backupNodeIP;
	}

	public String getBackupNodePort() {
		return backUpNodePort;
	}

	public void setBackupNodePort(String backupNodePort) {
		backUpNodePort = backupNodePort;
	}
	
	public ServerMessage(String uuid,String name,long length,String mainNodeIP,String mainNodePort,String backUpNodeIP,String backUpNodePort ) {
		// TODO 自动生成的构造函数存根
		super(uuid,name);
		this.backUpNodeIP = backUpNodeIP;
		this.backUpNodePort = backUpNodePort;
		this.length = length;
		this.mainNodeIP = mainNodeIP;
		this.mainNodePort = mainNodePort;
	}
}
