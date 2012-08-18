package es.jaumesingla.StackCalculator;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Help extends Activity {

	private static final String TAG = "HELP";



	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Creaci�
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		WebView wb=(WebView) findViewById(R.id.webView1);
		wb.setBackgroundColor(0x00000000);
		
		//wb.loadData(this.getHelpText(), "text/html", "UTF-8");
		//wb.loadUrl("file:///android_asset/help.html");
		wb.loadDataWithBaseURL("file:///", this.getHelpText(), "text/html", "UTF-8", null);
	}
	
	public static String getFileContents(AssetManager manager, String path){
		try {
			InputStream file=manager.open(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(file));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
			
		} catch (IOException ex) {
			Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return "";
	}
	
	private String getHelpText() {
		String base=getFileContents(getResources().getAssets(), "help.html");
		Log.d(TAG, base);
		return String.format(base, (Object[]) getResources().getStringArray(R.array.tutorial_texts));
	}



	public void onClickClose(View view){
		this.finish();
	}
}
