import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class FileClient {
	private OutputStream os;
	private InputStream is;
	private Socket socket;
	private Properties properties = new Properties();
	private ArrayList<ClientMessage> clientMessages = new ArrayList<ClientMessage>();
	TestDES td = new TestDES("lee");
	private String propertyfile;

	public FileClient(String string) {
		// TODO 自动生成的构造函数存根
		try {
			propertyfile = string;
			FileInputStream fis = new FileInputStream(propertyfile);
			properties.load(fis);
			Iterator<String> it = properties.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				clientMessages.add(new ClientMessage(key, properties.getProperty(key)));
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取配置文件失败");
		}
	}

	public ArrayList<ClientMessage> getClientMessages() {
		return clientMessages;
	}

	public void upLoad(String string) {
		// TODO 自动生成的方法存根
		String uuid = "";
		try {
			String ipAddress = "127.0.0.1";
			// String spare_ipAddress = "";
			String port = "8000";
			// String spare_port = "";
			File file = new File(string);
			getConnection(ipAddress, port);
			DataInputStream dis = new DataInputStream(is);
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(0);
			dos.writeBoolean(true);
			dos.writeUTF(file.getName());
			dos.writeLong(file.length());
			ipAddress = dis.readUTF();
			port = dis.readUTF();
			uuid = dis.readUTF();
			getConnection(ipAddress, port);
			dos = new DataOutputStream(os);
			dis = new DataInputStream(is);
			dos.writeInt(0);
			dos.writeUTF(uuid);
			dos.writeBoolean(true);
			File tempfile = new File(".", "tempfile");
			File zipfile = ZipUtil.zip(file.getPath());
			td.encrypt(zipfile.getPath(), tempfile.getPath());
			// FileEncAndDec.EncFile(zipfile, tempfile);
			// FileEncAndDec.EncFile(file, tempfile);
			// file = ZipUtil.zip(tempfile.getPath());
			FileInputStream fis = new FileInputStream(tempfile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = fis.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			socket.shutdownOutput();
			fis.close();
			socket.close();
			clientMessages.add(new ClientMessage(uuid, file.getName()));
			properties.setProperty(uuid, file.getName());
			saveProperties();
			tempfile.delete();
			zipfile.delete();
		} catch (Exception e) {
			try {
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
				File backUpFile = new File(string);
				File tempfile = new File(".", "tempfile");
				File zipfile = ZipUtil.zip(backUpFile.getPath());
				td.encrypt(zipfile.getPath(), tempfile.getPath());
				FileInputStream fis = new FileInputStream(tempfile);
				byte[] tempbuf = new byte[1024];
				int templen;
				while ((templen = fis.read(tempbuf)) != -1) {
					tempos.write(tempbuf, 0, templen);
				}
				socket.shutdownOutput();
				fis.close();
				tempsocket.close();
				tempfile.delete();
				zipfile.delete();
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
				System.out.println("文件1+1备份上传失败");
			}
		}
	}

	public void download(String string) {
		try {
			String ipAddress = "127.0.0.1";
			// String spare_ipAddress = "";
			String port = "8000";
			// String spare_port = "";
			// File file = new File(string);
			getConnection(ipAddress, port);
			DataInputStream dis = new DataInputStream(is);
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(1);
			dos.writeBoolean(true);
			dos.writeUTF(string);
			// dos.writeLong(file.length());
			ipAddress = dis.readUTF();
			port = dis.readUTF();
			getConnection(ipAddress, port);
			dos = new DataOutputStream(os);
			dis = new DataInputStream(is);
			dos.writeInt(1);
			dos.writeUTF(string);
			dos.writeBoolean(true);
			// File file = new File(".", properties.getProperty(string));
			File tempfile = new File(".", "tempfile");
			File zipfile = new File(".", "zipfile.zip");
			if (!zipfile.exists()) {
				zipfile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(tempfile);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.close();
			socket.close();
			td.decrypt(tempfile.getPath(), zipfile.getPath());
			ZipUtil.unzip(zipfile.getPath());
			zipfile.delete();
			tempfile.delete();
		} catch (Exception e) {
			try {
				getConnection("127.0.0.1", "8000");
				DataInputStream dis = new DataInputStream(is);
				DataOutputStream dos = new DataOutputStream(os);
				dos.writeInt(1);
				dos.writeBoolean(false);
				dos.writeUTF(string);
				// dos.writeLong(file.length());
				String ipAddress = dis.readUTF();
				String port = dis.readUTF();
				getConnection(ipAddress, port);
				dos = new DataOutputStream(os);
				dis = new DataInputStream(is);
				dos.writeInt(1);
				dos.writeUTF(string);
				dos.writeBoolean(true);
				File file = new File(".", properties.getProperty(string));
				File tempfile = new File(".", "tempfile");
				File zipfile = new File(".", "zipfile.zip");
				if (!zipfile.exists()) {
					zipfile.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(tempfile);
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = is.read(buf)) != -1) {
					fos.write(buf, 0, len);
				}
				fos.close();
				socket.close();
				td.decrypt(tempfile.getPath(), zipfile.getPath());
				ZipUtil.unzip(zipfile.getPath());
				zipfile.delete();
				tempfile.delete();
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				System.out.println("主备节点均下载失败");
				e.printStackTrace();
			}

		}
	}

	public void delete(String string) {
		try {
			String ipAddress = "127.0.0.1";
			// String spare_ipAddress = "";
			String port = "8000";
			// String spare_port = "";
			// File file = new File(string);
			getConnection(ipAddress, port);
			DataInputStream dis = new DataInputStream(is);
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(2);
			dos.writeBoolean(true);
			dos.writeUTF(string);
			// dos.writeLong(file.length());
			ipAddress = dis.readUTF();
			port = dis.readUTF();
			getConnection(ipAddress, port);
			dos = new DataOutputStream(os);
			dis = new DataInputStream(is);
			dos.writeInt(2);
			dos.writeUTF(string);
			dos.writeBoolean(true);
			socket.close();
			for (int i = 0; i < clientMessages.size(); i++) {
				if (clientMessages.get(i).getUuid().equals(string)) {
					clientMessages.remove(i);
				}
			}
			properties.remove((String) string);
			saveProperties();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("删除文件失败");
		}
	}

	public void saveProperties() {
		try {
			FileOutputStream oFile = new FileOutputStream(propertyfile);
			properties.store(oFile, "Comment");
			oFile.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void getConnection(String ipAddress, String port) throws Exception {
		int tempPort = new Integer(port);
		socket = new Socket(ipAddress, tempPort);
		is = socket.getInputStream();
		os = socket.getOutputStream();
	}

}
