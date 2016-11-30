package fyp.teejay.apollo.fyp1;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Markers marker;
    ArrayList<Readings> read;
    TextView tv_name;
    TextView tv_city;
    TextView tv_totalread;
    CircleDisplay cd;
    ListView lv;
    private ReadingAdapter m_adapter;
    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
lv=(ListView) findViewById(R.id.listview);
        lineChart = (LineChart) findViewById(R.id.chart);
        tv_name= (TextView)(findViewById(R.id.tv_name));
        tv_city = (TextView)(findViewById(R.id.tv_city));
        tv_totalread = (TextView)(findViewById(R.id.tvtotalread));
        cd=(CircleDisplay) findViewById(R.id.circleDisplay);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        marker = (Markers)i.getSerializableExtra("Markerobj");
        read=marker.getreads();
        loadData();
        cd.setTextSize(20.0f);
        cd.setTouchEnabled(false);
        cd.showValue(Float.parseFloat(marker.calcpurity()), 100f, true);
        tv_name.setText(marker.getName());
        tv_city.setText(marker.getCity());
        if(read==null){tv_totalread.setText("(0 Readings)");}
        else {
            tv_totalread.setText("("+read.size() + " Readings)");
        }
        if(read!=null){
        m_adapter = new ReadingAdapter(this, R.layout.readings_list, read);
        lv.setAdapter(m_adapter);}
    }
public void loadData(){
    ArrayList<String> labels = new ArrayList<String>();
    ArrayList<Entry> entries = new ArrayList<>();
    if(read!=null) {
        for (int i = read.size() - 1; i >= 0; i--) {
            labels.add(read.get(i).get_date().substring(0, 10));
            entries.add(new Entry(Float.parseFloat(read.get(i).get_purity()), i));

        }
    }
    LineDataSet dataset = new LineDataSet(entries, "Readings");
    dataset.setDrawFilled(true);
    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
    LineData data = new LineData(labels, dataset);

    lineChart.setData(data); // set the data and list of lables into chart
}

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(marker.getLat()),(Double.parseDouble(marker.getLon())));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Water Source"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
