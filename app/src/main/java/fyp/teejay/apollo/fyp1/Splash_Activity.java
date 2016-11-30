package fyp.teejay.apollo.fyp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class Splash_Activity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
Intent i;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(Login_Info.getIsfirst(Splash_Activity.this)){
         i= new Intent(Splash_Activity.this,Startup_activity.class);
                    Login_Info.setisfirst(Splash_Activity.this,false);
                }
                else{
                    if(Login_Info.getloggedin(Splash_Activity.this)){
                        i= new Intent(Splash_Activity.this,MainActivity.class);
                    }
                    else{
                        i= new Intent(Splash_Activity.this,Startup_activity.class);
                    }

                }
                startActivity(i);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
