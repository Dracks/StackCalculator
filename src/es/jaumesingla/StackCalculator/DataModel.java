package es.jaumesingla.StackCalculator;

import java.util.ArrayList;
import java.util.List;

import es.jaumesingla.StackCalculator.AngleConversor.ConversorInterface;

public class DataModel {
	private String newValue;
	private ArrayList<Double> listData;
	private static DataModel instance;
	
	private DataModel(){
		newValue=null;
		listData=new ArrayList<Double>();
	}
	
	static public DataModel getInstance(){
		if (instance==null){
			instance=new DataModel();
		}
		return instance;
	}
	
	public void clear(){
		listData.clear();
		newValue=null;
	}
	
	public boolean hasNewValue(){
		return newValue!=null;
	}
	
	public String getNewValue() {
		return newValue;
	}
	
	public void addChar(String s){
		try{
			String tmp;
			if (newValue==null){
				tmp=s;
			} else {
				tmp=newValue+s;
			}
			Double.valueOf(tmp);
			
			newValue=tmp;
		} catch (Exception e) {
		}
	}
	
	public void deleteChar(){
		if (newValue!=null){
			newValue=newValue.substring(0, newValue.length() - 1);
		}
	}
	
	public void pushNewValue(){
		if (newValue!=null){
			listData.add(0,Double.valueOf(newValue));
			newValue=null;
		}
	}
	
	public void pushValue(double d){
		listData.add(0,d);
	}
	
	public void copyValue(int index){
		this.pushValue(listData.get(index));
	}
	
	public void setNewValue(String newValue) {
		if (newValue!=null){
			try{
				Double.valueOf(newValue);
				this.newValue = newValue;
			} catch (Exception e){
				
			}
		} else {
			this.newValue=newValue;
		}
	}
	
	public void deleteValue(){
		if (listData.size()>0){
			listData.remove(0);
		}
	}
	
	protected Double popData(){
		Double r=this.listData.get(0);
		this.listData.remove(0);
		return r;
	}
	
	public void operationAdd(){
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			this.pushValue(Double.valueOf(a+b));
		}
	}
	
	public void operationSubs(){
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			this.pushValue(Double.valueOf(a-b));
		}		
	}
	
	public void operationMinusX(){
		if (listData.size()>=1){
			this.pushValue(Double.valueOf(-this.popData()));
		}
	}
	
	public void operationMultiply(){
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			this.pushValue(Double.valueOf(a*b));
		}		
	}
	
	public void operationDivided(){
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			this.pushValue(Double.valueOf(a/b));
		}		
	}
	
	public void operationInverse(){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(1/v));
		}		
	}
	
	public void operationPowSquare(){
		if (listData.size()>=1){
			Double a=this.popData();
			this.pushValue(Double.valueOf(a*a));
		}
	}
	
	public void operationSqrt(){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(Math.sqrt(v)));
		}
	}
	
	public void operationPow(){
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			this.pushValue(Double.valueOf(Math.pow(b,a)));
		}
	}
	
	public void operationLog(){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(Math.log10(v)));
		}
	}
	
	public void operationEPowX(){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(Math.exp(v)));
		}
	}
	
	public void operationLn(){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(Math.log(v)));
		}
	}
	
	public void operationSin(ConversorInterface conv){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(Math.sin(conv.toProcess(v))));
		}
	}
	
	public void operationCos(ConversorInterface conv){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(Math.cos(conv.toProcess(v))));
		}
	}
	
	public void operationTan(ConversorInterface conv){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(Math.tan(conv.toProcess(v))));
		}
	}
	
	public void operationArcsin(ConversorInterface conv){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(conv.toShow(Math.asin(v))));
		}
	}
	
	public void operationArccos(ConversorInterface conv){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(conv.toShow(Math.acos(v))));
		}
	}
	
	public void operationArctan(ConversorInterface conv){
		if (listData.size()>=1){
			Double v=this.popData();
			this.pushValue(Double.valueOf(conv.toShow(Math.atan(v))));
		}
	}
	
	public void operationSwap(){
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			this.pushValue(a);
			this.pushValue(b);
		}
	}
	
	public List<Double> getListData() {
		return listData;
	}
}
