import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ServerFileMessageTableModel extends AbstractTableModel {

	private ArrayList<ServerMessage> serverMessages = new ArrayList<ServerMessage>();

	public ServerFileMessageTableModel() {
		// TODO 自动生成的构造函数存根
	}

	public void setTable(ArrayList<ServerMessage> serverMessages) {
		this.serverMessages = serverMessages;
	}

	public String getColumnName(int columnIndex) {
		// TODO 自动生成的方法存根
		switch (columnIndex) {
		case 1:
			return "文件名";
		case 0:
			return "UUID";
		case 2:
			return "文件大小";
		case 3:
			return "主节点ip";
		case 4:
			return "主节点端口号";
		case 5:
			return "备份节点IP";
		case 6:
			return "备份节点端口号";
		}
		return null;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int getColumnCount() {
		// TODO 自动生成的方法存根
		return 7;
	}

	@Override
	public int getRowCount() {
		// TODO 自动生成的方法存根
		return serverMessages.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO 自动生成的方法存根
		ServerMessage serverMessage = serverMessages.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return serverMessage.getUuid();
		case 1:
			return serverMessage.getName();
		case 2:
			return serverMessage.getLength();
		case 3:
			return serverMessage.getMainNodeIP();
		case 4:
			return serverMessage.getMainNodePort();
		case 5:
			return serverMessage.getBackupNodeIP();
		case 6:
			return serverMessage.getBackupNodePort();
		}
		return null;
	}

	
}
