package application;

import static javafx.concurrent.Worker.State.FAILED;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

public class BrowserPanel extends JFXPanel {
	private WebEngine engine;
	
	
	public BrowserPanel() {
		createScene();
	}
	

    private void createScene() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                WebView view = new WebView();
                engine = view.getEngine();

                setScene(new Scene(view));
            }
        });
    }
    
    public void reload() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                engine.reload();
            }
        });
    }
    
    public void loadLocalPage(String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                engine.load(new File (url).toURI ().toString () );
            }
        });
    }

    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String tmp = toURL(url);

                if (tmp == null) {
                    tmp = toURL("http://" + url);
                }

                engine.load(tmp);
            }
        });
    }
    

    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JFrame frame = new JFrame();
            	frame.setSize(900, 600);
            	frame.setVisible(true);
            	BrowserPanel browserPanel = new BrowserPanel();
            	frame.getContentPane().add(browserPanel);
            	browserPanel.setVisible(true);
//            	browserPanel.loadURL("http://www.baidu.com");
            	browserPanel.loadLocalPage("./src/Amap_huge_amount_points_order.html");
            }
        });
    }

}
