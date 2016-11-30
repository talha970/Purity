package fyp.teejay.apollo.fyp1;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Device_Reading extends AppCompatActivity {

    //Button btnOn, btnOff;

    CircleDisplay cdph,cdturb,cdtemp,cdpurity;
    Handler bluetoothIn;
FrameLayout progressbar;
    private ProgressDialog dialog;
    Handler progresshandler;
    final int handlerState = 0;        				 //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
Button finishbut;
    private ConnectedThread mConnectedThread;
    Readings myread;


    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device__reading);



        //Link the buttons and textViews to respective views
        //btnOn = (Button) findViewById(R.id.buttonOn);
        //btnOff = (Button) findViewById(R.id.buttonOff);
        

        progressbar=(FrameLayout) findViewById(R.id.progress_overlay);
        progressbar.setVisibility(View.VISIBLE);
        cdph=(CircleDisplay) findViewById(R.id.circleDisplayph);
        cdturb=(CircleDisplay) findViewById(R.id.circleDisplayturb);
        cdtemp=(CircleDisplay) findViewById(R.id.circleDisplaytemp);
        cdpurity=(CircleDisplay) findViewById(R.id.circleDisplayPurity);
        cdph.setUnit("");
        cdph.setTextSize(10.0f);
        cdturb.setUnit("Ntu");
        cdturb.setTextSize(10.0f);
        cdtemp.setUnit("C");
        cdtemp.setTextSize(10.0f);
        /*cdtemp.showValue(30.0f,100.0f,false);
        cdturb.showValue(8.0f,20.0f,false);
        cdph.showValue(7.0f,14.0f,false);*/
cdph.setTouchEnabled(false);
        cdturb.setTouchEnabled(false);
        cdtemp.setTouchEnabled(false);
        finishbut=(Button) findViewById(R.id.finishbut);
        /// /sensorView2 = (TextView) findViewById(R.id.sensorView2);
        //sensorView3 = (TextView) findViewById(R.id.sensorView3);
progresshandler =new Handler();
        progresshandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                progressbar.setVisibility(View.GONE);
                finishbut.setVisibility(View.VISIBLE);
                Random r=new Random();
if(myread!=null) {
    cdpurity.showValue(Float.parseFloat(myread.get_purity()), 100.0f, true);
}
}
        }, 10000);
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {										//if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);      								//keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        

                        if (recDataString.charAt(0) == '#')								//if it starts with # we know it is what we are looking for
                        {
                                    //get sensor value from string between indices 1-5
                            String sensortemp = recDataString.substring(1, 6);            //same again...
                            String sensorturb = recDataString.substring(7, 12);
                            String sensorph =recDataString.substring(13, 20);

                           // cdph.showValue(Float.valueOf(sensortemp),100.0f,false);
                            cdtemp.setColor(Color.WHITE);
                            cdtemp.showValue(Float.valueOf(sensortemp),100.0f,false);
                            cdturb.setColor(Color.WHITE);
                            cdturb.showValue(Float.valueOf(sensorturb),20.0f,false);
                            cdph.setColor(Color.WHITE);
                            cdph.showValue(Float.valueOf(sensorph),14.0f,false);
                            if(sensortemp!=null&& sensorturb!=null&&sensorph!=null){
                                myread=new Readings();
                                myread.setConductivity(sensortemp);
                                myread.setPh(sensorph);
                                myread.setTurbidity(sensorturb);
                                Date date=new Date();
                                String dt=new SimpleDateFormat("MM-dd-yyyy").format(date);
                                myread.setDate(dt);
                            }
                        }
                        recDataString.delete(0, recDataString.length()); 					//clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }

            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();


        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED
        /*btnOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("0");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
            }
        });

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("1");    // Send "1" via Bluetooth
                Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            if(btSocket!=null) {
                btSocket.close();
            }
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }



    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        public AsyncHttpTask(){
            dialog=new ProgressDialog(Device_Reading.this);}

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending readings to cloud");
            dialog.show();

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                int statusCode = urlConnection.getResponseCode();
                Log.d("statuscode",Integer.toString(statusCode));
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    result=1;
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("Error device reading", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI

            dialog.dismiss();

            if (result == 1) {

                Toast.makeText(Device_Reading.this, "Fetched!", Toast.LENGTH_SHORT).show();
                finish();

                //  recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(Device_Reading.this, "Failed to send data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readtocloud(View v){
       /* String strurl = "http://fypfinal20160320031344.azurewebsites.net/api/Readings/add/";
        StringBuilder url=new StringBuilder(strurl);
        Log.d("Cloud url",url.toString());
        new AsyncHttpTask().execute(url.toString());*/
        Intent i =new Intent(this,DeviceListActivity.class);
        i.putExtra("Readings", myread);
        startActivity(i);
        //url.append(Login_Info.dev_id.replace(":","")+"/"+Login_Info.getusername(this)+"/home/"+Login_Info.getLoc(this).getLatitude()+"/"+Login_Info.getLoc(this).getLongitude()+"/");


    }
}
