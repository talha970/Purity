package fyp.teejay.apollo.fyp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Startup_activity extends Activity {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data.getExtras().containsKey("Username")) {
                MainActivity.navuserstring=("Welcome " + data.getStringExtra("Username"));
                Intent i=new Intent(this,MainActivity.class);
                startActivity(i);
                finish();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_activity);
    }
    public void login_activity(View v){
        Intent i=new Intent(this,LoginActivity.class);
        startActivityForResult(i,10);
    }
    public void register_activity(View v){
        Intent i=new Intent(this,RegisterActivity.class);
        startActivity(i);
    }
}
