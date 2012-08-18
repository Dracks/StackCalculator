package es.jaumesingla.StackCalculator;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;

import com.google.ads.*;

import es.jaumesingla.StackCalculator.AngleConversor.ConversorInterface;
import es.jaumesingla.StackCalculator.AngleConversor.DegreeConversor;
import es.jaumesingla.StackCalculator.AngleConversor.RadiantConversor;



public class StackCalculatorActivity extends Activity{

	public interface UndoRedo{
		public void undo(DataModel dm);
		public void redo(DataModel dm);
		public String getOperationName();
	}
	
	public class Historial{
		private ArrayList<UndoRedo> undoHistorial;
		private ArrayList<UndoRedo> redoHistorial;
		private DataModel dm;
		
		public Historial(DataModel dm){
			this.dm=dm;
			undoHistorial=new ArrayList<UndoRedo>();
			redoHistorial=new ArrayList<UndoRedo>();
		}
		
		public void pushOperation(UndoRedo op){
			undoHistorial.add(op);
			redoHistorial.clear();
		}
		
		public void undo(){
			int size=undoHistorial.size();
			if (size>0){
				UndoRedo op=undoHistorial.get(size-1);
				undoHistorial.remove(size-1);
				op.undo(dm);
				redoHistorial.add(op);
			}
		}
		public void redo(){
			int size=redoHistorial.size();
			if (size>0){
				UndoRedo op=redoHistorial.get(size-1);
				op.redo(dm);
				undoHistorial.add(op);
			}
		}
		
		public void clear(){
			undoHistorial.clear();
			redoHistorial.clear();
		}
	}
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
	
	private static final String TAG="StackCalculatorActivity";
	
	private ResultsViewAdapter showedData;
	
	private Historial historial;

