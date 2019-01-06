package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.alibaba.fastjson.JSON;

import models.ComparableWithIndex;

public class ManageClsDetailPanel extends JPanel {
	private JTable table;
	private OrdersManager ordersManager = OrdersManager.getInstance();
	private boolean initialed = false;

	/**
	 * Create the panel.
	 */
	public ManageClsDetailPanel() {
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
		int[] assignment = ordersManager.cluster_with_info();
		int [] depart = ordersManager.getDepartNum();
		int[] arri = ordersManager.getArrivNum();
		double[][] center = ordersManager.getClusterCenter();
		
		ArrayList<ComparableWithIndex> arr_dep= new ArrayList<>();
		for (int i = 0; i < 193; i++) {
			arr_dep.add(new ComparableWithIndex(depart[i], i));
		}
		
		Collections.sort(arr_dep);
		
		Object[][] table_elem = new Object[193][4];
		
		for (int i = 0; i < table_elem.length; i++) {
			int index = arr_dep.get(i).getIndex();
			Object[] row = new Object[4];
			row[0] = Integer.toString(i+1);
			row[1] = Integer.toString(depart[index]);
			row[2] = Integer.toString(arri[index]);
			row[3] =  String.format("%.4f,%.4f", center[index][0],center[index][1]);
			table_elem[i] = row;
			
		}
		String[] headers = {"簇序号", "解锁人数", "停车人数", "位置信息"};
		table = new JTable(table_elem, headers);
//		table.setBounds(6, 6, 400, 500);
//		add(table);
		
//		System.out.println(table_elem[0][0]);
//		this.validate();
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(100, 0, 600, 480);
		add(scrollPane);
		initialed = true;
		
		ArrayList<String> arrayList = new ArrayList<>();
		for (int i = 0; i < table_elem.length; i++) {
			arrayList.add((String) table_elem[i][3]);
		}
		

		
		try {
			FileWriter fw;
			fw = new FileWriter("./src/clus_loca.txt", false);
			BufferedWriter bf = new BufferedWriter(fw);
			
//			bf.write("var cluster_id =");
			bf.write(JSON.toJSONString(arrayList));
//			bf.write(";");
			bf.newLine();
			bf.flush();
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	


	public void creatTabel1()
	{
		
		String[] columnNames = { "First Name", "Last Name", "Sport",
				                 "# of Years", "Vegetarian" };
				  
				          //创建显示数据
				          Object[][] data = {
				                  { "Kathy", "Smith", "Snowboarding", new Integer(5),
				                          new Boolean(false) },
				                  { "John", "Doe", "Rowing", new Integer(3), new Boolean(true) },
				                  { "Sue", "Black", "Knitting", new Integer(2),
				                          new Boolean(false) },
				                  { "Jane", "White", "Speed reading", new Integer(20),
				                          new Boolean(true) },
				                  { "Joe", "Brown", "Pool", new Integer(10), new Boolean(false) } };
				         
				          /*
				           * JTable还提供了一个重载的构造方法,传入两个Vector
				           * JTable(Vector rowData, Vector columnNames)
				           * 
				           */
				         
				         table = new JTable(data, columnNames);
				         table.setBounds(6, 6, 400, 500);
				 		add(table);
	}

}
