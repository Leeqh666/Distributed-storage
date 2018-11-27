import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class FileServer {
	private ArrayList<ServerMessage> serverMessages = new ArrayList<ServerMessage>();
	private ArrayList<StorageNode> storageNodes = new ArrayList<StorageNode>();
	private HashMap<String, Integer> heart = new HashMap<String, Integer>();

	public ArrayList<ServerMessage> getServerMessages() {
		return serverMessages;
	}

	public void setServerMessages(ArrayList<ServerMessage> serverMessages) {
		this.serverMessages = serverMessages;
	}

	public ArrayList<StorageNode> getStorageNodes() {
		return storageNodes;
	}

	public void setStorageNodes(ArrayList<StorageNode> storageNodes) {
		this.storageNodes = storageNodes;
	}

	public String getUuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public FileServer getFileServer() {
		return this;
	}

	public String[] changeString(String string) {
		String[] messages = new String[5];
		int count = 0;
		while (string.length() != 0) {
			int i = string.indexOf("_");
			messages[count] = string.substring(0, i);
			count++;
			string = string.substring(i + 1, string.length());
		}
		// messages[4] = string;
		return messages;
	}

	public void getUDP() {
		try {
			int index = -1;
			byte[] inBuffer = new byte[1024];
			DatagramSocket dSocket = new DatagramSocket(7777);
			while (true) {
				// System.out.println("准备接收UDP消息");
				boolean flag = true;
				DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
				dSocket.receive(inPacket);
				String string = new String(inPacket.getData(), 0, inPacket.getLength());
				String[] messages = changeString(string);
				for (int i = 0; i < getStorageNodes().size(); i++) {
					if (getStorageNodes().get(i).getNodeName().equals(messages[0])) {
						index = i;
						flag = false;
						break;
					}
				}
				if (flag) {
					StorageNode node = new StorageNode();
					node.setNodeName(messages[0]);
					node.setNodeIP(messages[1]);
					node.setNodePort(messages[2]);
					node.setVolume(messages[3]);
					if (messages[4].equals("true")) {
						node.setUseable(true);
					} else {
						node.setUseable(false);
					}
					getStorageNodes().add(node);
					heart.put(node.getNodeName(), 0);
					System.out.println(node.getNodeName() + "激活");
				} else if (flag == false && index != -1) {
					StorageNode node = getStorageNodes().get(index);
					node.setVolume(messages[3]);
					if (messages[4].equals("true")) {
						node.setUseable(true);
					} else {
						node.setUseable(false);
					}
					heart.put(node.getNodeName(), 0);
				}
				// System.out.println(getStorageNodes().size());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public FileServer() {
		// TODO 自动生成的构造函数存根
		getSerialization();
		Thread thread = new Thread() {
			public void run() {
				getUDP();
			}
		};
		thread.start();
		Thread thread2 = new Thread() {
			public void run() {
				while (true) {
					if (heart.size() != 0) {
						Iterator<String> iterator = heart.keySet().iterator();
						while (iterator.hasNext()) {
							String key = (String) iterator.next();
							if (heart.get(key) > 3) {
								for (int i = 0; i < getStorageNodes().size(); i++) {
									if (getStorageNodes().get(i).getNodeName().equals(key)) {
										System.out.println(getStorageNodes().get(i).getNodeName() + "失去连接");
										getStorageNodes().remove(i);
									}
								}
							}
						}
					}
					heartbeat();
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
		};
		thread2.start();
		Thread thread4 = new Thread() {
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(8000);
					while (true) {
						Socket socket = serverSocket.accept();
						Thread thread3 = new Thread() {
							public void run() {
								ServerProtocol serverProtocol = new ServerProtocol(socket, getFileServer());
								setSerialization();
							}
						};
						thread3.start();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
		thread4.start();
//		while(true){
//			if(storageNodes.size()!=0&&storageNodes.get(0).isUseable()){
//				System.out.println(storageNodes.get(0).isUseable());
//			}
//			else {
//				System.out.println("服了");
//			}
//		}
	}

	public void heartbeat() {
		if (heart.size() != 0) {
			Iterator<String> iterator = heart.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				int value = heart.get(key) + 1;
				heart.put(key, value);
			}
		}
	}

	public void setSerialization() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("FileServer.txt"));
			oos.writeObject(serverMessages);
			oos.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void getSerialization() {
		try {
			File file = new File("FileServer.txt");
			if (file.length() != 0) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream("FileServer.txt"));
				serverMessages = (ArrayList<ServerMessage>) ois.readObject();
				System.out.println(serverMessages.size());
				ois.close();
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {
		FileServer fs = new FileServer();
	}
}
