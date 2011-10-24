package es.jaumesingla.StackCalculator;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class StackCalculatorActivity extends Activity {
	private LinkedList<Double> 	stack;
	private int 				index;
	//private double				write;
	private String				write;
	private boolean				writing;
	private boolean				navigation;
	
	public StackCalculatorActivity(){
		stack=new LinkedList<Double>();
		index=0;
		write="";
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        this.refreshView();
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
    	String show=":7\n:6\n:5\n:4\n:3\n:2\n:1";
    	//int i;
    	if (stack.size()>0){
    		if (navigation){
	    		int ini,end;
				ini=0; end=7;
				if (index>3){
					if (index<stack.size()-3){
						ini=index-3;
						end=index+3;
					} else {
						ini=stack.size()-7;
						end=stack.size();
					}
				}
				show=this.getText(ini,end,index);
    		} else {
    			if (writing){
    				show=this.getText(0,6,index-1);
    				show=show+write;
    			} else {
    				show=this.getText(0,7,index-1);
    			}
    		}
			
    		/*if (navigation){
    			if (stack.size()<7){
    				show="";
    				for (i=0; i<stack.size();i++){
    					String tag=stack.get(i).toString();
    					if (i!=index){
    						tag=tag+":";
    					} else {
    						tag=tag+"<";
    					}
    					
    					show=tag+Integer.toString(i+1)+show;
    				}
    				
    				for (i=stack.size(); i<7; i++){
    					show=":"+Integer.toString(i+1)+show;
    				}
    				
    			} else {
    				
    				
    				show="";
    				for (i=ini; i<end; i++){
    				
    					String tag=stack.get(i).toString();
    					if (i!=index){
    						tag=tag+":";
    					} else {
    						tag=tag+"<";
    					}
    
    					show=tag+Integer.toString(i+1)+show;
    				}
    			}
    		} else {
    			if (writing){
    				
    			} else {
    				
    			}
    		}*/
    	} else {
			if (writing){
				show=this.getText(0,6,index-1);
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
    	if (stack.size()>=2 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		double b=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(a-b));
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
    		double b=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(a/b));
    	}
    	this.refreshView();
    }
    
    public void onClickPow(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (stack.size()>=1 && !writing && !navigation){
    		double a=stack.getFirst().doubleValue();
    		stack.removeFirst();
    		stack.addFirst(new Double(a*a));
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
    
    public void onClickDel(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	if (writing){
    		write=write.substring(0, write.length()-1);
    	} else if (!navigation){
    		stack.removeFirst();
    	}
    	this.refreshView();
    }
   
    /*public void onClick(View view){
    	Log.d("StackCalculatorActivity", "holamon-Button");
    	
    }//*/
    
};
