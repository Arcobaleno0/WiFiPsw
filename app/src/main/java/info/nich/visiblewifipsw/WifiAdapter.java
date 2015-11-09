package info.nich.visiblewifipsw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.nich.visiblewifipsw.R;
import info.nich.visiblewifipsw.WiFi;

public class WifiAdapter extends BaseAdapter {
    private List<WiFi> dataList;
    private LayoutInflater mInflater;
    private Context mContext;

    public WifiAdapter(Context context, List<WiFi> arrayList) {
        dataList = arrayList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

//    public WifiAdapter(List<WiFi> arrayList) {
//        dataList = arrayList;
//    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Using convertView and ViewHolder to avoid repeat layout loading.
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.ssidTv = (TextView) view.findViewById(R.id.ssidTv);
            holder.pswTv = (TextView) view.findViewById(R.id.pswTv);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.ssidTv.setText(dataList.get(position).getSsid());
        holder.pswTv.setText(dataList.get(position).getPsw());
        return view;
    }

    class ViewHolder {
        TextView ssidTv, pswTv;
    }
}
