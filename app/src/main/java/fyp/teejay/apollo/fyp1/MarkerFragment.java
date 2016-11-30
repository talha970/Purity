package fyp.teejay.apollo.fyp1;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fyp.teejay.apollo.fyp1.dummy.DummyContent;
import fyp.teejay.apollo.fyp1.dummy.DummyContent.DummyItem;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MarkerFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    Location mLastLocation;
    float myLat,myLon;
    public static HashMap<String,Markers> data;
    RecyclerView recyclerView;
    LinearLayout progressbar;

    MyMarkerRecyclerViewAdapter adapter;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
            @Override
            public void onStop() {
                mGoogleApiClient.disconnect();
                super.onStop();
            }private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MarkerFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MarkerFragment newInstance(int columnCount) {
        MarkerFragment fragment = new MarkerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.markerfragmenu, menu);
       // menu.findItem(R.id.itemPurity).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch(item.getItemId()){

            case R.id.itemPurity:
            if (item.isChecked()) item.setChecked(false);
            else item.setChecked(true);
            sortPurity();

                adapter.notifyDataSetChanged();
return true;


            case R.id.itemDist:

                sortDist();
                adapter.notifyDataSetChanged();

                if (item.isChecked()) item.setChecked(false);
                    else item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setHasOptionsMenu(true);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        // Set the adapter
       // if (view instanceof RecyclerView) {
            Context context = view.getContext();
//            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            data=new HashMap<>();
progressbar=(LinearLayout) view.findViewById(R.id.HeaderProgress);
adapter=new MyMarkerRecyclerViewAdapter(data, mListener);
         recyclerView.setAdapter(adapter);

            final String url = "http://fypfinal20160320031344.azurewebsites.net/Api/markers";

            cd=new ConnectionDetector(getActivity());
            isInternetPresent = cd.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {
                // Internet Connection is Present
                // make HTTP requests
                new AsyncHttpTask().execute(url);
            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

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
           // }

        }
        return view;
    }
public void sortDist(){
    ArrayList<Markers> mList=new ArrayList((data.values()));

    Collections.sort(mList, new Comparator<Markers>() {

        public int compare(Markers o1, Markers o2) {
            float change1 = Float.valueOf(o1.getdist());
            float change2 = Float.valueOf(o2.getdist());
            if (change1 < change2) return -1;
            if (change1 > change2) return 1;
            return 0;

        }
    });
    data.clear();
    for(int i=0;i<mList.size();i++){
        data.put(String.valueOf(i),mList.get(i));
    }
}
public void sortPurity(){
    ArrayList<Markers> mList=new ArrayList((data.values()));

    Collections.sort(mList, new Comparator<Markers>() {

        public int compare(Markers o1, Markers o2) {
            float change1 = Float.valueOf(o1.getPurity());
            float change2 = Float.valueOf(o2.getPurity());
            if (change1 < change2) return 1;
            if (change1 > change2) return -1;
            return 0;
        }
    });
    data.clear();
    for(int i=0;i<mList.size();i++){
        data.put(String.valueOf(i),mList.get(i));
    }
}
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                        if (mLastLocation != null) {
                            myLat=(float)mLastLocation.getLatitude();
                            myLon=(float)mLastLocation.getLongitude();
                            Login_Info.setloc(getActivity(),mLastLocation);

                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    getActivity(),
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Markers item);
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        public AsyncHttpTask(){
        //dialog=new ProgressDialog(getActivity());
            }

        @Override
        protected void onPreExecute() {
           /* dialog.setMessage("Please wait...");
            dialog.show();*/
            progressbar.setVisibility(View.VISIBLE);


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
//                Log.d("Error marker frag", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
          // dialog.dismiss();
            progressbar.setVisibility(View.GONE);

            if (result == 1) {
                Log.d("response", data.toString());
for(Markers m:data.values()){
    m.setPurity(m.calcpurity());
}
               /* ArrayList<Markers> mList=new ArrayList((data.values()));

                Collections.sort(mList, new Comparator<Markers>() {

                    public int compare(Markers o1, Markers o2) {
                        float change1 = Float.valueOf(o1.getPurity());
                        float change2 = Float.valueOf(o2.getPurity());
                        if (change1 < change2) return 1;
                        if (change1 > change2) return -1;
                        return 0;

                    }
                });
                data.clear();
for(int i=0;i<mList.size();i++){
    data.put(String.valueOf(i),mList.get(i));
}*/
                adapter.notifyDataSetChanged();
             //  recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static Map<String, Markers> sortByComparator(Map<String, Markers> unsortMap, final boolean order)
    {

        List<Map.Entry<String, Markers>> list = new LinkedList<Map.Entry<String, Markers>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Markers>>()
        {
            public int compare(Map.Entry<String,Markers> o1,
                               Map.Entry<String, Markers> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Markers> sortedMap = new LinkedHashMap<String, Markers>();
        for (Map.Entry<String, Markers> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
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
                item.setName(post.optString("name"));
                item.setAddress(post.optString("address"));
                item.setCity(post.optString("city"));
                item.setLat(post.optString("lat"));
                item.setLon(post.optString("lon"));
                item.setId(post.optString("markerId"));
                if(mLastLocation!=null){
                item.setdist(item.calcdist(mLastLocation));}
                myReadings.setId(post.optString("ReadingId"));
                myReadings.setPh(post.optString("pH"));
                myReadings.setTurbidity(post.optString("Turbidity"));
                myReadings.setConductivity(post.optString("Conductivity"));
                myReadings.setDate(post.optString("Date_Time"));


                if(!data.containsKey(item.getId())) { // if marker already present
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
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
