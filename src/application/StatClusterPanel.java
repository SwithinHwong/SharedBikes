package application;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StatClusterPanel extends JPanel {

	private BrowserPanel browserPanel;
	/**
	 * Create the panel.
	 */
	public StatClusterPanel() {
		setBounds(0,0,800,500);
		
		browserPanel = new BrowserPanel();
		browserPanel.setBounds(0, 0, 800, 500);
		browserPanel.loadLocalPage("./src/Amap_huge_clustered.html");
		browserPanel.setVisible(true);
		setLayout(null);
		add(browserPanel);
	}
	
	public void reload() {
		browserPanel.reload();
	}
	


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JFrame frame = new JFrame();
            	frame.setSize(800, 600);
            	frame.setVisible(true);
            	StatClusterPanel demonstratePanel;
				demonstratePanel = new StatClusterPanel();
				frame.getContentPane().add(demonstratePanel);
				
				
				
            	
            }
        });
    }
}
