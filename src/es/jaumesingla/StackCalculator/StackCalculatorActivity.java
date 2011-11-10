package es.jaumesingla.StackCalculator;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.ads.*;

public class StackCalculatorActivity extends Activity {
	private LinkedList<Double> 	stack;
	private int 				index;
	//private double				write;
	private String				write;
	private boolean				writing;
	private boolean				navigation;
	private boolean				shifted;
	private int					nLines;
	
	
	
	public StackCalculatorActivity(){
		stack=new LinkedList<Double>();
		index=0;
		write="";
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layouts); 
        this.refreshView();
        
        // Look up the AdView as a resource and load a request.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest();
        adRequest.addTestDevice("953C629AF51EC113C8C153493876C11F");
        adRequest.setTesting(true);
        adView.loadAd(adRequest);
        nLines=5;
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      // Save UI state changes to the savedInstanceState.
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
      savedInstanceState.putInt("StackSize", this.stack.size());
      for (int i=0; i<stack.size(); i++){
    	  savedInstanceState.putDouble("Row "+Integer.toString(i),stack.get(i).doubleValue());
      }
      // etc.
      super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      // Restore UI state from the savedInstanceState.
      // This bundle has also been passed to onCreate.
      int Size=savedInstanceState.getInt("StackSize");
      for (int i=0; i<Size; i++){
    	  stack.addLast(new Double(savedInstanceState.getDouble("Row "+Integer.toString(i))));
      }
    }
    
    private String getText(int ini,int end,int index){
    	String show="";
    	for(int i=ini; i<end; i++){
    		if (i<stack.size()){
    			char c=' ';
    			if (i!=index){
    				c=':';
    			} else {
    				c='<';
    			}
    			show=stack.get(i).toString()+c+Integer.toString(i+1)+"\n"+show;
    		} else {
    			show=":"+Integer.toString(i+1)+"\n"+show;
    		}
    	}
    	return show;
    }
    
    public void refreshView(){
    	EditText Display=(EditText) findViewById(R.id.Display);
    	String show=":5\n:4\n:3\n:2\n:1";
    	//int i;
    	if (stack.size()>0){
    		if (navigation){
	    		int ini,end;
				ini=0; end=nLines;
				if (index>(nLines/2)){
					if (index<stack.size()-(nLines/2+1)){
						ini=index-(nLines/2);
						end=index+(nLines/2+1);
					} else {
						ini=stack.size()-nLines;
						end=stack.size();
					}
				}
				show=this.getText(ini,end,index);
    		} else {
    			if (writing){
    				show=this.getText(0,nLines-1,index-1);
    				show=show+write;
    			} else {
    				show=this.getText(0,nLines,index-1);
    			}
    		}
    	} else {
			if (writing){
				show=this.getText(0,nLines-1,index-1);
				show=show+write;
			} 
		}
    	Display.setText(show);
    	Log.d("StackCalculatorActivity", "refreshView");
    }
    
    public void addValue(String c){
    	Log.d("StackCalculatorActivity", write);
    	String tmp=write+c;
    	try {
    		@SuppressWarnings("unused")
			double d=Double.valueOf(tmp);
    		write=tmp;
    		writing=true;
    		this.refreshView();
    	} catch(Exception e){
    		Log.d("StackCalculatorActivity", tmp);
    		Log.d("StackCalculatorActivity", e.toString());
    	}
    }
    
    public void onClickButton(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	
    }
    
    public void onClickShift(View view){
    	shifted=!shifted;
    	if (shifted) {
    		((Button) findViewById(R.id.bShift)).setText("Unshift");
    		((Button) findViewById(R.id.bDiv)).setText("1/x");
    		((Button) findViewById(R.id.bSub)).setText("-x");
    		((Button) findViewById(R.id.bSin)).setText("Arcsin");
    		((Button) findViewById(R.id.bCos)).setText("Arccos");
    		((Button) findViewById(R.id.bTan)).setText("Arctan");
    		((Button) findViewById(R.id.bLn)).setText("Log");
    	} else {
    		((Button) findViewById(R.id.bShift)).setText("Shift");
    		((Button) findViewById(R.id.bDiv)).setText("/");
    		((Button) findViewById(R.id.bSub)).setText("-");
    		((Button) findViewById(R.id.bSin)).setText("Sin");
    		((Button) findViewById(R.id.bCos)).setText("Cos");
    		((Button) findViewById(R.id.bTan)).setText("Tan");
    		((Button) findViewById(R.id.bLn)).setText("Ln");
    	}
    }
    
    public void onClickUp(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (!writing){
    		if (!navigation)
    			navigation=true;
    		else if (stack.size()>index+1)
    			index++;
    		this.refreshView();
    	}
    	
    }
    
    public void onClickDown(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (!writing){
    		if (index>0)
    			index--;
    		this.refreshView();
    	}
    	
    }
    
    public void onClickEnter(View view){
    	if (writing){
    		stack.addFirst(new Double(write));
    		write="";
    	} else {
    		if (stack.size()>0){
    			stack.addFirst(stack.get(index));
    			index=0;
    		}
    	}
    	writing=false;
    	navigation=false;
    	this.refreshView();
    }
    
    public void onClickDel(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (writing){
    		write=write.substring(0, write.length()-1);
    	} else if (!navigation){
    		if (stack.size()>0)
    			stack.removeFirst();
    	} else if (navigation){
    		navigation=false;
    		index=0;
    	}
    	this.refreshView();
    }
    
    public void onClickChar(View view){
    	Log.d("StackCalculatorActivity", ((Button) view).getText().toString());
    	this.addValue(((Button) view).getText().toString());
    	
    }
    
    public void onClickAdd(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (stack.size()>=2 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		double b=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(a+b));
    	}
    	this.refreshView();
    }
    
    public void onClickSub(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (shifted && stack.size()>=1 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(-a));
    	} else if (stack.size()>=2 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		double b=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(b-a));
    	} else if (writing){
    		if (Double.valueOf(write)<0){
    			write=write.substring(1);
    		} else {
    			write="-"+write;
    		}
    	}
    	this.refreshView();
    	
    }
    
    public void onClickMul(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (stack.size()>=2 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		double b=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(a*b));
    	}
    	this.refreshView();
    	
    }
    
    public void onClickDiv(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (stack.size()>=2 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		if (shifted){
    			stack.addFirst(new Double(1/a));
    		} else {
	    		double b=stack.getFirst().doubleValue();
	    		stack.removeFirst();
	    		stack.addFirst(new Double(b/a));
    		}
    	}
    	this.refreshView();
    }
    
    public void onClickPow(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (stack.size()>=2 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		double b=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(Math.pow(b,a)));
    	}
    	this.refreshView();
    	
    }
    
    public void onClickSqrt(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (stack.size()>=1 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(Math.sqrt(a)));
    	}
    	this.refreshView();
    }
    
    public void onClickSin(View view){
    	if (stack.size()>=1 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		if (!shifted)
    			stack.addFirst(new Double(Math.sin(a)));
    		else
    			stack.addFirst(new Double(Math.asin(a)));
    	}
    	this.refreshView();
    	
    }
    
    public void onClickCos(View view){
    	if (stack.size()>=1 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		if (!shifted)
    			stack.addFirst(new Double(Math.cos(a)));
    		else
    			stack.addFirst(new Double(Math.acos(a)));
    	}
    	this.refreshView();
    }
    
    public void onClickTan(View view){
    	if (stack.size()>=1 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		if (!shifted)
    			stack.addFirst(new Double(Math.tan(a)));
    		else
    			stack.addFirst(new Double(Math.atan(a)));
    	}
    	this.refreshView();
    }
    
    public void onClickIntegralEx(View view){
    	if (stack.size()>=1 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		//if (!shifted)
    		stack.addFirst(new Double(Math.pow(Math.E, a)));
    	}
    	this.refreshView();
    }
    
    public void onClickLn(View view){
    	if (stack.size()>=1 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		if (shifted)
    			stack.addFirst(new Double(Math.log10(a)));
    		else 
    			stack.addFirst(new Double(Math.log(a)));
    	}
    	this.refreshView();
    }
    
    public void onClickPi(View view){
    	if (stack.size()>=1 && !writing && !navigation){
    		stack.addFirst(new Double(Math.PI));
    	}
    	this.refreshView();
    }
    
    /*public void onClick(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	
    }//*/
    
};
