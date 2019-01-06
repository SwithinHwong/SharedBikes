package application;



import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.web.WebView;

public class StatTimePanel extends JFXPanel {
	private OrdersManager ordersManager = OrdersManager.getInstance();

	/**
	 * Create the panel.
	 */
	public StatTimePanel() {
		createScene();
	}
	
	


    public void createScene() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	int[] counts = ordersManager.countOrdersPreHour();

            	final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
//                xAxis.setLabel("Month");       
                
                final LineChart<String,Number> lineChart = 
                        new LineChart<String,Number>(xAxis,yAxis);
                        
                lineChart.setTitle("每小时用户使用数量");
                                        
                XYChart.Series<String, Number> series = new XYChart.Series();
                series.setName("使用次数");
                
                for (int j = 0; j < counts.length; j++) {
                	series.getData().add(new XYChart.Data<String, Number>(j+"", counts[j]));
				}
                
                
//                
//                String[] labels = new String[24];
//                for (int i = 0; i < labels.length; i++) {
//					labels[i] = i+"";
//				}
//                Random random = new Random();
//                for (int j = 0; j < labels.length; j++) {
//                	series.getData().add(new XYChart.Data<String, Number>(labels[j], random.nextInt(100)));
//				}
                
//                series.getData().add(new XYChart.Data("0:00-1:00", 23));
//                series.getData().add(new XYChart.Data("1:00-2:00", 14));
//                series.getData().add(new XYChart.Data("Mar", 15));
//                series.getData().add(new XYChart.Data("Apr", 24));
//                series.getData().add(new XYChart.Data("May", 34));
//                series.getData().add(new XYChart.Data("Jun", 36));
//                series.getData().add(new XYChart.Data("Jul", 22));
//                series.getData().add(new XYChart.Data("Aug", 45));
//                series.getData().add(new XYChart.Data("Sep", 43));
//                series.getData().add(new XYChart.Data("Oct", 17));
//                series.getData().add(new XYChart.Data("Nov", 29));
//                series.getData().add(new XYChart.Data("Dec", 25));
                
                Scene scene  = new Scene(lineChart);
                lineChart.getData().add(series);
                
               
                setScene(scene);
                
            }
        });
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JFrame frame = new JFrame();
            	frame.setSize(800, 500);
            	frame.setVisible(true);
            	StatTimePanel statTimePanel = new StatTimePanel();
            	frame.getContentPane().add(statTimePanel);
            	statTimePanel.setVisible(true);
            	
            }
        });
    }
}
