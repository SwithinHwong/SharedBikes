package application;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class UserInterface {

	private JFrame frame;
	private CardLayout main_cardLayout;
	private JPanel main_card_panel;
	private OrdersManager ordersManager = OrdersManager.getInstance();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface window = new UserInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserInterface() {
		initialize();
		loadDefaultFile();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {100, 800};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JPanel navigator_panel = new JPanel();
		navigator_panel.setBackground(new Color(68, 68, 68));
		navigator_panel.setLayout(null);
		GridBagConstraints gbc_navigator_panel = new GridBagConstraints();
		gbc_navigator_panel.insets = new Insets(0, 0, 0, 5);
		gbc_navigator_panel.fill = GridBagConstraints.BOTH;
		gbc_navigator_panel.gridx = 0;
		gbc_navigator_panel.gridy = 0;
		frame.getContentPane().add(navigator_panel, gbc_navigator_panel);
		
		JButton button = new JButton("显示订单");
		button.setBackground(Color.BLUE);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main_cardLayout.show(main_card_panel,"demonstrate"); 
				main_card_panel.validate();
			}
		});
		button.setBounds(12, 30, 70, 70);
		navigator_panel.add(button);
		
		JButton button_1 = new JButton("订单统计");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main_cardLayout.show(main_card_panel,"statistics"); 
				main_card_panel.validate();
			}
		});
		button_1.setBounds(12, 120, 70, 70);
		navigator_panel.add(button_1);
		
		JButton button_2 = new JButton("规划管理");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main_cardLayout.show(main_card_panel,"manage"); 
				main_card_panel.validate();
			}
		});
		button_2.setBounds(12, 210, 70, 70);
		navigator_panel.add(button_2);
		
		main_card_panel = new JPanel();
		GridBagConstraints gbc_main_card_panel = new GridBagConstraints();
		gbc_main_card_panel.fill = GridBagConstraints.BOTH;
		gbc_main_card_panel.gridx = 1;
		gbc_main_card_panel.gridy = 0;
		frame.getContentPane().add(main_card_panel, gbc_main_card_panel);
		
		main_cardLayout = new CardLayout(0,0);
		main_card_panel.setLayout(main_cardLayout);
		
		JPanel demonstratePanel = new DemonstratePanel();
		main_card_panel.add(demonstratePanel, "demonstrate");
		
		JPanel statisticsPanel = new StatisticsPanel();
		main_card_panel.add(statisticsPanel,"statistics");
		
		JPanel managementPanel = new ManagementPanel();
		main_card_panel.add(managementPanel,"manage");
		
		
		
		
	}
	
	public void loadDefaultFile() {
		try {
			ordersManager.loadOrderFile("./src/orders/orders.txt");
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "找不到文件！","文件载入错误" , JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "读写错误","文件载入错误" , JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
}
