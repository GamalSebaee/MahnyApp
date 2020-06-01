package atiaf.mehany.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;

import com.vistrav.ask.Ask;

import atiaf.mehany.Activity.phase2.login.OtherloginActivity;
import atiaf.mehany.R;

public class MainActivity extends Activity {
LinearLayout lin , lin1 , lin2 ,lin2More;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ask.on(this)
                .forPermissions(android.Manifest.permission.CALL_PHONE
                        , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , android.Manifest.permission.READ_EXTERNAL_STORAGE
                        , android.Manifest.permission.ACCESS_COARSE_LOCATION
                        , android.Manifest.permission.GET_ACCOUNTS
                        , android.Manifest.permission.ACCESS_FINE_LOCATION
                        , android.Manifest.permission.ACCESS_NETWORK_STATE
                        , android.Manifest.permission.SEND_SMS
                        , Manifest.permission.CAMERA)
                .withRationales("Call permission need for call",
                        "In order to save file you will need to grant storage permission"
                        , "In order to Read file you will need to grant storage permission"
                        ,"allow access location"
                        ,"allow access GET ACCOUNTS"
                        ,"allow access location"
                        ,"allow access network"
                        ,"allow permission need for send sms"
                        ,"allow permission need for open camera"
                ) //optional
                .go();
        statusCheck();
        lin =  findViewById(R.id.lin);
        lin1 =  findViewById(R.id.lin1);
        lin2 =  findViewById(R.id.lin2);
        lin2More =  findViewById(R.id.lin2_more);

        lin2More.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), OtherloginActivity.class);
            startActivity(i);
        });

        lin1.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),WorkerloginActivity.class);
            startActivity(i);
        });
        lin2.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),ClientloginActivity.class);
            startActivity(i);
        });
        lin.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),techerlogin.class);
            startActivity(i);
        });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.plz_enable_gps)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, id) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton(R.string.no, (dialog, id) -> {
                    dialog.cancel();
                    finish();
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }
}
