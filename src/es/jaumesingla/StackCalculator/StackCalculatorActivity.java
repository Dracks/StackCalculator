package es.jaumesingla.StackCalculator;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
//import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;
import com.google.ads.*;

import es.jaumesingla.StackCalculator.AngleConversor.ConversorInterface;
import es.jaumesingla.StackCalculator.AngleConversor.DegreeConversor;
import es.jaumesingla.StackCalculator.AngleConversor.RadiantConversor;



public class StackCalculatorActivity extends Activity{

	private LinkedList<Double> stack;
	private int index;
	//private double				write;
	private String write;
	private boolean writing;
	private boolean navigation;
	private boolean shifted;
	private int nLines;
	private ConversorInterface conv;
	private PopupWindow pw;
	
	private boolean horizontal;
	private boolean dadesImportades;
	
	private static final String dataName="StackCalculatorData"; 
	private static final float dataVersion=1.0f;
	
	private ResultsViewAdapter showedData;
	
	

	public StackCalculatorActivity() {
		super();
		stack = new LinkedList<Double>();
		index = 0;
		write = "";
		dadesImportades=false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Creaci�
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layouts);
		//this.refreshView();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		//Publicitat
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest();
		// adRequest.addTestDevice("5E63C202A7136485DEFA87461EB95C26");
		
		//adRequest.setTesting(true);
		adView.loadAd(adRequest);
		
