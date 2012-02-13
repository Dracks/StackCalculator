package es.jaumesingla.StackCalculator;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
//import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;


import android.widget.PopupWindow;
import com.google.ads.*;

import es.jaumesingla.StackCalculator.AngleConversor.ConversorInterface;
import es.jaumesingla.StackCalculator.AngleConversor.DegreeConversor;
import es.jaumesingla.StackCalculator.AngleConversor.RadiantConversor;

public class StackCalculatorActivity extends Activity {

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

	public StackCalculatorActivity() {
		stack = new LinkedList<Double>();
		index = 0;
		write = "";
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Creaciï¿½
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layouts);
		//this.refreshView();

		//Publicitat
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice("38309AC626B900EC");
		// adRequest.setTestDevices("953C629AF51EC113C8C153493876C11F");
		adRequest.setTesting(true);
		adView.loadAd(adRequest);

		this.conv = new RadiantConversor();
		//nLines=5;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (this.findViewById(R.id.lVertical)!=null){
			horizontal=true;
			//Calcul de tamany de la pantalla
			View contentView = this.findViewById(R.id.Contingut);
			int alcadaTotal = contentView.getHeight();
			
			alcadaTotal -= this.findViewById(R.id.linearLayout7).getHeight();
			
			int ampladaTotal = contentView.getWidth();
			//this.findViewById(R.id.Display).setLayoutParams(new LayoutParams(this.findViewById(R.id.Display).getHeight(), alcadaTotal));
			EditText Display = (EditText) this.findViewById(R.id.Display);
			Display.getLayoutParams().height = alcadaTotal;
			Display.getLayoutParams().width = ampladaTotal/2;
			nLines = alcadaTotal / Display.getLineHeight() - 1;
			//Log.i("stackCalculatorActivity", "Alï¿½ada total de:" + Integer.toString(alcadaTotal));
		} else {
			horizontal=false;
			//Calcul de tamany de la pantalla
			View contentView = this.findViewById(R.id.Contingut);
			int alcadaTotal = contentView.getHeight();
			//Log.i("stackCalculatorActivity", "Pre Alï¿½ada total de:" + Integer.toString(alcadaTotal));
			alcadaTotal -= this.findViewById(R.id.linearLayout1).getHeight();
			alcadaTotal -= this.findViewById(R.id.linearLayout2).getHeight();
			alcadaTotal -= this.findViewById(R.id.linearLayout3).getHeight();
			alcadaTotal -= this.findViewById(R.id.linearLayout4).getHeight();
			alcadaTotal -= this.findViewById(R.id.linearLayout5).getHeight();
			alcadaTotal -= this.findViewById(R.id.linearLayout6).getHeight();
			alcadaTotal -= this.findViewById(R.id.linearLayout7).getHeight();
			//this.findViewById(R.id.Display).setLayoutParams(new LayoutParams(this.findViewById(R.id.Display).getHeight(), alcadaTotal));
			EditText Display = (EditText) this.findViewById(R.id.Display);
			Display.getLayoutParams().height = alcadaTotal;
			nLines = alcadaTotal / Display.getLineHeight() - 1;
			//Log.i("stackCalculatorActivity", "Alï¿½ada total de:" + Integer.toString(alcadaTotal));
		}
		this.refreshView();
		//updateSizeInfo();
	}

	@Override
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
		for (int i = 0; i < Size; i++) {
			stack.addLast(new Double(savedInstanceState.getDouble("Row " + Integer.toString(i))));
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

	private String getText(int ini, int end, int index) {
		String show = "";
		for (int i = ini; i < end; i++) {
			if (i < stack.size()) {
				char c = ' ';
				if (i != index) {
					c = ':';
				} else {
					c = '<';
				}
				show = stack.get(i).toString() + c + Integer.toString(i + 1) + show;
			} else {
				show = ":" + Integer.toString(i + 1) + show;
			}
			if (i < end - 1) {
				show = "\n" + show;
			}
		}
		return show;
	}

	public void refreshView() {
		EditText Display = (EditText) findViewById(R.id.Display);
		String show = this.getText(0, nLines, index - 1);
		//int i;
		int impar = (nLines) % 2;
		//int par=(nLines+1)%2;
		if (stack.size() > 0) {
			if (navigation) {
				int ini, end;
				ini = 0;
				end = nLines;
				if (index > (nLines / 2)) {
					if (index < stack.size() - (nLines / 2 + impar)) {
						ini = index - (nLines / 2);
						end = index + (nLines / 2 + impar);
					} else {
						ini = stack.size() - nLines;
						end = stack.size();
					}
				}
				show = this.getText(ini, end, index);
			} else {
				if (writing) {
					show = this.getText(0, nLines - 1, index - 1);
					if (nLines>1)
						show = show + "\n" + write;
					else
						show=write;
				} else {
					show = this.getText(0, nLines, index - 1);
				}
			}
		} else {
			if (writing) {
				show = this.getText(0, nLines - 1, index - 1);
				if (nLines>1)
					show = show + "\n" + write;
				else
					show=write;
			}
		}
		Display.setText(show);
		//Log.d("StackCalculatorActivity", "refreshView");
	}

	public void addValue(String c) {
		//Log.d("StackCalculatorActivity", write);
		String tmp = write + c;
		try {
			@SuppressWarnings("unused")
			double d = Double.valueOf(tmp);
			write = tmp;
			writing = true;
			this.refreshView();
		} catch (Exception e) {
			Log.d("StackCalculatorActivity", tmp);
			Log.d("StackCalculatorActivity", e.toString());
		}
	}

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
				}
				write = "";
			} else {
				if (stack.size() > 0) {
					stack.addFirst(stack.get(index));
					index = 0;
				}
			}
			writing = false;
			navigation = false;
			this.refreshView();
			return true;
		} else if (keyCode==KeyEvent.KEYCODE_BACK){
			if (pw!=null)
				pw.dismiss();
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
			((Button) findViewById(R.id.bSqrt)).setText("Ãx");
			((Button) findViewById(R.id.bEx)).setText("e^y");
		}
	}

	public void onClickUp(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (!writing) {
			if (!navigation) {
				navigation = true;
			} else if (stack.size() > index + 1) {
				index++;
			}
			this.refreshView();

			Button back = (Button) this.findViewById(R.id.bDel);
			back.setText("Cancel");
		}

	}

	public void onClickDown(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (!writing) {
			if (index > 0) {
				index--;
			}
			this.refreshView();

			Button back = (Button) this.findViewById(R.id.bDel);
			back.setText("Cancel");
		}

	}

	public void onClickEnter(View view) {
		if (writing) {
			if (!write.equals("")) {
				stack.addFirst(new Double(write));
			}
			write = "";
		} else {
			if (stack.size() > 0) {

				stack.addFirst(stack.get(index));
				index = 0;
				if (navigation) {
					Button back = (Button) this.findViewById(R.id.bDel);
					back.setText("Delete");
				}
			}
		}
		writing = false;
		navigation = false;
		this.refreshView();
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
			} else {
				Button back = (Button) this.findViewById(R.id.bDel);
				back.setText("Delete");
				writing=false;
			}
		} else if (!navigation) {
			if (stack.size() > 0) {
				stack.removeFirst();
			}
		} else if (navigation) {
			navigation = false;
			index = 0;
			Button back = (Button) this.findViewById(R.id.bDel);
			back.setText("Delete");
		}
		this.refreshView();
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
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			stack.addFirst(new Double(a + b));
		}
		this.refreshView();
	}

	public void onClickSub(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (shifted && stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			stack.addFirst(new Double(-a));
		} else if (stack.size() >= 2 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			stack.addFirst(new Double(b - a));
		} else if (writing) {
			if (Double.valueOf(write) < 0) {
				write = write.substring(1);
			} else {
				write = "-" + write;
			}
		}
		this.refreshView();

	}

	public void onClickMul(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (stack.size() >= 2 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			stack.addFirst(new Double(a * b));
		}
		this.refreshView();

	}

	public void onClickDiv(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (stack.size() >= 2 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			stack.addFirst(new Double(b / a));
		}
		this.refreshView();
	}
	
	public void onClickInv(View view){
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			stack.addFirst(new Double(1 / a));
		}
		this.refreshView();
	}

	public void onClickSin(View view) {
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			if (!shifted) {
				stack.addFirst(new Double(Math.sin(conv.toProcess(a))));
			} else {
				stack.addFirst(new Double(conv.toShow(Math.asin(a))));
			}
		}
		this.refreshView();

	}

	public void onClickCos(View view) {
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			if (!shifted) {
				stack.addFirst(new Double(Math.cos(conv.toProcess(a))));
			} else {
				stack.addFirst(new Double(conv.toShow(Math.acos(a))));
			}
		}
		this.refreshView();
	}

	public void onClickTan(View view) {
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			if (!shifted) {
				stack.addFirst(new Double(Math.tan(conv.toProcess(a))));
			} else {
				stack.addFirst(new Double(conv.toShow(Math.atan(a))));
			}
		}
		this.refreshView();
	}
	
	public void onClickSqrt(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			if (shifted){
				stack.addFirst(new Double(Math.pow(a,2.0f)));
			} else {
				stack.addFirst(new Double(Math.sqrt(a)));
			}
		}
		this.refreshView();
	}

	public void onClickIntegralEx(View view) {
		if (shifted){
			if (stack.size() >= 2 && !writing && !navigation) {
				double a = stack.getFirst().doubleValue();
				stack.removeFirst();
				double b = stack.getFirst().doubleValue();
				stack.removeFirst();
				//if (!shifted)
				stack.addFirst(new Double(Math.pow(b, a)));
			}
		} else{ 
			if (stack.size() >= 1 && !writing && !navigation) {
				double a = stack.getFirst().doubleValue();
				stack.removeFirst();
				//if (!shifted)
				stack.addFirst(new Double(Math.pow(Math.E, a)));
			}
		}
		this.refreshView();
	}

	public void onClickLn(View view) {
		if (stack.size() >= 1 && !writing && !navigation) {
			double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			if (shifted) {
				stack.addFirst(new Double(Math.log10(a)));
			} else {
				stack.addFirst(new Double(Math.log(a)));
			}
		}
		this.refreshView();
	}

	public void onClickPi(View view) {
		if (!writing && !navigation) {
			stack.addFirst(new Double(Math.PI));
		}
		this.refreshView();
	}
	
	public void onClickClose(View view) {
		pw.dismiss();

	}
	/*public void onClick(View view){
	Log.d("StackCalculatorActivity", "holamon-Button");
	
	}//*/
};