	public StackCalculatorActivity() {
		super();
		mData=DataModel.getInstance();
		dadesImportades=false;
		historial=new Historial(mData);
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
		adRequest.addTestDevice("201D02A393A249A6928C1C03AA422FD7");
		
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
		
		ListView Display=(ListView) this.findViewById(R.id.DisplayList);
		FrameLayout Shadow=(FrameLayout) this.findViewById(R.id.DisplayShadow);

		if (this.findViewById(R.id.lHorizontal)!=null){
			horizontal=true;
			//Calcul de tamany de la pantalla
			View contentView = this.findViewById(R.id.Contingut);
			int alcadaTotal = contentView.getHeight();
			
			alcadaTotal -= this.findViewById(R.id.linearLayout7).getHeight();
			
			int ampladaTotal = contentView.getWidth()/2;
			//this.findViewById(R.id.Display).setLayoutParams(new LayoutParams(this.findViewById(R.id.Display).getHeight(), alcadaTotal));
			//EditText Display = (EditText) this.findViewById(R.id.Display);
			//Display = (ListView) this.findViewById(R.id.DisplayList);
			
			Shadow.getLayoutParams().height = alcadaTotal;
			Shadow.getLayoutParams().width = ampladaTotal;
			//nLines = alcadaTotal / Display.getLineHeight() - 1;
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
			//Display = (ListView) this.findViewById(R.id.DisplayList);
			Shadow.getLayoutParams().height = alcadaTotal;
			//nLines = alcadaTotal / Display.getLineHeight() - 1;
			//Log.i("stackCalculatorActivity", "Al�ada total de:" + Integer.toString(alcadaTotal));
			//this.refreshView();
		}
		
		AdapterView.OnItemClickListener clickListener=new AdapterView.OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> adapterView, View view,int arg2, long arg3)
			{
				StackCalculatorActivity.this.historial.pushOperation(mData.copyValue(arg2));
				showedData.notifyDataSetChanged();
			}
		};
		
		/*OnItemLongClickListener longClickListener=new AdapterView.OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.d(TAG, String.format("Item: %d and Group: %d", arg2, arg3));
				return true;
			}
			
		};*/
		
		Display.setAdapter(showedData);
		
		Display.setOnItemClickListener( clickListener );
		
		registerForContextMenu(Display);
		
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
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.list_extension_menu, menu);
	    menu.setHeaderTitle(getResources().getString(R.string.value)+mData.getListData().get((int) ((AdapterContextMenuInfo) menu.getItem(0).getMenuInfo()).id).toString());
	    
	    ClipboardManager clipboard=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	    MenuItem mPaste=menu.findItem(R.id.paste);
	    mPaste.setEnabled(false);
	    if (clipboard.hasText()){
	    	try{
	    		Double.valueOf(clipboard.getText().toString());
	    		mPaste.setEnabled(true);
	    	} catch (Exception e){
	    		
	    	}
	    	
	    } 
	    /*if (!(clipboard.hasPrimaryClip())) {

	        mPasteItem.setEnabled(false);

	        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

	            // This disables the paste menu item, since the clipboard has data but it is not plain text
	            mPasteItem.setEnabled(false);
	        } else {

	            // This enables the paste menu item, since the clipboard contains plain text.
	            mPasteItem.setEnabled(true);
	        }
	    }*/
	    
	    //menu.findItem(id)
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    ClipboardManager clipboard;
	    switch (item.getItemId()) {
	        case R.id.delete:
	            Log.d(TAG, "Delete item:"+info.id);
	            historial.pushOperation(mData.deleteValue((int)info.id));
	            return true;
	        case R.id.copy:
	        	Log.d(TAG, "copy item:"+info.id);
	        	clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	        	//ClipData clip = ClipData.newPlainText("simple text","Hello, World!");
	        	//clipboard.setPrimaryClip(clip);
	        	clipboard.setText(mData.getListData().get((int) info.id).toString());
	            return true;
	        case R.id.paste:
	        	Log.d(TAG, "Paste no item:");
	        	clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	        	//ClipData clip = ClipData.newPlainText("simple text","Hello, World!");
	        	//clipboard.setPrimaryClip(clip);
	        	//clipboard.setText(mData.getListData().get((int) info.id).toString());
	        	try{
	        		historial.pushOperation(mData.pushValue(Double.valueOf(clipboard.getText().toString())));
	        	} catch (Exception e){}
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
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
						historial.pushOperation(mData.pushNewValue());
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
		historial.pushOperation(mData.operationSwap());
		showedData.notifyDataSetChanged();
	}
	
	public void onClickUndo(View view){
		historial.undo();
		showedData.notifyDataSetChanged();
	}

	public void onClickEnter(View view) {
		String nv=mData.getNewValue();
		if (nv!=null) {
			if (nv.equals("")) {
				mData.setNewValue(null);
			} else {
				historial.pushOperation(mData.pushNewValue());
			}
			showedData.notifyDataSetChanged();
		}
		writing = false;
		navigation = false;
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
			//List<Double> list=mData.getListData();
			//if (list.size() > 0) {
			historial.pushOperation(mData.deleteValue());
			//}
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
			historial.pushOperation(mData.operationAdd());
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickSub(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (shifted && !mData.hasNewValue() && !navigation) {
			historial.pushOperation(mData.operationMinusX());
		} else if (!mData.hasNewValue() && !navigation) {
			historial.pushOperation(mData.operationSubs());
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
			historial.pushOperation(mData.operationMultiply());
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickDiv(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (!mData.hasNewValue() && !navigation) {
			historial.pushOperation(mData.operationDivided());
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}
	
	public void onClickInv(View view){
		if (!mData.hasNewValue() && !navigation) {
			historial.pushOperation(mData.operationInverse());
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickSin(View view) {
		if (!mData.hasNewValue() && !navigation) {
			if (!shifted) {
				historial.pushOperation(mData.operationSin(conv));
			} else {
				historial.pushOperation(mData.operationArcsin(conv));
			}
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();

	}

	public void onClickCos(View view) {
		if (!mData.hasNewValue() && !navigation) {
			if (!shifted) {
				historial.pushOperation(mData.operationCos(conv));
			} else {
				historial.pushOperation(mData.operationArccos(conv));
			}
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickTan(View view) {
		if (!mData.hasNewValue() && !navigation) {
			if (!shifted) {
				historial.pushOperation(mData.operationTan(conv));
			} else {
				historial.pushOperation(mData.operationArctan(conv));
			}
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}
	
	public void onClickSqrt(View view) {
		//Log.d("StackCalculatorActivity", "holamon-Button");
		if (!mData.hasNewValue() && !navigation) {
			
			if (shifted) {
				historial.pushOperation(mData.operationPowSquare());
			} else {
				historial.pushOperation(mData.operationSqrt());
			}
			showedData.notifyDataSetChanged();
		}
		//this.refreshView();
	}

	public void onClickIntegralEx(View view) {
		if (shifted){
			if (!mData.hasNewValue() && !navigation) {
				historial.pushOperation(mData.operationPow());
			}
		} else{ 
			if (!mData.hasNewValue() && !navigation) {
				historial.pushOperation(mData.operationEPowX());
			}
		}
		showedData.notifyDataSetChanged();
		//this.refreshView();
	}

	public void onClickLn(View view) {
		if (!mData.hasNewValue() && !navigation) {
			if (shifted) {
				historial.pushOperation(mData.operationLog());
			} else {
				historial.pushOperation(mData.operationLn());
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
}
