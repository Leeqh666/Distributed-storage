import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ClientFileMessageTabelModel extends AbstractTableModel {

	private ArrayList<ClientMessage> clientFileMessages = new ArrayList<ClientMessage>();

	public ClientFileMessageTabelModel() {
		// TODO �Զ����ɵĹ��캯�����
	}

	public void setTable(ArrayList<ClientMessage> clientMessages) {
		this.clientFileMessages = clientMessages;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO �Զ����ɵķ������
		switch (columnIndex) {
		case 1:
			return "�ļ���";
		case 0:
			return "UUID";
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		// TODO �Զ����ɵķ������
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO �Զ����ɵķ������
		return clientFileMessages.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO �Զ����ɵķ������
		ClientMessage clientMessage = clientFileMessages.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return clientMessage.getUuid();
		case 1:
			return clientMessage.getName();
		default:
			break;
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO �Զ����ɵķ������
		return false;
	}
	
}
