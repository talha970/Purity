package fyp.teejay.apollo.fyp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import fyp.teejay.apollo.fyp1.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MarkerFragment.OnListFragmentInteractionListener {
    NavigationView navigationView=null;
    Toolbar toolbar=null;
public static String navuserstring="Welcome";
    TextView nav_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabFragment mainfrag=new TabFragment();
        android.support.v4.app.FragmentTransaction fragtrans=getSupportFragmentManager().beginTransaction();
        fragtrans.replace(R.id.frag_container,mainfrag);
        fragtrans.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

     navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v=navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.nav_login);
        MenuItem nav_logout = menu.findItem(R.id.nav_logout);
        if(Login_Info.loggedin) {
            nav_login.setTitle(Login_Info.getusername(this));

            nav_user = (TextView)v.findViewById(R.id.tvUser);
            nav_logout.setVisible(true);
            if(nav_user!=null){
                nav_user.setText("Welcome"+" "+Login_Info.getusername(MainActivity.this) );}

        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data.getExtras().containsKey("Username")) {
                nav_user.setText("Welcome " + data.getStringExtra("Username"));

            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            MarkerFragment markfrag=new MarkerFragment();
            android.support.v4.app.FragmentTransaction fragtrans=getSupportFragmentManager().beginTransaction();
            fragtrans.replace(R.id.frag_container,markfrag);
            fragtrans.commit();
        } else if (id == R.id.nav_login) {
            if(item.getTitle().toString().equals("Log in")) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivityForResult(i, 1);
            }
            else{
                Intent i = new Intent(this, Login_Pref.class);
                startActivity(i);

            }

        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.nav_logout) {
logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onListFragmentInteraction(Markers item) {

    }
    public void logout(){

        Login_Info.setloggedin(this,false);
        Login_Info.setUsername(this,"Guest");
        Intent i =new Intent(this,Startup_activity.class);
        startActivity(i);
        finish();


    }
}
