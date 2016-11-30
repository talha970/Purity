package fyp.teejay.apollo.fyp1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by teejay on 4/18/2016.
 */
public class Introfrag1 extends Fragment {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private ProgressDialog mProgressDlg;
ListView mListView;
    private DeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    private BluetoothSocket btSocket = null;
  //  private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;
    private BluetoothAdapter mBluetoothAdapter;
    public static Introfrag1 newInstance(int layoutResId) {
        Introfrag1 Introfrag1 = new Introfrag1();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        Introfrag1.setArguments(args);

        return Introfrag1;
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }
    private int layoutResId;

    public Introfrag1() {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
       // Toast.makeText(getActivity(), "BlueTooth Turned On", Toast.LENGTH_LONG).show();
        if(resultCode== Activity.RESULT_OK){
            mBluetoothAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            getActivity().registerReceiver(mReceiver, filter);


        }
    }
    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        super.onPause();
    }
    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        getActivity().unregisterReceiver(mPairReceiver);

        super.onDestroy();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        mDeviceList = new ArrayList<BluetoothDevice>();
        mAdapter		= new DeviceListAdapter(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v= inflater.inflate(layoutResId, container, false);
        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
        mListView=(ListView) v.findViewById(R.id.listViewDetected);

        mProgressDlg 		= new ProgressDialog(getActivity());
        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mAdapter.setData(mDeviceList);
        mAdapter.setListener(new DeviceListAdapter.OnPairButtonClickListener() {
            @Override
            public void onPairButtonClick(int position) {
                BluetoothDevice device = mDeviceList.get(position);

                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unpairDevice(device);
                } else {
                    showToast("Pairing...");

                    pairDevice(device);
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        try {
                            btSocket = createBluetoothSocket(device);
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Socket creation failed", Toast.LENGTH_LONG).show();
                        }
                        // Establish the Bluetooth socket connection.
                        try
                        {
                            mBluetoothAdapter.cancelDiscovery();
                            btSocket.connect();
                            Toast.makeText(getActivity(), "Connected", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            try
                            {
                                btSocket.close();
                            } catch (IOException e2)
                            {
                                //insert code to deal with this
                            }
                        }
                    }
                }
            }
        });

        mListView.setAdapter(mAdapter);

        getActivity().registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        startActivityForResult(intent, 3);
        return v;
    }
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();
                //mListView.invalidateViews();
                mAdapter.notifyDataSetChanged();


            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
 Log.d("Device Name",device.getName());
                if(device.getName().toLowerCase().contains("purity")) {
     mDeviceList.add(device);
 }

            }
        }
    };

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    showToast("Paired");
                    Login_Info.setisPaired(getActivity(),true);
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Login_Info.dev_name=device.getName();
                    Login_Info.dev_id=device.getAddress();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    showToast("Unpaired");
                    Login_Info.setisPaired(getActivity(),false);
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
