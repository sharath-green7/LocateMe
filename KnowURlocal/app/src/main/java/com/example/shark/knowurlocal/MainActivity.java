package com.example.shark.knowurlocal;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    Button check,click;
    TextView t1,t2;
    WebView browser;
    ProgressBar pb;
    private Boolean flag = false;
    private LocationListener locationListener;
    private LocationManager locationManager=null;
    String url="https://en.wikipedia.org/wiki/";
    @Override
    public void onClick(View v) {
        flag = displayGpsStatus();
        if (flag) {
            t2.setText("GPS ON");
            locationListener = new LocationTrack();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10,locationListener);
            pb.setVisibility(View.VISIBLE);
        }
        else{
            t2.setText("GPS OFF");
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check=(Button)findViewById(R.id.button);
        click=(Button)findViewById(R.id.button2);
        check.setOnClickListener(this);
        t1=(TextView)findViewById(R.id.textView);
        t2=(TextView)findViewById(R.id.textView2);
        browser = (WebView) findViewById(R.id.webView);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver,LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private class LocationTrack implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            String cityName=null;
            final String s1;
            Geocoder gcd = new Geocoder(getBaseContext(),Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),loc.getLongitude(),1);
                if (addresses.size()>0)
                cityName=addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            t2.setText(cityName);
            s1=cityName;
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    browser.loadUrl(url+s1);
                }
            });
            pb.setVisibility(View.INVISIBLE);
    }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
 }
