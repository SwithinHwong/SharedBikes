package application;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StatisticsPanel extends JPanel {
	
	private JPanel card_panel;
	/**
	 * Create the panel.
	 */
	public StatisticsPanel() {
		setBounds(new Rectangle(0, 0, 800, 600));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0};
		gridBagLayout.rowHeights = new int[] {100, 500};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0};
		setLayout(gridBagLayout);
		

		card_panel = new JPanel();
		GridBagConstraints gbc_card_panel = new GridBagConstraints();
		gbc_card_panel.fill = GridBagConstraints.BOTH;
		gbc_card_panel.gridx = 0;
		gbc_card_panel.gridy = 1;
		add(card_panel, gbc_card_panel);
		card_panel.setLayout(new CardLayout(0, 0));
		
		StatBasicPanel statBasicPanel = new StatBasicPanel();
		card_panel.add(statBasicPanel, "basic");
		
		StatClusterPanel statClusterPanel = new StatClusterPanel();
		card_panel.add(statClusterPanel, "clustering");
		
		StatTimePanel statTimePanel = new StatTimePanel();
		card_panel.add(statTimePanel, "time");
		
		
		JPanel settings = new JPanel();
		settings.setLayout(null);
		GridBagConstraints gbc_settings = new GridBagConstraints();
		gbc_settings.insets = new Insets(0, 0, 5, 0);
		gbc_settings.fill = GridBagConstraints.BOTH;
		gbc_settings.gridx = 0;
		gbc_settings.gridy = 0;
		add(settings, gbc_settings);
		
		JButton button = new JButton("基本信息");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout) card_panel.getLayout()).show(card_panel, "basic");
			}
		});
		button.setBounds(45, 33, 96, 29);
		settings.add(button);
		
		JButton button_1 = new JButton("订单聚类");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout) card_panel.getLayout()).show(card_panel, "clustering");
				statClusterPanel.reload();
			}
		});
		button_1.setBounds(157, 33, 117, 29);
		settings.add(button_1);
		
		JButton button_2 = new JButton("时间分布");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statTimePanel.createScene();
				((CardLayout) card_panel.getLayout()).show(card_panel, "time");
			}
		});
		button_2.setBounds(302, 33, 117, 29);
		settings.add(button_2);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(SystemColor.textInactiveText);
		separator.setBounds(0, 79, 800, 5);
		settings.add(separator);
		

	}
	

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JFrame frame = new JFrame();
            	frame.setSize(800, 600);
            	frame.setVisible(true);
            	StatisticsPanel statisticsPanel = new StatisticsPanel();
            	frame.getContentPane().add(statisticsPanel);
            	
            }
        });
    }


}
