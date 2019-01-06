package application;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import models.BasicOrderStatData;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StatBasicPanel extends JPanel {
	private OrdersManager ordersManager = OrdersManager.getInstance();
	private BasicOrderStatData basic_data;
	private JLabel lblDate;
	private JLabel lblOrder;
	private JLabel lblDuration;
	private JLabel lblDistance;
	private JLabel lblBike;
	private JLabel lblUser;

	/**
	 * Create the panel.
	 */
	public StatBasicPanel() {
//		basic_data = ordersManager.calcu_basic_stat();
		
		setBounds(0, 0, 800, 500);
		setLayout(null);
		
		JLabel label_5 = new JLabel("日期：");
		label_5.setBounds(58, 58, 61, 16);
		add(label_5);
		
		JLabel label_6 = new JLabel("订单数量：");
		label_6.setBounds(58, 121, 83, 16);
		add(label_6);
		
		JLabel label_1 = new JLabel("平均骑行时间：");
		label_1.setBounds(58, 184, 97, 16);
		add(label_1);
		
		JLabel label_2 = new JLabel("平均骑行距离：");
		label_2.setBounds(58, 252, 97, 16);
		add(label_2);
		
		JLabel label_3 = new JLabel("车辆日均被使用次数：");
		label_3.setBounds(58, 317, 130, 16);
		add(label_3);
		
		JLabel label_4 = new JLabel("用户日均使用次数：");
		label_4.setBounds(58, 382, 120, 16);
		add(label_4);
		
		lblDate = new JLabel("2017-06-01");
		lblDate.setBounds(106, 58, 97, 16);
		add(lblDate);
		
		lblOrder = new JLabel("100000");
		lblOrder.setBounds(127, 121, 139, 16);
		add(lblOrder);
		
		lblDuration = new JLabel("120s");
		lblDuration.setBounds(156, 184, 139, 16);
		add(lblDuration);
		
		lblDistance = new JLabel("300m");
		lblDistance.setBounds(156, 252, 152, 16);
		add(lblDistance);
		
		lblBike = new JLabel("19次");
		lblBike.setBounds(200, 317, 139, 16);
		add(lblBike);
		
		lblUser = new JLabel("1.2次");
		lblUser.setBounds(188, 382, 120, 16);
		add(lblUser);
		
		JButton button = new JButton("更新");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				basic_data = ordersManager.calcu_basic_stat();
				update_labels();
			}
		});
		button.setBounds(611, 377, 117, 29);
		add(button);
		
		
//		update_labels();
	}
	
	public void update_labels() {
		lblDate.setText(basic_data.date);
		lblOrder.setText(basic_data.ordersNum+"");
		lblDuration.setText(String.format("%.2f", basic_data.average_duration) +"s");
		lblDistance.setText(String.format("%.2f", basic_data.average_distance)+"m");
		lblBike.setText(String.format("%.4f", basic_data.average_bike_using_times)+"次");
		lblUser.setText(String.format("%.4f", basic_data.average_user_times)+"次");
	}
	
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JFrame frame = new JFrame();
            	frame.setSize(800, 500);
            	frame.setVisible(true);
            	StatBasicPanel statBasicPanel = new StatBasicPanel();
            	frame.getContentPane().add(statBasicPanel);
            	
            }
        });
	}
}
