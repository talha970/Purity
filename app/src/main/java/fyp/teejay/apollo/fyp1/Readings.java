package fyp.teejay.apollo.fyp1;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Random;

/**
 * Created by teejay on 3/23/2016.
 */
public class Readings implements Serializable{
    String id;
    String turbidity;
    String conductivity;
    String ph;
    String date;
    String purity;
    String locname;
    String locaddress;
    public String  get_id(){
        return this.id;
    }
    public String  get_turbudity(){
        return this.turbidity;
    }
    public String  get_ph(){
        return this.ph;
    }
    public String  get_cond(){
        return this.conductivity;
    }
    public String  get_date(){
        return this.date;
    }
    public String  get_purity(){
        return this.purity;
    }
    public String  get_locname(){
        return this.locname;
    }
    public String  get_locadd(){
        return this.locaddress;
    }
    public void setId(String ID){this.id=ID;}
    public void setTurbidity(String turb){this.turbidity=turb;}
    public void setPh(String ph){this.ph=ph;}
    public void setDate(String date){this.date=date;}
    public void setConductivity(String cond){this.conductivity=cond;}
    public void setPurity(String pur){this.purity=pur;}
    public void setlocname(String locn){this.locname=locn;}
    public void setlocadd(String loca){this.locaddress=loca;}
    public String calcpurity(){
        Random r =new Random();
float turb,temp,phn;
        turb= Float.parseFloat(turbidity);
        temp=Float.parseFloat(conductivity);
        phn=Float.parseFloat(ph);
        phn=Math.abs(7-phn);
        if(phn==0){
            phn=100;
        }
        else {
            phn = (phn / 7) * 100;
        }
        temp=Math.abs(32-temp);

        turb=Math.abs(5-turb);
        if(temp==0){
               temp=100;
        }
        else{
            temp=(temp/32)*100;
        }
        if(turb==0){
            turb=100.0f;
        }
        else{

        turb=(turb/5)*100;}

        float w1=0.4f;
        float w2=0.1f;
        float w3=0.5f;
        purity=Float.toString(((phn)+(temp)+(turb))/3);

        //purity=String.valueOf(r.nextInt(101));
        return purity;
    }
}
