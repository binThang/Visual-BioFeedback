package digist.com.visualbiofeedback;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by brianbae on 2016. 6. 1..
 */
public class VBBluetoothListAdapter extends BaseAdapter implements VBBluetoothManager.DiscoveryCallback
{
    private ArrayList<BluetoothDevice> mList = VBBluetoothManager.getInstance().getAvailableBlutoothDevices();
    private Context mContext = null;

    public VBBluetoothListAdapter(Context context)
    {
        mContext = context;
        VBBluetoothManager.getInstance().setDiscoveryCallback(this);
    }

    @Override
    public int getCount()
    {
        return mList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        final int idx = i;
        ViewHolder holder;
        if (view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.listitem_bluetooth, null);

            holder = new ViewHolder();
            holder.mName = (TextView)view.findViewById(R.id.text_name);
            holder.mAdress = (TextView)view.findViewById(R.id.text_address);
            holder.mChecked = (CheckBox)view.findViewById(R.id.cb_check);

            view.setTag(holder);
        }
        else
            holder = (ViewHolder)view.getTag();

        BluetoothDevice device = mList.get(i);

        holder.mName.setText(device.getName());
        holder.mAdress.setText(device.getAddress());
        holder.mChecked.setChecked(false);
        holder.mChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                    VBBluetoothManager.getInstance().selectDevice(idx);
                else
                    VBBluetoothManager.getInstance().unSelectDevice(idx);
            }
        });

        return view;
    }

    @Override
    public void discoveryFinished()
    {
        notifyDataSetChanged();
    }

    private class ViewHolder
    {
        public TextView mName;
        public TextView mAdress;
        public CheckBox mChecked;
    }
}
