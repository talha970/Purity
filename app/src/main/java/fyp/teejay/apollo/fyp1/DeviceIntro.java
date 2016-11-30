package fyp.teejay.apollo.fyp1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by teejay on 4/18/2016.
 */
public class DeviceIntro extends AppIntro2 {
    private ProgressDialog dialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.

        /*addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);*/


        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        //addSlide(AppIntroFragment.newInstance("Connect your Device", "Select your device from the bluetooth menu", R.drawable.ic_media_route_on_1_mono_dark, R.color.colorPrimary));
        addSlide(Introfrag1.newInstance(R.layout.intro1));
        //askForPermissions(new String[]{Manifest.permission.BLUETOOTH},1);
        // OPTIONAL METHODS
        // Override bar/separator color.

        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        /*setVibrate(true);
        setVibrateIntensity(30);*/
    }



    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        if(Login_Info.getIsPaired(DeviceIntro.this)){
             String strurl = "http://fypfinal20160320031344.azurewebsites.net/api/Devices/add/";
            StringBuilder url=new StringBuilder(strurl);

            url.append(Login_Info.dev_id.replace(":","")+"/"+Login_Info.getusername(this)+"/home/"+Login_Info.getLoc(this).getLatitude()+"/"+Login_Info.getLoc(this).getLongitude()+"/");
Log.d("Device url",url.toString());

            cd=new ConnectionDetector(this);
            isInternetPresent = cd.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {
                // Internet Connection is Present
                // make HTTP requests
                new AsyncHttpTask().execute(url.toString());
            }/* else {
                // Internet connection is not present
                // Ask user to connect to Internet
                AlertDialog alertDialog = new AlertDialog.Builder(DeviceIntro.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("FYP");

                // Setting Dialog Message
                alertDialog.setMessage("Please Check your Internet Connection");

                // Setting alert dialog icon
                // alertDialog.setIcon();

                // Setting OK Button
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                // Showing Alert Message
                alertDialog.show();
            }*/
            //finish();
        }
        else{
            Toast.makeText(this, "Please Pair your device first!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        public AsyncHttpTask(){
            dialog=new ProgressDialog(DeviceIntro.this);}

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Registering your device");
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
                Log.d("Error device register", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI

            dialog.dismiss();

            if (result == 1) {

                Toast.makeText(DeviceIntro.this, "Fetched!", Toast.LENGTH_SHORT).show();
                finish();

                //  recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(DeviceIntro.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }



}