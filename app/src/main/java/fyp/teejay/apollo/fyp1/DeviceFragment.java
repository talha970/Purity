package fyp.teejay.apollo.fyp1;

/**
 * Created by teejay on 4/11/2016.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DeviceFragment extends Fragment {
Button login_but;
    Button started_but;
    FrameLayout noLoginlayout;
    FrameLayout Loginlayout;
    FrameLayout devreadlayout;
    LinearLayout progressbar;
    FloatingActionButton fab;
    CircleDisplay circle;
    TextView tvph;
    TextView tvturb;
    TextView tvtemp;
    TextView tvname;
    TextView tvadd;
    private ArrayList<Readings> data;
    protected BarChart mChart;
    String url = "http://fypfinal20160320031344.azurewebsites.net/Api/Readings/getdevreadings/0000";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


                View v=inflater.inflate(R.layout.device_layout,null);
        login_but=(Button) v.findViewById(R.id.button2);
        started_but=(Button) v.findViewById(R.id.button3);
       noLoginlayout = (FrameLayout) v.findViewById(R.id.layout_nologin);
        Loginlayout = (FrameLayout) v.findViewById(R.id.layout_login);
        devreadlayout = (FrameLayout) v.findViewById(R.id.devreadings);
        progressbar=(LinearLayout) v.findViewById(R.id.HeaderProgress);
        tvph=(TextView) v.findViewById(R.id.tvph);
        tvturb=(TextView) v.findViewById(R.id.tvturb);
        tvtemp=(TextView) v.findViewById(R.id.tvtemp);
        tvadd=(TextView) v.findViewById(R.id.tvlocadd);
        tvname=(TextView) v.findViewById(R.id.tvlocname);
        mChart = (BarChart) v.findViewById(R.id.chart);
circle=(CircleDisplay) v.findViewById(R.id.circleDisplay);
         fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent i=new Intent(getActivity(),DeviceListActivity.class);
                getActivity().startActivity(i);
            }
        });
        changeLoginView();
      login_but_list();
        started_but_list();
        return v;

    }
    public void started_but_list(){
        started_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i=new Intent(getActivity(),DeviceIntro.class);
                startActivityForResult(i,999);
            }
        });
    }

    public void login_but_list(){
    login_but.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            //Toast.makeText(getActivity(), "Hello World", Toast.LENGTH_LONG).show();
            Intent i=new Intent(getActivity(),LoginActivity.class);
            startActivityForResult(i,2);
        }
    });
}

public void changeLoginView(){
    //Toast.makeText(getActivity(),Boolean.toString(Login_Info.loggedin),Toast.LENGTH_LONG).show();
    if(Login_Info.getloggedin(getActivity())){
        if(Login_Info.getIsPaired(getActivity())){
            Loginlayout.setVisibility(View.GONE);
            progressbar.setVisibility(View.VISIBLE);
            new AsyncHttpTask().execute(url);



        }
        else {
            noLoginlayout.setVisibility(View.GONE);
            Loginlayout.setVisibility(View.VISIBLE);
        }
    }
    else{
        noLoginlayout.setVisibility(View.VISIBLE);
        Loginlayout.setVisibility(View.GONE);
    }
}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            if (resultCode == Activity.RESULT_OK) {

                if (data.getExtras().containsKey("Username")) {

                    changeLoginView();
                }
            }
        }
        else if(requestCode==999){
            changeLoginView();

        }
    }
    private void setData(int count, float range) {
        ArrayList<String> labels = new ArrayList<String>();
       /* labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");*/
        // create BarEntry for group 1
        ArrayList<BarEntry> group1 = new ArrayList<>();
        int counter=0;
        for(int i=data.size()-1;i>=0;i--){
            labels.add(data.get(i).get_date());
            group1.add(new BarEntry(Float.parseFloat(data.get(i).get_purity()), counter));
            counter++;
        }
       /* for(Readings item:data){
            if(data!=null) {
                labels.add(item.get_date());
                group1.add(new BarEntry(Float.parseFloat(item.get_purity()), counter));
                counter++;
            }

        }*/
       /* group1.add(new BarEntry(4f, 0));
        group1.add(new BarEntry(8f, 1));
        group1.add(new BarEntry(6f, 2));
        group1.add(new BarEntry(12f, 3));
        group1.add(new BarEntry(18f, 4));
        group1.add(new BarEntry(9f, 5));*/

        // create BarEntry for group 1
        ArrayList<BarEntry> group2 = new ArrayList<>();
        group2.add(new BarEntry(6f, 0));
        group2.add(new BarEntry(7f, 1));
        group2.add(new BarEntry(8f, 2));
        group2.add(new BarEntry(12f, 3));
        group2.add(new BarEntry(15f, 4));
        group2.add(new BarEntry(10f, 5));

        BarDataSet barDataSet1 = new BarDataSet(group1, "Purity Readings");  // creating dataset for group1
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(group2, "Brand 2"); // creating dataset for group1
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        BarData data = new BarData(labels,  barDataSet1); // initialize the Bardata with argument labels and dataSet
        mChart.setData(data);
        mChart.animateY(5000);

    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        public AsyncHttpTask(){
            data=new ArrayList<>();
           }

        @Override
        protected void onPreExecute() {



        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("Error device frag", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
           progressbar.setVisibility(View.GONE);
            devreadlayout.setVisibility(View.VISIBLE);
            if (result == 1) {
                Log.d("response", data.toString());
               /* for(Markers m:data.values()){
                    m.setPurity(m.calcpurity());
                }*/

              //  adapter.notifyDataSetChanged();

                Readings read=data.get(0);

                tvph.setText(read.get_ph());
                tvturb.setText(read.get_turbudity());
                tvtemp.setText(read.get_cond());
                tvname.setText(read.get_locname());
                tvadd.setText(read.get_locadd());
             circle.showValue(Float.parseFloat(read.get_purity()),100f,true);
                circle.setTouchEnabled(false);
                setData(12, 50);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            data.size();
        }
    }

    private void parseResult(String result) {
        try {
//            JSONObject response = new JSONObject(result);
            JSONArray posts = new JSONArray(result);//response.optJSONArray("posts");

            Log.d("json",posts.toString());
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                Markers item = new Markers();
                Readings myReadings=new Readings();
               /* item.setName(post.optString("name"));
                item.setAddress(post.optString("address"));
                item.setCity(post.optString("city"));
                item.setLat(post.optString("lat"));
                item.setLon(post.optString("lon"));
                item.setId(post.optString("markerId"));*/

                myReadings.setId(post.optString("ReadingId"));
                myReadings.setPh(post.optString("pH"));
                myReadings.setTurbidity(post.optString("Turbidity"));
                myReadings.setConductivity(post.optString("Conductivity"));
                String s=post.optString("Date_Time").substring(0,10);
                myReadings.setDate(post.optString("Date_Time").substring(0,10));
                myReadings.setlocname(post.optString("name"));
                myReadings.setlocadd(post.optString("address"));
                myReadings.setPurity(myReadings.calcpurity());
                data.add(myReadings);
             /*  if(!data.containsKey(item.getId())) { // if marker already present
                    ArrayList<Readings> r=new ArrayList<>() ;
                    if(!myReadings.get_id().equals("null"))
                    {
                        myReadings.setPurity(myReadings.calcpurity());
                        r.add(myReadings);
                        item.setreads(r);

                    }
                    data.put(item.getId(), item);

                    Log.d("added at ",data.get(item.getId()).getName());
                }
               else{
                   if(!myReadings.get_id().equals("null")) {
                        myReadings.setPurity(myReadings.calcpurity());
                        data.get(String.valueOf(item.getId())).getreads().add(myReadings); //get that markers array and add to it

                   }
               }*/
            }
        } catch (JSONException e) {
            Log.d("device frag","exception");
            e.printStackTrace();
        }

    }
}
