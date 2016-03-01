package ru;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.akexorcist.bluetoothspp.R;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import org.json.JSONException;
import org.json.JSONObject;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener;

import java.util.UUID;

/**
 * Created by eljah32 on 2/29/2016.
 */
public class FormTrackerActivity extends Activity {
    // Progress Dialog Object
    ProgressDialog prgDialog;
    TextView errorMsg;
    EditText diameter;
    EditText latitude;
    EditText longitude;
    EditText url;
    Button login;

    //private BluetoothAdapter btAdapter = null;
    //private BluetoothSocket btSocket = null;
    //private OutputStream outStream = null;
    //BluetoothDevice device = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothSPP bt;

    TrackerTransferObject tto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_tracker);
        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID
        diameter = (EditText) findViewById(R.id.diameter);
        // Find Password Edit View control by ID
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);
        login = (Button) findViewById(R.id.btnLogin);
        login.setText("Connect");
        //login.setEnabled(false);
        url = (EditText) findViewById(R.id.url);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        bt = new BluetoothSPP(this);

        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                //login.setEnabled(true);
                //prgDialog.hide();
                url.setText(message);
                //textRead.append(message + "\n");
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                url.setText("Disconnected");
                login.setText("Connect");
                // prgDialog.hide();
                //textStatus.setText("Status : Not connect");
                //menu.clear();
                //getMenuInflater().inflate(R.menu.menu_connection, menu);
            }

            public void onDeviceConnectionFailed() {
                url.setText("Failed");
                login.setText("Connect/Refresh data");
                // prgDialog.hide();
                //textStatus.setText("Status : Connection failed");
            }

            public void onDeviceConnected(String name, String address) {
                url.setText("Connedted " + name + " " + address);
                login.setText("Connected/Refresh data");
                tto=new TrackerTransferObject(diameter.getText().toString(),longitude.getText().toString(),latitude.getText().toString());
                BTLoopSender loop=new BTLoopSender(bt,tto);
                new Thread(loop).start();
                //loop.run();
                //textStatus.setText("Status : Connected to " + name);
                //menu.clear();
                //getMenuInflater().inflate(R.menu.menu_disconnection, menu);
            }
        });

        //startMethod();

    }


    public void startMethod()

    {


        bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);

        //if (bt.getServiceState() == BluetoothState.STATE_CONNECTED)
        //{            bt.disconnect();}

        Intent intent = new Intent(getApplicationContext(), DeviceListFiltered.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    public void loginUser(View view) {
        String diameterS = diameter.getText().toString();
        String latitudeS = latitude.getText().toString();
        String longitudeS = longitude.getText().toString();
        String urleS = url.getText().toString();


        if(latitude.getText().length() != 0) {
            tto.update(diameter.getText().toString(),longitude.getText().toString(),latitude.getText().toString());
            //bt.send(latitude.getText().toString(), true);
            //latitude.setText("");

        }

        //invokeBT(urleS, latitudeS, longitudeS, diameterS);

        //Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        //startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        // invokeBT(params, urleS, latitudeS, longitudeS, diameterS);
        //Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();


    }


    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoHomeActivity() {
        Intent homeIntent = new Intent(getApplicationContext(), Main.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void invokeBT(String url2, String longitude, String latitude, String diameter) {
        // Show Progress Dialog
        //prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        bt.send("A", true);
        //bt.send(diameter + " " + longitude + " " + latitude, true);
        url.setText("Waiting for responce");
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                setup();
            }
        }


    }

    public void setup() {
        Button btnSend = (Button)findViewById(R.id.btnLogin);
        btnSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (bt.getServiceState()!= BluetoothState.STATE_CONNECTED)
                {
                    bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
                    login.setText("Connecting");
			/*
			if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
                    Intent intent = new Intent(getApplicationContext(), DeviceListFiltered.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }


                if(latitude.getText().length() != 0) {
                    tto.update(diameter.getText().toString(),longitude.getText().toString(),latitude.getText().toString());
                    //bt.send(latitude.getText().toString(), true);
                    //latitude.setText("");

                }
            }
        });
    }

}
