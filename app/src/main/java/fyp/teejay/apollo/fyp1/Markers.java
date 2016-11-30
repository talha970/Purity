package fyp.teejay.apollo.fyp1;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by teejay on 3/9/2016.
 */
public class Markers implements Serializable,Comparable<Markers>{
    private String name;
    private String address;
    private String lat;
    private String lon;
    private String city;
    private String purity;
    private String id;
    private float dist;
    private ArrayList<Readings> read;
    public Markers() {

    }
public void setreads(ArrayList<Readings> r){
    this.read=r;

}
    public ArrayList<Readings> getreads(){
        return this.read;

    }
    //Getters and setters
    public String getName() {
        return name;
    }
    public float getdist() {
        return dist;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setdist(float dist) {
        this.dist =dist;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPurity() {
        return purity;
    }
    public void setPurity(String purity) {
        this.purity = purity;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String calcpurity(){
        float sum=0;
        String avg="0";
        if(read!=null) {

            for (Readings item : read) {
                sum = sum + Float.parseFloat(item.get_purity());

            }
            avg= String.valueOf(sum/read.size());
        }

if(Float.parseFloat(avg)>100){
    avg=String.valueOf(100);
}
        return avg;
    }
    public float calcdist(Location myloc){
        float ans=0.0f;

        Location markerloc=new Location("");
        markerloc.setLatitude(Double.valueOf(this.lat));
        markerloc.setLongitude(Double.valueOf(this.lon));
        ans=markerloc.distanceTo(myloc);
        ans=ans/1000;
    return ans;
    }

    @Override
    public int compareTo(Markers markers) {
        return 0;
    }
}