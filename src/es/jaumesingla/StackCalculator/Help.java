package es.jaumesingla.StackCalculator;

import java.util.Random;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Help extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Creaci�
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}
	
	public void justifyTextView(TextView v, String text){
		Random r=new Random(31415);
		
		String[] listText=text.split(" ");
		String[] linia;
		String[] espaiat;
		String saved="";
		String tmp="";
		
		int iniText=0;
		int endText=0;
		int addSpaced=0;
		//int nLines=1;
		
		String sep="";
		String tst="";
		
		//int maxWidth=(int) (v.getWidth()*0.8);
		float maxWidth= v.getMeasuredWidth()- v.getCompoundPaddingLeft() - v.getCompoundPaddingRight();
		
		Rect bounds = new Rect();
		Paint textPaint = v.getPaint();
		textPaint.getTextBounds(text,0,text.length(),bounds);
		/*int height = bounds.height();
		int width = bounds.width();//*/
		
		//v.setText(saved+sep+tmp);
		while (endText<listText.length-1){
			tmp=listText[endText++];
			
			//textPaint.getTextBounds(tst+tmp, 0, tmp.length()+tst.length(), bounds);
			while (textPaint.measureText(tst+tmp) <maxWidth && endText<listText.length){
				tmp=tmp+" "+listText[endText];
				endText++;
				//textPaint.getTextBounds(tst+tmp, 0, tmp.length()+tst.length(), bounds);
				//v.setText(saved+sep+tmp);
			}
			if (endText<listText.length)
				endText--;
			else {
				v.setText(saved+sep+tmp);
				break;
			}
			Log.d("Help-JustifiViewText ", Integer.toString(bounds.width()));
			//Log.d("Help-JustifiViewText--", Integer.toString(bounds.right-bounds.left));
			
			Log.d("Help-JustifiViewText", tmp);
			Log.d("Help-JustifiViewText", Integer.toString(v.getLineCount()));
			assert endText>iniText;
			assert endText<listText.length;
			linia=new String[endText-iniText];
			espaiat=new String[endText-iniText-1];
			for (int i=iniText; i<endText; i++){
				linia[i-iniText]=listText[i];
			}
			iniText=endText;
			
			for (int i=0; i<espaiat.length; i++){
				espaiat[i]=" ";
			}
			
			addSpaced=Math.abs(r.nextInt());
			
			tmp=linia[0];
			for (int i=0; i<espaiat.length;i++){
				tmp+=espaiat[i]+linia[i+1];
			}
			//v.setText(saved+sep+tmp);
			
			//textPaint.getTextBounds(tst+tmp, 0, tmp.length()+tst.length(), bounds);
			//Log.d("Debug size:", Integer.toString(bounds.width())+" vs "+Float.toString(maxWidth));
			while (textPaint.measureText(tst+tmp)<maxWidth){
				addSpaced= (addSpaced+1) % espaiat.length;
				espaiat[addSpaced]+=" ";
				tmp=linia[0];
				for (int i=0; i<espaiat.length;i++){
					tmp+=espaiat[i]+linia[i+1];
				}
				//Log.d("Help-JustifiViewText", "otra mas");
				//v.setText(saved+sep+tmp);
				//textPaint.getTextBounds(tst+tmp, 0, tmp.length()+tst.length(), bounds);
			}
			
			espaiat[addSpaced]=espaiat[addSpaced].substring(1);
			
			tmp=linia[0];
			for (int i=0; i<espaiat.length;i++){
				tmp+=espaiat[i]+linia[i+1];
			}
			//textPaint.getTextBounds(tst+tmp, 0, tmp.length()+tst.length(), bounds);
			Log.d("Debug size:", Float.toString(textPaint.measureText(tst+tmp))+" vs "+Float.toString(maxWidth)+" =>"+tst+tmp);
			//Log.d("Help-JustifiViewText", tmp);
			//Log.d("Help-JustifiViewText", Integer.toString(v.getLineCount()));
			saved+=sep+tmp;
			sep="\n";
			tst="";
			
			//nLines++;
			
		}
		
		//v.setText(saved);
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		//Calcul de tamany de la pantalla
		//View contentView = this.findViewById(R.id.lHelp);
		//int alcadaTotal = contentView.getHeight();
		
		
		this.justifyTextView((TextView) this.findViewById(R.id.tHelp2), getString(R.string.tutorial_text_1));
		this.justifyTextView((TextView) this.findViewById(R.id.tHelp3), getString(R.string.tutorial_text_2));
		this.justifyTextView((TextView) this.findViewById(R.id.tHelp4), getString(R.string.tutorial_text_3));
		this.justifyTextView((TextView) this.findViewById(R.id.tHelp5), getString(R.string.tutorial_text_4));
	}
	
	public void onClickClose(View view){
		this.finish();
	}
}
