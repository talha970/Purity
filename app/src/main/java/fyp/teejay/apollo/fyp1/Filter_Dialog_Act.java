package fyp.teejay.apollo.fyp1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Filter_Dialog_Act extends Activity {
    ListView listView ;
    public  HashMap<String,Markers> data;
    ArrayList<Markers> markerlist;
    private ProgressDialog dialog;
    Readings read;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter__dialog_);
        markerlist=new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        data=MarkerFragment.data;
        Intent i = getIntent();
        read = (Readings) i.getSerializableExtra("Readings");
        for(Markers marks:data.values()){
            markerlist.add(marks);

        }
        Dialogistadapter adapter=new Dialogistadapter(this,markerlist);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                Markers  itemValue    = (Markers) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue.getId() , Toast.LENGTH_LONG)
                        .show();
                String strurl = "http://fypfinal20160320031344.azurewebsites.net/api/Readings/add/";
                StringBuilder url=new StringBuilder(strurl);
/*read=new Readings();
               read.setConductivity("30");
                read.setPh("6");
                read.setTurbidity("5");
                Date date=new Date();
                String dt=new SimpleDateFormat("MM-dd-yyyy").format(date);

                read.setDate(String.valueOf(date.getDate())+"-"+String.valueOf(date.getMonth())+"-20"+String.valueOf(date.getYear()));*/
url.append("0000"+"/"+read.get_ph()+"/"+read.get_cond()+"/"+read.get_turbudity()+"/"+read.get_date()+"/"+itemValue.getId()+"/");
                Log.d("Cloud url",url.toString());
                new AsyncHttpTask().execute(url.toString());
            }

        });
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        public AsyncHttpTask(){
            dialog=new ProgressDialog(Filter_Dialog_Act.this);}

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
                Log.d("Error sending cloud", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI

            dialog.dismiss();

            if (result == 1) {

                Toast.makeText(Filter_Dialog_Act.this, "Data sent to cloud", Toast.LENGTH_SHORT).show();
                finish();

                //  recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(Filter_Dialog_Act.this, "Failed to send data to cloud!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}


