package digist.com.visualbiofeedback;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.shimmerresearch.android.Shimmer;
import com.shimmerresearch.bluetooth.ShimmerBluetooth;
import com.shimmerresearch.driver.Configuration;
import com.shimmerresearch.driver.FormatCluster;
import com.shimmerresearch.driver.ObjectCluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by brianbae on 2016. 6. 6..
 */
public class VBShimmerActivity extends ActionBarActivity
{
    private ArrayList<String> mShimmerAddressList;
    private ArrayList<Shimmer> mShimmerList = new ArrayList<>();

    private Shimmer mShimmerDevice1 = null;
    private Shimmer mShimmerDevice2 = null;

    public TextView tvAccelX;
    public TextView tvAccelY;
    public TextView tvAccelZ;

    public TextView tvGyroX;
    public TextView tvGyroY;
    public TextView tvGyroZ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shimmer);

//        tvAccelX = (TextView)findViewById(R.id.tv_accel_x);
//        tvAccelY = (TextView)findViewById(R.id.tv_accel_y);
//        tvAccelZ = (TextView)findViewById(R.id.tv_accel_z);
//
//        tvGyroX = (TextView)findViewById(R.id.tv_gyro_x);
//        tvGyroY = (TextView)findViewById(R.id.tv_gyro_y);
//        tvGyroZ = (TextView)findViewById(R.id.tv_gyro_z);

        mShimmerAddressList = VBBluetoothManager.getInstance().getSelectedAddressList();
        initShimmers();
    }

    private void initShimmers()
    {
        if (mShimmerAddressList.size() == 1)
        {
            Shimmer temp = new Shimmer(this, mHandler, "Shimmer1", 204.8, 0, 0, Shimmer.SENSOR_ACCEL | Shimmer.SENSOR_GYRO, false);
            temp.connect(mShimmerAddressList.get(0), "default");
            mShimmerList.add(temp);
            mShimmerDevice1 = temp;
        }
        else
            return;
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        mShimmerDevice1.stopStreaming();
        mShimmerDevice1.stop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mShimmerDevice1.stopStreaming();
        mShimmerDevice1.stop();
    }

    private final Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            { // handlers have a what identifier which is used to identify the type of msg
                case Shimmer.MESSAGE_READ:
                    if ((msg.obj instanceof ObjectCluster))
                    {	// within each msg an object can be include, objectclusters are used to represent the data structure of the shimmer device
                        ObjectCluster objectCluster =  (ObjectCluster) msg.obj;
                        FormatCluster formatCluster;

                        Collection<FormatCluster> accelXFormats = objectCluster.mPropertyCluster.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_X);  // first retrieve all the possible formats for the current sensor device
                        formatCluster = ((FormatCluster)ObjectCluster.returnFormatCluster(accelXFormats,"CAL")); // retrieve the calibrated data
                        if (formatCluster!=null)
                        {
//                            tvAccelX.setText(objectCluster.mMyName + " AccelLNX: " + formatCluster.mData + " " + formatCluster.mUnits);
                        }

                        Collection<FormatCluster> accelYFormats = objectCluster.mPropertyCluster.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_Y);
                        formatCluster = ((FormatCluster)ObjectCluster.returnFormatCluster(accelYFormats,"CAL")); // retrieve the calibrated data
                        if (formatCluster!=null)
                        {
//                            tvAccelY.setText(objectCluster.mMyName + " AccelLNY: " + formatCluster.mData + " " + formatCluster.mUnits);
                        }

                        Collection<FormatCluster> accelZFormats = objectCluster.mPropertyCluster.get(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_Z);
                        formatCluster = ((FormatCluster) ObjectCluster.returnFormatCluster(accelZFormats, "CAL")); // retrieve the calibrated data
                        if (formatCluster!=null)
                        {
//                            tvAccelZ.setText(objectCluster.mMyName + " AccelLNZ: " + formatCluster.mData + " " + formatCluster.mUnits);
                        }

                        Collection<FormatCluster> gyroXFormats = objectCluster.mPropertyCluster.get(Configuration.Shimmer3.ObjectClusterSensorName.GYRO_X);
                        formatCluster = ((FormatCluster) ObjectCluster.returnFormatCluster(gyroXFormats, "CAL")); // retrieve the calibrated data
                        if (formatCluster!=null)
                        {
//                            tvGyroX.setText(objectCluster.mMyName + " GyroX: " + formatCluster.mData + " " + formatCluster.mUnits);
                        }

                        Collection<FormatCluster> gyroYFormats = objectCluster.mPropertyCluster.get(Configuration.Shimmer3.ObjectClusterSensorName.GYRO_Y);
                        formatCluster = ((FormatCluster) ObjectCluster.returnFormatCluster(gyroYFormats, "CAL")); // retrieve the calibrated data
                        if (formatCluster!=null)
                        {
//                            tvGyroY.setText(objectCluster.mMyName + " GyroY: " + formatCluster.mData + " " + formatCluster.mUnits);
                        }

                        Collection<FormatCluster> gyroZFormats = objectCluster.mPropertyCluster.get(Configuration.Shimmer3.ObjectClusterSensorName.GYRO_Z);
                        formatCluster = ((FormatCluster) ObjectCluster.returnFormatCluster(gyroZFormats, "CAL")); // retrieve the calibrated data
                        if (formatCluster!=null)
                        {
//                            tvGyroZ.setText(objectCluster.mMyName + " GyroZ: " + formatCluster.mData + " " + formatCluster.mUnits);
                        }
                    }
                    break;
                case Shimmer.MESSAGE_TOAST:
                    Log.d("toast", msg.getData().getString(Shimmer.TOAST));
                    Toast.makeText(getApplicationContext(), msg.getData().getString(Shimmer.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;

                case Shimmer.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1)
                    {
                        case Shimmer.MSG_STATE_FULLY_INITIALIZED:
                            if (mShimmerDevice1.getShimmerState()==Shimmer.STATE_CONNECTED)
                            {
                                Log.d("ConnectionStatus", "Successful");
//                                mShimmerDevice1.startStreaming();
//                                mShimmerDevice1.startDataLogAndStreaming();
//                                shimmerTimer(30); //Disconnect in 30 seconds
                                Shimmer temp2 = new Shimmer(VBShimmerActivity.this, mHandler, "Shimmer2", 204.8, 0, 0, Shimmer.SENSOR_ACCEL | Shimmer.SENSOR_GYRO, false);
                                temp2.connect(mShimmerAddressList.get(1), "default");
                                mShimmerList.add(temp2);
                                mShimmerDevice2 = temp2; // test

                            }

                            if (mShimmerDevice1.getShimmerState() == Shimmer.STATE_CONNECTED &&
                                    mShimmerDevice2.getShimmerState() == ShimmerBluetooth.STATE_CONNECTED)
                            {
                                mShimmerDevice1.startDataLogAndStreaming();
                                mShimmerDevice2.startDataLogAndStreaming();
                            }

                            break;
                        case Shimmer.STATE_CONNECTING:
                            Log.d("ConnectionStatus","Connecting");
                            break;
                        case Shimmer.STATE_NONE:
                            Log.d("ConnectionStatus","No State");
                            break;
                    }
                    break;
            }
        }
    };
}
