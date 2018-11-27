import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class FileClientFrame extends JFrame {
	FileClient fileClient;
	ClientFileMessageTabelModel model;
	private JTextField field;
	private JFileChooser fileChooser;
	private JTable clientMessageTable;
	private JButton upload_button;
	private JButton download_button;
	private JButton delete_button;

	public FileClientFrame(String string) {
		// TODO 自动生成的构造函数存根
		fileClient = new FileClient(string);
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		JPanel northPanel = setNorthPanel();
		JPanel centerPanel = setCenterPanel();
		JPanel southPanel = setSouthPanel();
		container.add(northPanel, BorderLayout.NORTH);
		container.add(centerPanel, BorderLayout.CENTER);
		container.add(southPanel, BorderLayout.SOUTH);
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					//model.fireTableDataChanged();
					int index = clientMessageTable.getSelectedColumn();
					// System.out.println(index);
					if (index == 0) {
						download_button.setEnabled(true);
						delete_button.setEnabled(true);
					}
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		upload_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				try {
					fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setApproveButtonText("sure");
					fileChooser.setDialogTitle("open files");
					fileChooser.showOpenDialog(container);
					field.setText(fileChooser.getSelectedFile().getPath());
					fileClient.upLoad(fileChooser.getSelectedFile().getPath());
					model.fireTableDataChanged();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
		download_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				fileClient.download(
						(String) (clientMessageTable.getModel().getValueAt(clientMessageTable.getSelectedRow(), 0)));
				model.fireTableDataChanged();
			}
		});
		delete_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				fileClient.delete(
						(String) (clientMessageTable.getModel().getValueAt(clientMessageTable.getSelectedRow(), 0)));
				model.fireTableDataChanged();
			}
		});
	}

	public JPanel setSouthPanel() {
		// TODO 自动生成的方法存根
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		download_button = new JButton("download");
		delete_button = new JButton("delete");
		southPanel.add(download_button);
		southPanel.add(delete_button);
		download_button.setEnabled(false);
		delete_button.setEnabled(false);
		return southPanel;
	}

	public JPanel setCenterPanel() {
		JPanel centerPanel = new JPanel();
		clientMessageTable = new JTable();
		model = new ClientFileMessageTabelModel();
		model.setTable(fileClient.getClientMessages());
		clientMessageTable.setModel(model);
		centerPanel.add(new JScrollPane(clientMessageTable));
		return centerPanel;
	}

	public JPanel setNorthPanel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());
		field = new JTextField(20);
		field.setEnabled(false);
		upload_button = new JButton("upload");
		northPanel.add(field);
		northPanel.add(upload_button);
		return northPanel;
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		FileClientFrame app = new FileClientFrame(args[0]);
		app.setSize(800, 600);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}

}
