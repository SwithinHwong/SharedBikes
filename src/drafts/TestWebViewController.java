package drafts;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class TestWebViewController implements Initializable
{
	@FXML
	private WebView myWebView;

	public TestWebViewController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		WebEngine engine = myWebView.getEngine();
//		engine.load("./src/Amap_huge_amount_points_order.html");
		engine.load(new File ( "./src/Amap_huge_amount_points_order.html" ).toURI ().toString () );
		
	}

}
