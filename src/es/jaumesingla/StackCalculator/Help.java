package es.jaumesingla.StackCalculator;

import java.util.Random;

import android.app.Activity;
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
		Random r=new Random();
		
		String[] listText=text.split(" ");
		String[] linia;
		String[] espaiat;
		String saved="";
		String tmp="";
		
		int iniText=0;
		int endText=0;
		int addSpaced=0;
		int nLines=1;
		
		char sep='\t';
		
		v.setText(saved+sep+tmp);
		while (endText<listText.length-1){
			tmp="";
			while (v.getLineCount()==nLines && endText<listText.length){
				tmp=tmp+" "+listText[endText];
				endText++;
				v.setText(saved+sep+tmp);
			}
			if (endText<listText.length)
				endText--;
			Log.d("Help-JustifiViewText", Integer.toString(v.getLineCount()));
			
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
			v.setText(saved+sep+tmp);
			
			while (v.getLineCount()==nLines){
				addSpaced= (addSpaced+1) % espaiat.length;
				espaiat[addSpaced]+=" ";
				tmp=linia[0];
				for (int i=0; i<espaiat.length;i++){
					tmp+=espaiat[i]+linia[i+1];
				}
				v.setText(saved+sep+tmp);
			}
			
			Log.d("Help-JustifiViewText", Integer.toString(v.getLineCount()));
			
			espaiat[addSpaced]=espaiat[addSpaced].substring(1);
			
			tmp=linia[0];
			for (int i=0; i<espaiat.length;i++){
				tmp+=espaiat[i]+linia[i+1];
			}
			saved+=sep+tmp;
			sep=' ';
			
			nLines++;
			
		}
		
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		//Calcul de tamany de la pantalla
		View contentView = this.findViewById(R.id.lHelp);
		int alcadaTotal = contentView.getHeight();
		
		
		this.justifyTextView((TextView) this.findViewById(R.id.tHelp2), getString(R.string.tutorial_text_1));
		this.justifyTextView((TextView) this.findViewById(R.id.tHelp3), getString(R.string.tutorial_text_2));
		this.justifyTextView((TextView) this.findViewById(R.id.tHelp4), getString(R.string.tutorial_text_3));
		this.justifyTextView((TextView) this.findViewById(R.id.tHelp5), getString(R.string.tutorial_text_4));
	}
	
	public void onClickClose(View view){
		this.finish();
	}
}