		showedData=new ResultsViewAdapter((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		
		
		this.conv = new RadiantConversor();
		//nLines=5;
		
		try {
			if (!dadesImportades){
				dadesImportades=true;
				SharedPreferences settings = getSharedPreferences(dataName, 0);
				if (settings.getFloat("version",dataVersion)==dataVersion){
					int nData=settings.getInt("StackSize", 0);
					
					for (int i=0; i<nData; i++){
						Double d=new Double(settings.getFloat("Row " + Integer.toString(i), 0.0f));
						stack.addLast(d);
						showedData.addItem(d.toString());
					}
					switch (settings.getInt("AngleConversor", 0)) {
						case 0:
							this.conv = new RadiantConversor();
							break;
						case 1:
							this.conv = new DegreeConversor();
							break;
						default:
							this.conv = new RadiantConversor();
					}
				}
			}
		} catch (Exception e){
			
		}
		
	}
	
	@Override
	public void onStop(){
		super.onStop();
		SharedPreferences.Editor settings = getSharedPreferences(dataName, 0).edit();
		settings.putFloat("version", dataVersion);
		settings.putInt("AngleConversor", this.conv.getID());
		settings.putInt("StackSize", this.stack.size());
		for (int i = 0; i < stack.size(); i++) {
			settings.putFloat("Row " + Integer.toString(i), (float) stack.get(i).doubleValue());
		}
		settings.commit();
	}
	
	/*@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		savedInstanceState.putInt("StackSize", this.stack.size());
		for (int i = 0; i < stack.size(); i++) {
			savedInstanceState.putDouble("Row " + Integer.toString(i), stack.get(i).doubleValue());
		}
		savedInstanceState.putInt("AngleConversor", this.conv.getID());
		// etc.
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		int Size = savedInstanceState.getInt("StackSize");
		stack = new LinkedList<Double>();
		
		for (int i = 0; i < Size; i++) {
			Double d=new Double(savedInstanceState.getDouble("Row " + Integer.toString(i)));
			stack.addLast(d);
			showedData.addItem(d.toString());
		}
		switch (savedInstanceState.getInt("AngleConversor")) {
			case 0:
				this.conv = new RadiantConversor();
				break;
			case 1:
				this.conv = new DegreeConversor();
				break;
			default:
				this.conv = new RadiantConversor();
		}
	}*/

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (this.findViewById(R.id.lHorizontal)!=null){
			horizontal=true;
			//Calcul de tamany de la pantalla
			View contentView = this.findViewById(R.id.Contingut);
			int alcadaTotal = contentView.getHeight();
			
			alcadaTotal -= this.findViewById(R.id.linearLayout7).getHeight();
			
			int ampladaTotal = contentView.getWidth()/2;
			//this.findViewById(R.id.Display).setLayoutParams(new LayoutParams(this.findViewById(R.id.Display).getHeight(), alcadaTotal));
			//EditText Display = (EditText) this.findViewById(R.id.Display);
			ListView Display = (ListView) this.findViewById(R.id.DisplayList);
			Display.getLayoutParams().height = alcadaTotal;
			Display.getLayoutParams().width = ampladaTotal;
			//nLines = alcadaTotal / Display.getLineHeight() - 1;
			Display.setAdapter(showedData);
			//Log.d("StackCalculatorActivity", "Stack Size:"+stack.size());
			
			//this.refreshView();
			//*/
			int ampladaOcupada=this.findViewById(R.id.lVertical_buttons).getWidth();
			//int alcadaDisponible=this.findViewById(R.id.lVertical_buttons).getHeight();
			
			alcadaTotal = contentView.getHeight();
			int alcadaOcupada=0;
			alcadaOcupada += this.findViewById(R.id.linearLayout1).getHeight();
			alcadaOcupada += this.findViewById(R.id.linearLayout2).getHeight();
			alcadaOcupada += this.findViewById(R.id.linearLayout3).getHeight();
			alcadaOcupada += this.findViewById(R.id.linearLayout4).getHeight();
			alcadaOcupada += this.findViewById(R.id.linearLayout5).getHeight();
			alcadaOcupada += this.findViewById(R.id.linearLayout6).getHeight();
			//alcadaOcupada += this.findViewById(R.id.linearLayout7).getHeight();
			
			float propX=(float)ampladaTotal/(float)ampladaOcupada;
			float propY=(float)alcadaTotal/(float)alcadaOcupada;
			
			float prop=propX<propY?propX:propY;
			
			LinearLayout obj=(LinearLayout) this.findViewById(R.id.lVertical_buttons);
			
			for (int i=0; i<obj.getChildCount(); i++){
				LinearLayout aux=(LinearLayout) obj.getChildAt(i); //Get  LinearLayoutHorizontal [1..6]
				for (int j=0; j<aux.getChildCount(); j++){
					View aux2=aux.getChildAt(j);
					aux2.getLayoutParams().width*=prop;
					aux2.getLayoutParams().height*=prop;
				}
			}
			

			//Log.i("stackCalculatorActivity", "Al�ada total de:" + Integer.toString(alcadaTotal));
		} else {
			horizontal=false;
			
			//Calcul de tamany de la pantalla
			View contentView = this.findViewById(R.id.Contingut);
			
			int ampladaOcupada=this.findViewById(R.id.linearLayout1).getWidth();
			int ampladaTotal=contentView.getWidth();
			Log.d("Debug1", " D->"+Integer.toString(ampladaOcupada)+" vs "+Integer.toString(ampladaTotal));
			float prop=(float)ampladaTotal/(float)ampladaOcupada;;
			
			LinearLayout obj=(LinearLayout) contentView;
			
			for (int i=1; i<obj.getChildCount()-1; i++){
				LinearLayout aux=(LinearLayout) obj.getChildAt(i); //Get  LinearLayoutHorizontal [1..6]
				for (int j=0; j<aux.getChildCount(); j++){
					View aux2=aux.getChildAt(j);
					aux2.getLayoutParams().width*=prop;
					aux2.getLayoutParams().height*=prop;
				}
			}
			
			int alcadaTotal = contentView.getHeight();
			
			//Log.i("stackCalculatorActivity", "Pre Al�ada total de:" + Integer.toString(alcadaTotal));
			alcadaTotal -= this.findViewById(R.id.linearLayout1).getHeight()*prop;
			alcadaTotal -= this.findViewById(R.id.linearLayout2).getHeight()*prop;
			alcadaTotal -= this.findViewById(R.id.linearLayout3).getHeight()*prop;
			alcadaTotal -= this.findViewById(R.id.linearLayout4).getHeight()*prop;
			alcadaTotal -= this.findViewById(R.id.linearLayout5).getHeight()*prop;
			alcadaTotal -= this.findViewById(R.id.linearLayout6).getHeight()*prop;
			alcadaTotal -= this.findViewById(R.id.linearLayout7).getHeight();
			//this.findViewById(R.id.Display).setLayoutParams(new LayoutParams(this.findViewById(R.id.Display).getHeight(), alcadaTotal));
			//EditText Display = (EditText) this.findViewById(R.id.Display);
			ListView Display = (ListView) this.findViewById(R.id.DisplayList);
			Display.getLayoutParams().height = alcadaTotal;
			Display.setAdapter(showedData);
			//nLines = alcadaTotal / Display.getLineHeight() - 1;
			//Log.i("stackCalculatorActivity", "Al�ada total de:" + Integer.toString(alcadaTotal));
			//this.refreshView();
		}
		//this.refreshView();
		//updateSizeInfo();
	}
	
