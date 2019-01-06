package application;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

import javafx.scene.web.WebEngine;

import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class DemonstratePanel extends JPanel {
	private OrdersManager ordersManager = OrdersManager.getInstance();
	private int beginTime = 0;
	private int endTime = 96;
 	private BrowserPanel browserPanel;
	private JTextField startTF;
	private JTextField endTF;
	private JSlider start_slider;
	private JSlider end_slider;
	private JButton button;
	/**
	 * Create the panel.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public DemonstratePanel() {
		setBounds(new Rectangle(0, 0, 800, 600));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0};
		gridBagLayout.rowHeights = new int[] {100, 500};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0};
		setLayout(gridBagLayout);
		
		JPanel settings = new JPanel();
		settings.setLayout(null);
		GridBagConstraints gbc_settings = new GridBagConstraints();
		gbc_settings.insets = new Insets(0, 0, 5, 0);
		gbc_settings.fill = GridBagConstraints.BOTH;
		gbc_settings.gridx = 0;
		gbc_settings.gridy = 0;
		add(settings, gbc_settings);
		
		endTF = new JTextField();
		endTF.setText("24:00");
		endTF.setColumns(10);
		endTF.setBounds(563, 50, 70, 21);
		settings.add(endTF);
		
		startTF = new JTextField();
		startTF.setText("00:00");
		startTF.setBounds(563, 20, 70, 21);
		settings.add(startTF);
		startTF.setColumns(10);
		
		JLabel label = new JLabel("起始时间");
		label.setBounds(44, 23, 61, 16);
		settings.add(label);
		
		JLabel label_1 = new JLabel("终止时间");
		label_1.setBounds(44, 53, 61, 16);
		settings.add(label_1);
		
		start_slider = new JSlider();
		start_slider.setSnapToTicks(false);
		start_slider.setMinorTickSpacing(2);
		start_slider.setMajorTickSpacing(4);
		start_slider.setMaximum(96);
		start_slider.setValue(0);
		start_slider.setPaintTicks(true);
		start_slider.setBounds(95, 17, 456, 28);
		start_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int value = ((JSlider) e.getSource()).getValue();
//				changeBeginTime(((double)value)/4.0);
				changeBeginTime(value);
				
				
			}
		});
		settings.add(start_slider);
		
		end_slider = new JSlider();
		end_slider.setSnapToTicks(false);
		end_slider.setValue(96);
		end_slider.setPaintTicks(true);
		end_slider.setMinorTickSpacing(2);
		end_slider.setMaximum(96);
		end_slider.setMajorTickSpacing(4);
		end_slider.setBounds(95, 51, 456, 28);
		end_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int value = ((JSlider) e.getSource()).getValue();
//				changeEndTime(((double)value)/4.0);
				changeEndTime(value);
			}
		});
		settings.add(end_slider);
		
		button = new JButton("绘制地图");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//解析时间，选择
				try {
					ordersManager.selectTimeScope(startTF.getText(), endTF.getText());
				} catch (ParseException e2) {
					JOptionPane.showMessageDialog(null, "输入时间不正确！","时间解析错误" , JOptionPane.ERROR_MESSAGE);
					e2.printStackTrace();
				}
				
				//将选中的位置写入location.js，刷新浏览器
				try {
					ordersManager.writeSelected2JS();
					browserPanel.reload();
//					JOptionPane.showMessageDialog(null, "ok！");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "写入时出现IO错误！");
				}
				
			}
		});
		button.setBounds(658, 25, 118, 46);
		settings.add(button);
		
		JPanel map_panel = new JPanel();
		GridBagConstraints gbc_map_panel = new GridBagConstraints();
		gbc_map_panel.fill = GridBagConstraints.BOTH;
		gbc_map_panel.gridx = 0;
		gbc_map_panel.gridy = 1;
		add(map_panel, gbc_map_panel);
		
		browserPanel = new BrowserPanel();
		browserPanel.loadLocalPage("./src/Amap_huge_amount_points_order.html");
		browserPanel.setVisible(true);
		map_panel.add(browserPanel);
		
//		try {
//			ordersManager.loadOrderFile("./src/orders/small_orders.txt");
//		} catch (FileNotFoundException e1) {
//			JOptionPane.showMessageDialog(null, "找不到文件！","文件载入错误" , JOptionPane.ERROR_MESSAGE);
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			JOptionPane.showMessageDialog(null, "读写错误","文件载入错误" , JOptionPane.ERROR_MESSAGE);
//			e1.printStackTrace();
//		}
	}
	
	private String timeString(int time_4)
	{
		double time = time_4/4.0;
		time = time < 0 ? 0 : (time > 24 ? 24 : time);
		int hour = (int) time;
		int min = (int) ((time-hour)/0.25*15);
		return String.format("%02d", hour)+":"+String.format("%02d", min);
		
	}
	
	private void changeBeginTime(int beginTime)
	{
		//beginTime是0到96的小数
		System.out.println("origin begin time:"+Double.toString(endTime));
		beginTime = beginTime < 0 ? 0 : (beginTime > 95 ? 95 : beginTime);
		this.beginTime = beginTime;
		System.out.println("Modified begin time:"+Integer.toString(endTime));
		

		if (endTime-beginTime<1) {
			endTime = beginTime+1;
			changeEndTime(endTime);
		}
		
//		start_slider.setValue((int)(beginTime)*4);
		start_slider.setValue(beginTime);
		startTF.setText(timeString(beginTime));
		
		
		
	}
	
	private void changeEndTime(int endTime)
	{
		//endTime是0到96的小数
//		System.out.println("origin end time:"+Integer.toString(endTime));
		endTime = endTime < 1 ? 1 : (endTime > 96 ? 96 : endTime);
		this.endTime = endTime;
//		System.out.println("Modified end time:"+Integer.toString(endTime));
		

		if (endTime-beginTime<1) {
			beginTime = endTime-1;
			changeBeginTime(beginTime);
		}
		
//		end_slider.setValue((int)endTime*4);
		end_slider.setValue(endTime);
		endTF.setText(timeString(endTime));
		
		
		
	}
	

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JFrame frame = new JFrame();
            	frame.setSize(800, 600);
            	frame.setVisible(true);
            	DemonstratePanel demonstratePanel;
				demonstratePanel = new DemonstratePanel();
				frame.getContentPane().add(demonstratePanel);
				
				
				
            	
            }
        });
    }
}
