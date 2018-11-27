import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerProtocol implements ServerStrategy {

	public ServerProtocol(Socket socket, FileServer fs) {
		// TODO 自动生成的构造函数存根
		service(socket, fs);
	}

	@Override
	public void service(Socket socket, FileServer fs) {
		// TODO 自动生成的方法存根
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			int command;
			String uuid;
			String filename;
			long length;
			StorageNode mainNode;
			StorageNode backUpNode;
			boolean isMain;
			command = dis.readInt();
			isMain = dis.readBoolean();
			switch (command) {
			case 0:
				if (isMain) {
					filename = dis.readUTF();
					length = dis.readLong();
					mainNode = findNode(length, fs, null);
					backUpNode = findNode(length, fs, mainNode);
					if(mainNode == null){
						mainNode = new StorageNode();
						mainNode.setNodeIP("0");
						mainNode.setNodePort("0");
					}
					if(backUpNode == null){
						backUpNode = new StorageNode();
						backUpNode.setNodeIP("0");
						backUpNode.setNodePort("0");
					}
					// while (backUpNode == mainNode) {
					// backUpNode = findNode(length, fs);
					// }
					uuid = fs.getUuid();
					fs.getServerMessages().add(new ServerMessage(uuid, filename, length, mainNode.getNodeIP(),
							mainNode.getNodePort(), backUpNode.getNodeIP(), backUpNode.getNodePort()));
					dos.writeUTF(mainNode.getNodeIP());
					dos.writeUTF(mainNode.getNodePort());
					dos.writeUTF(uuid);
				} else {
					uuid = dis.readUTF();
					for (int i = 0; i < fs.getServerMessages().size(); i++) {
						if (fs.getServerMessages().get(i).getUuid().equals(uuid)) {
							dos.writeUTF(fs.getServerMessages().get(i).getBackupNodeIP());
							dos.writeUTF(fs.getServerMessages().get(i).getBackupNodePort());
							// dos.writeUTF(uuid);
							break;
						}
					}
				}
				break;
			case 1:
				if (isMain) {
					uuid = dis.readUTF();
					for (int i = 0; i < fs.getServerMessages().size(); i++) {
						if (fs.getServerMessages().get(i).getUuid().equals(uuid)) {
							dos.writeUTF(fs.getServerMessages().get(i).getMainNodeIP());
							dos.writeUTF(fs.getServerMessages().get(i).getMainNodePort());
							break;
						}
					}
				} else {
					uuid = dis.readUTF();
					for (int i = 0; i < fs.getServerMessages().size(); i++) {
						if (fs.getServerMessages().get(i).getUuid().equals(uuid)) {
							dos.writeUTF(fs.getServerMessages().get(i).getBackupNodeIP());
							dos.writeUTF(fs.getServerMessages().get(i).getBackupNodePort());
							break;
						}
					}
				}
				break;
			case 2:
				if (isMain) {
					uuid = dis.readUTF();
					for (int i = 0; i < fs.getServerMessages().size(); i++) {
						if (fs.getServerMessages().get(i).getUuid().equals(uuid)) {
							dos.writeUTF(fs.getServerMessages().get(i).getMainNodeIP());
							dos.writeUTF(fs.getServerMessages().get(i).getMainNodePort());
							break;
						}
					}
				} else {
					uuid = dis.readUTF();
					for (int i = 0; i < fs.getServerMessages().size(); i++) {
						if (fs.getServerMessages().get(i).getUuid().equals(uuid)) {
							dos.writeUTF(fs.getServerMessages().get(i).getBackupNodeIP());
							dos.writeUTF(fs.getServerMessages().get(i).getBackupNodePort());
							fs.getServerMessages().remove(i);
							break;
						}
					}
				}
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public StorageNode findNode(long length, FileServer fs, StorageNode node) {
		int index = 0;
		long[] now_volume = new long[fs.getStorageNodes().size()];
		for (int i = 0; i < fs.getStorageNodes().size(); i++) {
			if (fs.getStorageNodes().get(i).changeVolume() * 1024 * 1024 > length
					&& fs.getStorageNodes().get(i) != node && fs.getStorageNodes().get(i).isUseable()) {
				now_volume[i] = fs.getStorageNodes().get(i).changeVolume();
			}
		}
		for (int i = 0; i < now_volume.length; i++) {
			if (now_volume[i] > now_volume[index]) {
				index = i;
			}
		}
		// fs.getStorageNodes().get(index).setUseable(false);
		if (now_volume[index] > 0) {
			return fs.getStorageNodes().get(index);
		}
		else {
			return null;
		}
	}
}