	private void initiatePopupWindow(int layer, int layout_id, float margin) {
    try {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) StackCalculatorActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout
        View layout = inflater.inflate(layer,
                (ViewGroup) findViewById(layout_id));
        // create a 300px width and 470px height PopupWindow
        //View contentView = this.findViewById(R.id.Contingut);
		//contentView.getHeight();
        
        View contentView = this.findViewById(R.id.Contingut);
		int alcadaTotal = contentView.getHeight();
		int ampladaTotal= contentView.getWidth();
		if (horizontal){
			ampladaTotal=(2*alcadaTotal)/3;
		}
        pw = new PopupWindow(layout, ampladaTotal-(int)( ampladaTotal*margin), alcadaTotal-(int)(alcadaTotal*margin), true);
        
        //pw.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // display the popup in the center
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
        
       /* WebView wv=(WebView)layout.findViewById(R.id.webView1);
        if (wv!=null){
	        String data = "<html><body style='text-align:justify; background: rgba(0,0,0,0)'>" +
	        		"<p>" + getString(R.string.tutorial_text_1)+ "</p>" +
	        		"<p>" + getString(R.string.tutorial_text_2)+ "</p>" +
	        		"<p>" + getString(R.string.tutorial_text_3)+ "</p>" +
	        		"<p>" + getString(R.string.tutorial_text_4)+ "</p>" +
	        		"</body></html>";
	        wv.loadData(data, "text/html", "UTF-8");
	        
        }*/
		
		//layout.setOnKeyListener(this);
		//layout.setOnTouchListener(this);
		
 
    } catch (Exception e) {
        e.printStackTrace();
    }
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.base_options, menu);

		menu.getItem(0).getSubMenu().setGroupCheckable(menu.getItem(0).getSubMenu().getItem(0).getGroupId(), true, true);
		//menu.getItem(0).getSubMenu().getItem(0).setChecked(0 == conv.getID());
		//.getItem(0).getSubMenu().getItem(1).setChecked(1 == conv.getID());
		
		switch (conv.getID()){
			case 0:
				menu.findItem(R.id.oRad).setChecked(true);
			break;
			case 1:
				menu.findItem(R.id.oDegree).setChecked(true);
			break;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		//Log.d("StackCalculatorACtivity", "onOptionsItemSelected " + Integer.toString(item.getItemId()));
		switch (item.getItemId()) {
			case R.id.oConfig:
				item.getSubMenu().setGroupCheckable(R.id.gAngle, true, true);
				return true;
			case R.id.oDegree:
				//Log.d("StackCalculatorActivity", "oDegree");
				item.setChecked(true);
				conv=new DegreeConversor();
				return true;
			case R.id.oRad:
				//Log.d("StackCalculatorActivity", "oRad");
				item.setChecked(true);
				conv=new RadiantConversor();
				return true;
			case R.id.oAbout:
				this.initiatePopupWindow(R.layout.about, R.id.AboutContents, 0.25f);
				return true;
			case R.id.oHelp:
				//this.initiatePopupWindow(R.layout.help, R.id.lHelp, 0.0f);
				
				Intent intentMain = new Intent(this, Help.class);
	            this.startActivity(intentMain);
	            Log.i("Content "," Main layout ");
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public ConversorInterface getConversor() {
		return conv;
	}

	public void setConversor(ConversorInterface conv) {
		this.conv = conv;
	}


	public void addValue(String c) {
		//Log.d("StackCalculatorActivity", write);
		//listData.addItem(c);
		String tmp = write + c;
		try {
			//@SuppressWarnings("unused")
			Double.valueOf(tmp);
			
			write = tmp;
			writing = true;
			showedData.setNewValue(write);
			((ListView) this.findViewById(R.id.DisplayList)).smoothScrollToPosition(0);
			//this.refreshView();
		} catch (Exception e) {
			Log.d("StackCalculatorActivity", tmp);
			Log.d("StackCalculatorActivity", e.toString());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e) {
		//Log.d("StackCalculatorActivity", "KeyCode:" + Integer.toString(keyCode));
		if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 || keyCode == KeyEvent.KEYCODE_2
				|| keyCode == KeyEvent.KEYCODE_3 || keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5
				|| keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 || keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9) {
			this.addValue(Integer.toString(keyCode - KeyEvent.KEYCODE_0));
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_PERIOD) {
			this.addValue(".");
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (writing) {
				if (!write.equals("")) {
					stack.addFirst(new Double(write));
					showedData.pushNewValue();
				}
				write = "";
				showedData.setNewValue(null);
			} /*else {
				if (stack.size() > 0) {
					stack.addFirst(stack.get(index));
					index = 0;
				}
			}*/
			writing = false;
			navigation = false;
			//this.refreshView();
			return true;
		} else if (keyCode==KeyEvent.KEYCODE_BACK){
			if (pw!=null)
				pw.dismiss();
			else 
				return super.onKeyDown(keyCode, e);
		}
		return false;
	}

	public void onClickButton(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");

	}

	public void onClickShift(View view) {
		shifted = !shifted;
		if (shifted) {
			//((Button) findViewById(R.id.bShift)).setText("Unshift");
			//((Button) findViewById(R.id.bDiv)).setText("1/x");
			((Button) findViewById(R.id.bSub)).setText("-x");
			if (findViewById(R.id.bSin)!=null){
				((Button) findViewById(R.id.bSin)).setText("Arcsin");
				((Button) findViewById(R.id.bCos)).setText("Arccos");
				((Button) findViewById(R.id.bTan)).setText("Arctan");
			}
			((Button) findViewById(R.id.bLn)).setText("Log");
			((Button) findViewById(R.id.bSqrt)).setText("x^2");
			((Button) findViewById(R.id.bEx)).setText("x^y");
		} else {
			//((Button) findViewById(R.id.bShift)).setText("Shift");
			//((Button) findViewById(R.id.bDiv)).setText("/");
			((Button) findViewById(R.id.bSub)).setText("-");
			if (findViewById(R.id.bSin)!=null){
				((Button) findViewById(R.id.bSin)).setText("Sin");
				((Button) findViewById(R.id.bCos)).setText("Cos");
				((Button) findViewById(R.id.bTan)).setText("Tan");
			}
			((Button) findViewById(R.id.bLn)).setText("Ln");
			((Button) findViewById(R.id.bSqrt)).setText("�x");
			((Button) findViewById(R.id.bEx)).setText("e^y");
		}
	}

	public void onClickUp(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		/*if (!writing) {
			if (!navigation) {
				navigation = true;
			} else if (stack.size() > index + 1) {
				index++;
			}
			//this.refreshView();

			Button back = (Button) this.findViewById(R.id.bDel);
			back.setText("Cancel");
		}*/

	}

	public void onClickDown(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		/*if (!writing) {
			if (index > 0) {
				index--;
			}
			//this.refreshView();

			Button back = (Button) this.findViewById(R.id.bDel);
			back.setText("Cancel");
		}*/

	}

	public void onClickEnter(View view) {
		if (writing) {
			if (!write.equals("")) {
				stack.addFirst(new Double(write));
				showedData.pushNewValue();
			}
			write = "";
			showedData.setNewValue(null);
		}/* else {
			if (stack.size() > 0) {

				stack.addFirst(stack.get(index));
				index = 0;
				if (navigation) {
					Button back = (Button) this.findViewById(R.id.bDel);
					back.setText("Delete");
				}
			}
		}*/
		writing = false;
		navigation = false;
		//this.refreshView();
	}

	public void onClickDel(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (writing) {
			if (write.length()>0){
				write = write.substring(0, write.length() - 1);
				if (write.length()==0){
					Button back = (Button) this.findViewById(R.id.bDel);
					back.setText("Cancel");
				}
				showedData.setNewValue(write);
			} else {
				Button back = (Button) this.findViewById(R.id.bDel);
				back.setText("Delete");
				writing=false;
			}
		} else if (!navigation) {
			if (stack.size() > 0) {
				stack.removeFirst();
				showedData.removeItem(0);
			}
		} else if (navigation) {
			navigation = false;
			index = 0;
			Button back = (Button) this.findViewById(R.id.bDel);
			back.setText("Delete");
		}
		//this.refreshView();
	}

	public void onClickChar(View view) {
		//Log.d("StackCalculatorActivity", ((Button) view).getText().toString());
		if (!navigation){
			this.addValue(((Button) view).getText().toString());
		}

	}

	public void onClickAdd(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (stack.size() >= 2 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=new Double(a + b);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}

	public void onClickSub(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (shifted && stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			
			Double d=new Double(-a);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		} else if (stack.size() >= 2 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=new Double(b - a);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		} else if (writing) {
			if (Double.valueOf(write) < 0) {
				write = write.substring(1);
			} else {
				write = "-" + write;
			}
			showedData.setNewValue(write);
		}
		//this.refreshView();

	}

	public void onClickMul(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (stack.size() >= 2 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=new Double(a * b);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}

	public void onClickDiv(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (stack.size() >= 2 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=new Double(b / a);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}
	
	public void onClickInv(View view){
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=new Double(1 / a);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}

	public void onClickSin(View view) {
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			Double d;
			showedData.removeItem(0);
			if (!shifted) {
				d=new Double(Math.sin(conv.toProcess(a)));
			} else {
				d=new Double(conv.toShow(Math.asin(a)));
			}
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();

	}

	public void onClickCos(View view) {
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d;
			if (!shifted) {
				d=new Double(Math.cos(conv.toProcess(a)));
			} else {
				d=new Double(conv.toShow(Math.acos(a)));
			}
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}

	public void onClickTan(View view) {
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d;
			if (!shifted) {
				d=new Double(Math.tan(conv.toProcess(a)));
			} else {
				d=new Double(conv.toShow(Math.atan(a)));
			}
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}
	
	public void onClickSqrt(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d;
			if (shifted){
				d=new Double(Math.pow(a,2.0f));
			} else {
				d=new Double(Math.sqrt(a));
			}
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}

	public void onClickIntegralEx(View view) {
		if (shifted){
			if (stack.size() >= 2 && !writing && !navigation) {
				double a = stack.getFirst().doubleValue();
				stack.removeFirst();
				showedData.removeItem(0);
				double b = stack.getFirst().doubleValue();
				stack.removeFirst();
				showedData.removeItem(0);
				//if (!shifted)
				Double d=new Double(Math.pow(b, a));
				stack.addFirst(d);
				showedData.addFirstItem(d.toString());
			}
		} else{ 
			if (stack.size() >= 1 && !writing && !navigation) {
				double a = stack.getFirst().doubleValue();
				stack.removeFirst();
				showedData.removeItem(0);
				//if (!shifted)
				Double d=new Double(Math.pow(Math.E, a));
				stack.addFirst(d);
				showedData.addFirstItem(d.toString());
			}
		}
		//this.refreshView();
	}

	public void onClickLn(View view) {
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d;
			if (shifted) {
				d=new Double(Math.log10(a));
			} else {
				d=new Double(Math.log(a));
			}
			
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}

	public void onClickPi(View view) {
		if (!writing && !navigation) {
			Double d=new Double(Math.PI);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());
		}
		//this.refreshView();
	}
	
	public void onClickClose(View view) {
		pw.dismiss();

	}
	
	public void onClickElement(AdapterView adapterView, View view, int position, long id){
		
		int selectedPosition = adapterView.getSelectedItemPosition();
          ShowAlert(String.valueOf(selectedPosition));
	}
	
	/*public void onClick(View view){
	Log.d("StackCalculatorActivity", "holamon-Button");
	
	}//*/

	private void ShowAlert(String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
			// set title
			alertDialogBuilder.setTitle("Your Title");
 
			// set dialog message
			alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						StackCalculatorActivity.this.finish();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			
	}
}
