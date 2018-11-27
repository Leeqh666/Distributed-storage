import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ServerFileMessageTableModel extends AbstractTableModel {

	private ArrayList<ServerMessage> serverMessages = new ArrayList<ServerMessage>();

	public ServerFileMessageTableModel() {
		// TODO �Զ����ɵĹ��캯�����
	}

	public void setTable(ArrayList<ServerMessage> serverMessages) {
		this.serverMessages = serverMessages;
	}

	public String getColumnName(int columnIndex) {
		// TODO �Զ����ɵķ������
		switch (columnIndex) {
		case 1:
			return "�ļ���";
		case 0:
			return "UUID";
		case 2:
			return "�ļ���С";
		case 3:
			return "���ڵ�ip";
		case 4:
			return "���ڵ�˿ں�";
		case 5:
			return "���ݽڵ�IP";
		case 6:
			return "���ݽڵ�˿ں�";
		}
		return null;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public int getColumnCount() {
		// TODO �Զ����ɵķ������
		return 7;
	}

	@Override
	public int getRowCount() {
		// TODO �Զ����ɵķ������
		return serverMessages.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO �Զ����ɵķ������
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
