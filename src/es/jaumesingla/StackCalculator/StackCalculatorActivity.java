package es.jaumesingla.StackCalculator;

import java.util.List;

import android.app.Activity;
//import android.app.AlertDialog;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import android.widget.*;
import com.google.ads.*;

import es.jaumesingla.StackCalculator.AngleConversor.ConversorInterface;
import es.jaumesingla.StackCalculator.AngleConversor.DegreeConversor;
import es.jaumesingla.StackCalculator.AngleConversor.RadiantConversor;



public class StackCalculatorActivity extends Activity{

	//private LinkedList<Double> stack;
	//private double				write;
	//private String write;
	private DataModel mData;
	private boolean writing;
	private boolean navigation;
	private boolean shifted;
	private ConversorInterface conv;
	private PopupWindow pw;
	
	private boolean horizontal;
	private boolean dadesImportades;
	
	private static final String dataName="StackCalculatorData"; 
	private static final float dataVersion=1.0f;
	
	private ResultsViewAdapter showedData;
	
	

	public StackCalculatorActivity() {
		super();
		mData=DataModel.getInstance();
		dadesImportades=false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Creaci�
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		//this.refreshView();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		//Publicitat
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice("565DD0971D493145303E3C0AA27960F7");
		
		//adRequest.setTesting(true);
		adView.loadAd(adRequest);
		
		showedData=new ResultsViewAdapter((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		
		mData.clear();
		
		
		this.conv = new RadiantConversor();
		//nLines=5;
		
		try {
			if (!dadesImportades){
				dadesImportades=true;
				SharedPreferences settings = getSharedPreferences(dataName, 0);
				if (settings.getFloat("version",dataVersion)==dataVersion){
					int nData=settings.getInt("StackSize", 0);
					
					for (int i=0; i<nData; i++){
						Double d=Double.valueOf(settings.getFloat("Row " + Integer.toString(i), 0.0f));
						//stack.addLast(d);
						mData.pushValue(d);
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
		List<Double> list=mData.getListData();
		settings.putInt("StackSize", list.size());
		for (int i = 0; i < list.size(); i++) {
			settings.putFloat("Row " + Integer.toString(i), (float) list.get(i).doubleValue());
		}
		settings.commit();//*/
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		AdapterView.OnItemClickListener clickListener=new AdapterView.OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> adapterView, View view,int arg2, long arg3)
			{
				mData.copyValue(arg2);
				showedData.notifyDataSetChanged();
			}
		};
		
		/*AdapterView.OnLongClickListener longClickListener=new AdapterView.OnLongClickListener() 
		{
			
			public boolean onLongClick(View arg0) {
				
				ShowAlert(String.valueOf(arg0));
				return true;
			}
		};*/

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
			Display.setOnItemClickListener( clickListener );
			
			//Display.setOnLongClickListener(longClickListener);
			

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
			
			Display.setOnItemClickListener( clickListener );
			
			//Display.setOnLongClickListener(longClickListener);
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
		/*String tmp = write + c;
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
		}*/
		mData.addChar(c);
		showedData.notifyDataSetChanged();
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
				if (mData.getNewValue()!=null){
					if (mData.getNewValue().equals("")){
						mData.setNewValue(null);
					} else {
						mData.pushNewValue();
					}
				}
				showedData.notifyDataSetChanged();
				writing = false;
			}
			navigation = false;
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
			((Button) findViewById(R.id.bSub)).setText(R.string.minusx);
			if (findViewById(R.id.bSin)!=null){
				((Button) findViewById(R.id.bSin)).setText(R.string.arcsin);
				((Button) findViewById(R.id.bCos)).setText(R.string.arccos);
				((Button) findViewById(R.id.bTan)).setText(R.string.arctan);
			}
			((Button) findViewById(R.id.bLn)).setText(R.string.log);
			((Button) findViewById(R.id.bSqrt)).setText(R.string.square);
			((Button) findViewById(R.id.bEx)).setText(R.string.powxy);
		} else {
			//((Button) findViewById(R.id.bShift)).setText("Shift");
			//((Button) findViewById(R.id.bDiv)).setText("/");
			((Button) findViewById(R.id.bSub)).setText(R.string.subs);
			if (findViewById(R.id.bSin)!=null){
				((Button) findViewById(R.id.bSin)).setText(R.string.sin);
				((Button) findViewById(R.id.bCos)).setText(R.string.cos);
				((Button) findViewById(R.id.bTan)).setText(R.string.tan);
			}
			((Button) findViewById(R.id.bLn)).setText(R.string.ln);
			((Button) findViewById(R.id.bSqrt)).setText(R.string.sqrt);
			((Button) findViewById(R.id.bEx)).setText(R.string.pow);
		}
	}

	public void onClickExchange(View view){
		mData.operationSwap();
		showedData.notifyDataSetChanged();
	}
	
	public void onClickUndo(View view){
		
	}

	public void onClickEnter(View view) {
		String nv=mData.getNewValue();
		if (nv!=null) {
			if (nv.equals("")) {
				mData.setNewValue(null);
			} else {
				mData.pushNewValue();
			}
			showedData.notifyDataSetChanged();
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
		String nv=mData.getNewValue();
		if (nv!=null) {
			if (nv.length()>0){
				if (nv.length()==1){
					Button back = (Button) this.findViewById(R.id.bDel);
					back.setText("Cancel");
				}
				mData.deleteChar();
			} else {
				Button back = (Button) this.findViewById(R.id.bDel);
				back.setText("Delete");
				mData.setNewValue(null);
				writing=false;
			}
		} else if (!navigation) {
			List<Double> list=mData.getListData();
			if (list.size() > 0) {
				mData.deleteValue();
			}
		} else if (navigation) {
			navigation = false;
			Button back = (Button) this.findViewById(R.id.bDel);
			back.setText("Delete");
		}
		showedData.notifyDataSetChanged();
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
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=Double.valueOf(a + b);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			mData.operationAdd();
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickSub(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (shifted && !mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			
			Double d=Double.valueOf(-a);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			mData.operationMinusX();
		} else if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=Double.valueOf(b - a);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			mData.operationSubs();
		} else if (mData.hasNewValue()) {
			String nv=mData.getNewValue();
			if (Double.valueOf(nv) < 0) {
				nv = nv.substring(1);
			} else {
				nv = "-" + nv;
			}
			mData.setNewValue(nv);
		}
		showedData.notifyDataSetChanged();
		//this.refreshView();

	}

	public void onClickMul(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=Double.valueOf(a * b);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			mData.operationMultiply();
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickDiv(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			double b = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=Double.valueOf(b / a);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			mData.operationDivided();
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}
	
	public void onClickInv(View view){
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d=Double.valueOf(1 / a);
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			mData.operationInverse();
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickSin(View view) {
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			Double d;
			showedData.removeItem(0);
			if (!shifted) {
				d=Double.valueOf(Math.sin(conv.toProcess(a)));
			} else {
				d=Double.valueOf(conv.toShow(Math.asin(a)));
			}
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			if (!shifted) {
				mData.operationSin(conv);
			} else {
				mData.operationArcsin(conv);
			}
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();

	}

	public void onClickCos(View view) {
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d;
			if (!shifted) {
				d=Double.valueOf(Math.cos(conv.toProcess(a)));
			} else {
				d=Double.valueOf(conv.toShow(Math.acos(a)));
			}
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			if (!shifted) {
				mData.operationCos(conv);
			} else {
				mData.operationArccos(conv);
			}
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickTan(View view) {
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d;
			if (!shifted) {
				d=Double.valueOf(Math.tan(conv.toProcess(a)));
			} else {
				d=Double.valueOf(conv.toShow(Math.atan(a)));
			}
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());//*/
			if (!shifted) {
				mData.operationTan(conv);
			} else {
				mData.operationArctan(conv);
			}
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}
	
	public void onClickSqrt(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d;
			if (shifted){
				d=Double.valueOf(Math.pow(a,2.0f));
			} else {
				d=Double.valueOf(Math.sqrt(a));
			}
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			
			if (shifted) {
				mData.operationPowSquare();
			} else {
				mData.operationSqrt();
			}
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickIntegralEx(View view) {
		if (shifted){
			if (!mData.hasNewValue() && !navigation) {
				/*double a = stack.getFirst().doubleValue();
				stack.removeFirst();
				showedData.removeItem(0);
				double b = stack.getFirst().doubleValue();
				stack.removeFirst();
				showedData.removeItem(0);
				//if (!shifted)
				Double d=Double.valueOf(Math.pow(b, a));
				stack.addFirst(d);
				showedData.addFirstItem(d.toString());*/
				mData.operationPow();
			}
		} else{ 
			if (!mData.hasNewValue() && !navigation) {
				/*double a = stack.getFirst().doubleValue();
				stack.removeFirst();
				showedData.removeItem(0);
				//if (!shifted)
				Double d=Double.valueOf(Math.pow(Math.E, a));
				stack.addFirst(d);
				showedData.addFirstItem(d.toString());*/
				mData.operationEPowX();
			}
		}
		showedData.notifyDataSetChanged();
		//this.refreshView();
	}

	public void onClickLn(View view) {
		if (!mData.hasNewValue() && !navigation) {
			/*double a = stack.getFirst().doubleValue();
			stack.removeFirst();
			showedData.removeItem(0);
			Double d;
			if (shifted) {
				d=Double.valueOf(Math.log10(a));
			} else {
				d=Double.valueOf(Math.log(a));
			}
			
			stack.addFirst(d);
			showedData.addFirstItem(d.toString());*/
			if (shifted) {
				mData.operationLog();
			} else {
				mData.operationLn();
			}
			showedData.notifyDataSetChanged();
			
		}
		//this.refreshView();
	}

	public void onClickPi(View view) {
		if (!mData.hasNewValue() && !navigation) {
			Double d=Double.valueOf(Math.PI);
			//stack.addFirst(d);
			//showedData.addFirstItem(d.toString());
			mData.pushValue(d);
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}
	
	public void onClickClose(View view) {
		pw.dismiss();

	}
	
	/*public void onClickElement(AdapterView adapterView, View view, int position, long id){
		
		int selectedPosition = adapterView.getSelectedItemPosition();
          ShowAlert(String.valueOf(selectedPosition));
	}
	
	/*public void onClick(View view){
	Log.d("StackCalculatorActivity", "holamon-Button");
	
	}//*/
/*
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
			
	}//*/
}
