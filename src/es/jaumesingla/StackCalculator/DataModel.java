package es.jaumesingla.StackCalculator;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import junit.framework.Assert;

import es.jaumesingla.StackCalculator.StackCalculatorActivity.UndoRedo;
import es.jaumesingla.StackCalculator.AngleConversor.ConversorInterface;

public class DataModel {
	private String newValue;
	private ArrayList<Double> listData;
	private static DataModel instance;
	private int idOperation;
	
	public abstract class UndoRedoValuesClass implements UndoRedo{
		private String operation;
		private int nOperation;
		private static final String TAG="DataModel-UndoRedoValuesClass";
		private DataModel dm;
		
		public UndoRedoValuesClass(DataModel dm, String operation){
			this.operation=operation;
			dm.idOperation++;
			this.nOperation=dm.idOperation;
			Assert.assertEquals(dm.idOperation, this.nOperation);
			this.dm=dm;
		}
	
		protected void privateUndo(DataModel dm) {
			Log.d(TAG, this.operation);
			Assert.assertEquals(dm.idOperation, this.dm.idOperation);
			Assert.assertEquals(dm.idOperation, this.nOperation);
			dm.idOperation--;
		}

		protected void privateRedo(DataModel dm) {
			Assert.assertEquals(dm.idOperation-1, this.nOperation);
			dm.idOperation++;
		}

		@Override
		public String getOperationName() {
			return operation;
		}
	}
	
	public class TwoValuesUndoRedo extends UndoRedoValuesClass{
		private Double a;
		private Double b;
		private Double r;

		public TwoValuesUndoRedo(DataModel dm, String operation, Double a, Double b, Double r) {
			super(dm, operation);
			this.a=a;
			this.b=b;
			this.r=r;
		}

		@Override
		public void undo(DataModel dm) {
			super.privateUndo(dm);
			dm.popData();
			dm.pushValue(this.b);
			dm.pushValue(this.a);
			
		}

		@Override
		public void redo(DataModel dm) {
			super.privateRedo(dm);
			dm.popData();
			dm.popData();
			dm.pushValue(this.r);	
		}
	}
	
	public class OneValueUndoRedo extends UndoRedoValuesClass{
		private Double a;
		private Double r;

		public OneValueUndoRedo(DataModel dm, String operation, Double a, Double r) {
			super(dm, operation);
			this.a=a;
			this.r=r;
		}

		@Override
		public void undo(DataModel dm) {
			super.privateUndo(dm);
			dm.popData();
			dm.pushValue(this.a);
			
		}

		@Override
		public void redo(DataModel dm) {
			super.privateRedo(dm);
			dm.popData();
			dm.pushValue(this.r);	
		}
	}
	
	public class NewValueUndoRedo extends UndoRedoValuesClass{
		private Double v;
		public NewValueUndoRedo(DataModel dm, String operation, Double v) {
			super(dm, operation);
			this.v=v;
			
		}

		@Override
		public void undo(DataModel dm) {
			super.privateUndo(dm);
			Double popValue=dm.popData();
			Assert.assertEquals(popValue.doubleValue(), v.doubleValue());
		}

		@Override
		public void redo(DataModel dm) {
			super.privateUndo(dm);
			dm.pushValue(v);
			
		}
		
	}
	
	
	
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
	
	public UndoRedo pushNewValue(){
		UndoRedo undoredo=null;
		if (newValue!=null){
			undoredo=this.pushValue((Double.valueOf(newValue)));
			newValue=null;
		}
		return undoredo;
	}
	
	public UndoRedo pushValue(double d){
		UndoRedo undoredo=new NewValueUndoRedo(this, "New Value", d);
		listData.add(0,d);
		return undoredo;
	}
	
