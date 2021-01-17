package hcmut.cse.robotcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

public class BConnect extends AppCompatActivity {

    ListView devicelist;
    ListView lastdevice;
    TextView text;
    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    public static String EXTRA_INFO = "device_info";
    private String fileName = "Saved_Devices.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_connect);
        devicelist = (ListView)findViewById(R.id.list1);
        lastdevice = (ListView) findViewById(R.id.list2);
        text = (TextView)findViewById(R.id.text3);

        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }
        pairedDevicesList();
        ShowLastDevice();
    }
    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }

    private void msg(String s)
    {
        Toast a = Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT);
        a.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                a.cancel();
            }
        }, 5000);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            Intent i = new Intent();
            //Change the activity.
            i.putExtra(EXTRA_INFO, info);
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            setResult(Activity.RESULT_OK,i);
            finish();
        }
    };


    private void ShowLastDevice(){
        try{
            FileInputStream ifs = this.openFileInput(fileName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(ifs));
            String s = buf.readLine();
            if (s == null) {
                text.setText("(No device)");
                ifs.close();
                return;
            }
            s = s + "\n" + buf.readLine();
            ifs.close();
            ArrayList list = new ArrayList();
            list.add(s);
            final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
            lastdevice.setAdapter(adapter);
            lastdevice.setOnItemClickListener(myListClickListener);
        } catch (Exception e){
            msg("Error: " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        // đặt resultCode là Activity.RESULT_CANCELED thể hiện
        // đã thất bại khi người dùng click vào nút Back.
        // Khi này sẽ không trả về data.
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

}