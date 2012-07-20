package es.jaumesingla.StackCalculator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 *
 * @author dracks
 */
public class ResultsViewAdapter extends BaseAdapter {
	private static class ViewHolder {
		public TextView textView;
		public TextView labelPos;
	}
		 
	private ArrayList<String> mData = new ArrayList<String>();
	private LayoutInflater mInflater;
	private String newValue=null;

	public ResultsViewAdapter(LayoutInflater i) {
		mInflater = i;
	}

	public void addItem(final String item) {
		Log.d("MyCustomAdapter", item);
		mData.add(item);
		notifyDataSetChanged();
	}

	public void addFirstItem(final String item) {
		Log.d("MyCustomAdapter", item);
		mData.add(0, item);
		notifyDataSetChanged();
	}

	public void removeItem(int p){
		mData.remove(p);
		notifyDataSetChanged();
	}

	public void setNewValue(String s){
		newValue=s;
		notifyDataSetChanged();
	}

	public void pushNewValue(){
		mData.add(0,newValue);
		newValue=null;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		Log.d("MyCustomAdapter", "getCount"+Integer.toString(mData.size()));
		return mData.size();
	}

	@Override
	public String getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView " + position + " " + convertView);
		Log.d("MyCustomAdapter-getView", "getView " + position + " " + convertView);
		ViewHolder holder = null;
		if (convertView == null) {
			Log.d("MyCustomAdapter-getView", "Layout: " + R.layout.valuecell);
			convertView = mInflater.inflate(R.layout.valuecell, null);
			assert(convertView!=null);
			holder = new ViewHolder();
			holder.textView = (TextView)convertView.findViewById(R.id.text);
			holder.labelPos = (TextView)convertView.findViewById(R.id.labelText);
			assert(holder.textView!=null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		if (newValue!=null){
			position--;
			if (position<0){
				holder.textView.setText(newValue);
					holder.labelPos.setText("<<<<");
				return convertView;
			}
		}
		holder.textView.setText(mData.get(position));

		String posText="   "+Integer.toString(position);
		holder.labelPos.setText(":"+posText.subSequence(posText.length()-3, posText.length()));
		return convertView;
	}
	
	//@Override 
    public void onItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}
