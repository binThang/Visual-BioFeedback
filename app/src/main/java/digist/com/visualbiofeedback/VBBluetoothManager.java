package digist.com.visualbiofeedback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by brianbae on 2016. 6. 1..
 */
public class VBBluetoothManager
{
    private static VBBluetoothManager s_instance = null;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;

    private ArrayList<String> mSelectedAddressList = new ArrayList<>();

    private DiscoveryCallback mDiscoveryCallback = null;
    public void setDiscoveryCallback(DiscoveryCallback callback)
    {
        mDiscoveryCallback = callback;
    }

    private VBBluetoothManager()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDeviceList = new ArrayList<>(mBluetoothAdapter.getBondedDevices());
    }

    public static VBBluetoothManager getInstance()
    {
        if (s_instance == null)
            s_instance = new VBBluetoothManager();
        return s_instance;
    }

    public void startDiscovery(Context context)
    {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();
    }

    public void selectDevice(int idx)
    {
        mSelectedAddressList.add(mDeviceList.get(idx).getAddress());
    }

    public void unSelectDevice(int idx)
    {
        String addr = mDeviceList.get(idx).getAddress();
        mSelectedAddressList.remove(addr);
    }

    public ArrayList<String> getSelectedAddressList()
    {
        return mSelectedAddressList;
    }

    public ArrayList<BluetoothDevice> getAvailableBlutoothDevices()
    {
        return mDeviceList;
    }

//    public ArrayList<BluetoothDevice> getDeviceList()
//    {
//        return mDeviceList;
//    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        ProgressDialog mDialog;

        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                //discovery starts, we can show progress dialog or perform other tasks
                mDialog = new ProgressDialog(context);
                mDialog.show();
                mDeviceList.clear();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                //discovery finishes, dismis progress dialog
                mDialog.dismiss();
                if (mDiscoveryCallback != null)
                    mDiscoveryCallback.discoveryFinished();
//                ((Activity)context).finish();
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device);
                Log.d("BLUETOOTH FOUND", "onReceive: found one");
            }
        }
    };

    public interface DiscoveryCallback
    {
        void discoveryFinished();
    }
}