	public UndoRedo copyValue(int index){
		UndoRedo undoredo=this.pushValue(listData.get(index));
		return undoredo;
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
	public UndoRedo deleteValue(){
		return this.deleteValue(0);
	}
	public UndoRedo deleteValue(final int id){
		UndoRedo ret=null;
		if (listData.size()>id){
			final Double v=listData.remove(id);
			ret=new UndoRedoValuesClass(this, "delete") {
				private Double val=v;
				private int pos=id;
				@Override
				public void undo(DataModel dm) {
					super.privateUndo(dm);
					listData.add(pos, val);
				}
				
				@Override
				public void redo(DataModel dm) {
					super.privateRedo(dm);
					listData.remove(pos);
					
				}
			};
		}
		return ret;
	}
	
	protected Double popData(){
		Double r=this.listData.get(0);
		this.listData.remove(0);
		return r;
	}
	
	public UndoRedo operationAdd(){
		UndoRedo undoredo=null;
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			Double r=Double.valueOf(a+b);
			this.pushValue(r);
			undoredo=new TwoValuesUndoRedo(this, "Add", a, b, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationSubs(){
		UndoRedo undoredo=null;
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			Double r=Double.valueOf(a-b);
			this.pushValue(r);
			undoredo=new TwoValuesUndoRedo(this, "Subs", a, b, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationMinusX(){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(-v);
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "MinusX", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationMultiply(){
		UndoRedo undoredo=null;
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			Double r=Double.valueOf(a*b);
			this.pushValue(r);
			undoredo=new TwoValuesUndoRedo(this, "Mul", a, b, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationDivided(){
		UndoRedo undoredo=null;
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			Double r=Double.valueOf(a/b);
			this.pushValue(r);
			undoredo=new TwoValuesUndoRedo(this, "Div", a, b, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationInverse(){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(1/v);
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "Inverse", v, r);
		}	
		return undoredo;
	}
	
	public UndoRedo operationPowSquare(){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(v*v);
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "PowSquare", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationSqrt(){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(Math.sqrt(v));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "Sqrt", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationPow(){
		UndoRedo undoredo=null;
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			Double r=Double.valueOf(Math.pow(b,a));
			this.pushValue(r);
			undoredo=new TwoValuesUndoRedo(this, "Pow", a, b, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationLog(){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(Math.log10(v));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "Log", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationEPowX(){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(Math.exp(v));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "EPowX", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationLn(){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(Math.log(v));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "Ln", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationSin(ConversorInterface conv){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(Math.sin(conv.toProcess(v)));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "Sin", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationCos(ConversorInterface conv){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(Math.cos(conv.toProcess(v)));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "Cos", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationTan(ConversorInterface conv){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(Math.tan(conv.toProcess(v)));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "Tan", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationArcsin(ConversorInterface conv){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(conv.toShow(Math.asin(v)));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "ArcSin", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationArccos(ConversorInterface conv){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(conv.toShow(Math.acos(v)));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "ArcCos", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationArctan(ConversorInterface conv){
		UndoRedo undoredo=null;
		if (listData.size()>=1){
			Double v=this.popData();
			Double r=Double.valueOf(conv.toShow(Math.atan(v)));
			this.pushValue(r);
			undoredo=new OneValueUndoRedo(this, "ArcTan", v, r);
		}
		return undoredo;
	}
	
	public UndoRedo operationSwap(){
		UndoRedo undoredo=null;
		if (listData.size()>=2){
			Double a=this.popData();
			Double b=this.popData();
			this.pushValue(a);
			this.pushValue(b);
			undoredo=new UndoRedoValuesClass(this, "Swap") {
				
				@Override
				public void undo(DataModel dm) {
					super.privateUndo(dm);
					Double a=dm.popData();
					Double b=dm.popData();
					dm.pushValue(a);
					dm.pushValue(b);
				}
				
				@Override
				public void redo(DataModel dm) {
					super.privateUndo(dm);
					Double a=dm.popData();
					Double b=dm.popData();
					dm.pushValue(a);
					dm.pushValue(b);
				}
			};
		}
		return undoredo;
	}
	
	public List<Double> getListData() {
		return listData;
	}
}
