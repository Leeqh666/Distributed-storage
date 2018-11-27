import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class StorageNodeTableModel extends AbstractTableModel {
	private ArrayList<StorageNode> nodes = new ArrayList<StorageNode>();

	public StorageNodeTableModel() {
		// TODO �Զ����ɵĹ��캯�����
	}

	public void setTable(ArrayList<StorageNode> nodes) {
		this.nodes = nodes;
	}

	@Override
	public int getColumnCount() {
		// TODO �Զ����ɵķ������
		return 5;
	}

	@Override
	public int getRowCount() {
		// TODO �Զ����ɵķ������
		return nodes.size();
	}

	public String getColumnName(int columnIndex) {
		// TODO �Զ����ɵķ������
		switch (columnIndex) {
		case 1:
			return "IP";
		case 0:
			return "�ڵ���";
		case 2:
			return "�˿ں�";
		case 3:
			return "ʣ��ռ�";
		case 4:
			return "�Ƿ����";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO �Զ����ɵķ������
		StorageNode node = nodes.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return node.getNodeName();
		case 1:
			return node.getNodeIP();
		case 2:
			return node.getNodePort();
		case 3:
			return node.getVolume();
		case 4:
			return node.isUseable();
		default:
			break;
		}
		return null;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO �Զ����ɵķ������
		return false;
	}

}
