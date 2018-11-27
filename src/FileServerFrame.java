import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class FileServerFrame extends JFrame {
	private JTable fileTable;
	private JTable nodeTable;
	private JTabbedPane tabbedPane;
	private String[] tabNames = { "文件信息", "节点信息" };
	private FileServer fs;

	public FileServerFrame() {
		// TODO 自动生成的构造函数存根
		fs = new FileServer();

		Container container = getContentPane();

		container.setLayout(new FlowLayout());

		tabbedPane = new JTabbedPane();

		JPanel panel1 = new JPanel();
		ServerFileMessageTableModel model1 = new ServerFileMessageTableModel();
		model1.setTable(fs.getServerMessages());
		fileTable = new JTable(model1);
		panel1.add(new JScrollPane(fileTable));
		tabbedPane.add(tabNames[0], panel1);

		JPanel panel2 = new JPanel();
		StorageNodeTableModel model2 = new StorageNodeTableModel();
		model2.setTable(fs.getStorageNodes());
		nodeTable = new JTable(model2);
		panel2.add(new JScrollPane(nodeTable));
		tabbedPane.addTab(tabNames[1], panel2);

		container.add(tabbedPane);
		
		Thread thread = new Thread(){
			public void run() {
				while(true){
					model1.fireTableDataChanged();
					model2.fireTableDataChanged();
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	public static void main(String args[]) {
		FileServerFrame fsf = new FileServerFrame();
		fsf.setSize(800, 600);
		fsf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fsf.setVisible(true);
	}
}
