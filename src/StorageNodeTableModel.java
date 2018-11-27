import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class StorageNodeTableModel extends AbstractTableModel {
	private ArrayList<StorageNode> nodes = new ArrayList<StorageNode>();

	public StorageNodeTableModel() {
		// TODO 自动生成的构造函数存根
	}

	public void setTable(ArrayList<StorageNode> nodes) {
		this.nodes = nodes;
	}

	@Override
	public int getColumnCount() {
		// TODO 自动生成的方法存根
		return 5;
	}

	@Override
	public int getRowCount() {
		// TODO 自动生成的方法存根
		return nodes.size();
	}

	public String getColumnName(int columnIndex) {
		// TODO 自动生成的方法存根
		switch (columnIndex) {
		case 1:
			return "IP";
		case 0:
			return "节点名";
		case 2:
			return "端口号";
		case 3:
			return "剩余空间";
		case 4:
			return "是否可用";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO 自动生成的方法存根
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
		// TODO 自动生成的方法存根
		return false;
	}

}
