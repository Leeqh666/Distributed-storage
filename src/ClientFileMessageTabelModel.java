import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ClientFileMessageTabelModel extends AbstractTableModel {

	private ArrayList<ClientMessage> clientFileMessages = new ArrayList<ClientMessage>();

	public ClientFileMessageTabelModel() {
		// TODO 自动生成的构造函数存根
	}

	public void setTable(ArrayList<ClientMessage> clientMessages) {
		this.clientFileMessages = clientMessages;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO 自动生成的方法存根
		switch (columnIndex) {
		case 1:
			return "文件名";
		case 0:
			return "UUID";
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		// TODO 自动生成的方法存根
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO 自动生成的方法存根
		return clientFileMessages.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO 自动生成的方法存根
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
		// TODO 自动生成的方法存根
		return false;
	}
	
}
