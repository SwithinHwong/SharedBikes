package application;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import models.ComparableWithIndex;

public class ManageArrangePanel extends JPanel {
	private JTable table;
	private OrdersManager ordersManager = OrdersManager.getInstance();
	private boolean initialed = false;

	/**
	 * Create the panel.
	 */
	public ManageArrangePanel() {
		setBounds(0, 0, 800, 500);
		setLayout(null);
		
//		creatTabel();
//		table = new JTable();
		
		
//		table.setBounds(6, 6, 400, 500);
//		add(table);
	}
//	
	public void creatTabel() {
		if (initialed) {
			return;
		}
		ArrayList<int[]> arrange = ordersManager.logistics();
		
	
		Object[][] table_elem = new Object[arrange.size()][3];
		
		for (int i = 0; i < table_elem.length; i++) {
			Object[] row = new Object[3];
			row[0] = arrange.get(i)[0];
			row[1] = arrange.get(i)[1];
			row[2] = arrange.get(i)[2];
			
			table_elem[i] = row;
			
		}
		String[] headers = {"搬运地序号", "目的地序号", "数量"};
		table = new JTable(table_elem, headers);
//		table.setBounds(6, 6, 400, 500);
//		add(table);
		
//		System.out.println(table_elem[0][0]);
//		this.validate();
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(100, 0, 600, 480);
		add(scrollPane);
		initialed = true;
	}
	


}
