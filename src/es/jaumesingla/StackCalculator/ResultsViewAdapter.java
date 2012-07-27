package es.jaumesingla.StackCalculator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

/**
 *
 * @author Jaume Singla Valls
 */
public class ResultsViewAdapter extends BaseAdapter {
	private static class ViewHolder {
		public TextView textView;
		public TextView labelPos;
	}
		 
	//private ArrayList<String> mData = new ArrayList<String>();
	private DataModel mData;
	private List<Double> mList;
	private LayoutInflater mInflater;

	public ResultsViewAdapter(LayoutInflater i) {
		mInflater = i;
		mData=DataModel.getInstance();
		mList=mData.getListData();
	}

	@Override
	public int getCount() {
		//Log.d("MyCustomAdapter", "getCount"+Integer.toString(mData.size()));
		if (mData.hasNewValue()){
			return mList.size()+1;
		} else 
			return mList.size();
	}

	@Override
	public String getItem(int position) {
		if (!mData.hasNewValue())
			return mList.get(position).toString();
		else {
			if (position==0){
				return mData.getNewValue();
			} else 
				return mList.get(position-1).toString();
		}
			
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
		
		if (mData.hasNewValue()){
			if (position==0){
				holder.textView.setText(mData.getNewValue());
				holder.labelPos.setText(" <<<");
				return convertView;
			}
		}
		
		int rCount=mList.size();
		
		holder.textView.setText(this.getItem(position));

		String posText="   "+Integer.toString(rCount-position);
		holder.labelPos.setText(":"+posText.subSequence(posText.length()-3, posText.length()));
		return convertView;
	}
	
	//@Override 
    public void onItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}
