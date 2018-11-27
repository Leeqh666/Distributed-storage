import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class StorageNodeProtocol implements StorageNodeStrategy {

	public StorageNodeProtocol(Socket socket, StorageNode node) {
		// TODO 自动生成的构造函数存根
		service(socket, node);
	}

	@Override
	public void service(Socket socket, StorageNode node) {
		// TODO 自动生成的方法存根
		String uuid;
		int command;
		boolean isOriginal;
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			command = dis.readInt();
			uuid = dis.readUTF();
			isOriginal = dis.readBoolean();
			switch (command) {
			case 0:
				try {
					File parentFile = new File(node.getRootFolder());
					if (!parentFile.exists()) {
						parentFile.mkdir();
					}
					File uploadfile = new File(parentFile, uuid);
					if (uploadfile.exists()) {
						throw new Exception();
					}
					FileOutputStream fos = new FileOutputStream(uploadfile);
					byte[] buf = new byte[1024];
					int len = 0;
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					fos.close();
					node.setVolume(node.changeLength(node.changeVolume() - (uploadfile.length() / (1024 * 1024))));
					if (isOriginal) {
						// 传送成功进行备份，需要与其他节点进行连接，然后上传文件
						Socket tempsocket = new Socket("127.0.0.1", 8000);
						DataInputStream tempdis = new DataInputStream(tempsocket.getInputStream());
						DataOutputStream tempdos = new DataOutputStream(tempsocket.getOutputStream());
						tempdos.writeInt(0);
						tempdos.writeBoolean(false);
						tempdos.writeUTF(uuid);
						String tempIP = tempdis.readUTF();
						String tempPort = tempdis.readUTF();
						int port = new Integer(tempPort);
						tempsocket = new Socket(tempIP, port);
						tempdis = new DataInputStream(tempsocket.getInputStream());
						tempdos = new DataOutputStream(tempsocket.getOutputStream());
						InputStream tempis = tempsocket.getInputStream();
						OutputStream tempos = tempsocket.getOutputStream();
						tempdos.writeInt(0);
						tempdos.writeUTF(uuid);
						tempdos.writeBoolean(false);
						File backUpFile = new File(node.getRootFolder(), uuid);
						FileInputStream fis = new FileInputStream(backUpFile);
						byte[] tempbuf = new byte[1024];
						int templen;
						while ((templen = fis.read(tempbuf)) != -1) {
							tempos.write(tempbuf, 0, templen);
						}
						socket.shutdownOutput();
						fis.close();
						tempsocket.close();
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					System.out.println("文件上传失败");
				}
				break;
			case 1:
				try {
					File downloadfile = new File(node.getRootFolder(), uuid);
					if (!downloadfile.exists()) {
						throw new Exception();
					}
					FileInputStream fis = new FileInputStream(downloadfile);
					byte[] buf = new byte[1024];
					int len = 0;
					while ((len = fis.read(buf)) != -1) {
						os.write(buf, 0, len);
					}
					socket.shutdownOutput();
					fis.close();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				break;
			case 2:
				File deletefile = new File(node.getRootFolder(), uuid);
				if (!deletefile.exists()) {
					throw new Exception();
				}
				node.setVolume(node.changeLength(node.changeVolume() + (deletefile.length() / (1024 * 1024))));
				deletefile.delete();
				if (isOriginal) {
					// 删除备份节点上的内容
					Socket tempsocket = new Socket("127.0.0.1", 8000);
					DataInputStream tempdis = new DataInputStream(tempsocket.getInputStream());
					DataOutputStream tempdos = new DataOutputStream(tempsocket.getOutputStream());
					tempdos.writeInt(2);
					tempdos.writeBoolean(false);
					tempdos.writeUTF(uuid);
					String tempIP = tempdis.readUTF();
					String tempPort = tempdis.readUTF();
					int port = new Integer(tempPort);
					tempsocket = new Socket(tempIP, port);
					tempdis = new DataInputStream(tempsocket.getInputStream());
					tempdos = new DataOutputStream(tempsocket.getOutputStream());
					tempdos.writeInt(2);
					tempdos.writeUTF(uuid);
					tempdos.writeBoolean(false);
					tempsocket.close();
				}
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
