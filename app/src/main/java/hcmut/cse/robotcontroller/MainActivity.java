package hcmut.cse.robotcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EXAMPLE = 0x9345;
    private String fileName = "Saved_Devices.txt";

    ImageButton btu, btl, btd, btr,
                btred, btblue, btgreen, btyellow;
    ImageView blue;

    String address = null;
    String info = null;
    public static String BLUETOOTH = "bluetooth_connect";
    boolean isBtConnected = false;
    BluetoothSocket btSocket = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            FileOutputStream ofs = this.openFileOutput(fileName,MODE_PRIVATE);
            ofs.close();
        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        btu = (ImageButton)findViewById(R.id.btu);
        btd = (ImageButton)findViewById(R.id.btd);
        btl = (ImageButton)findViewById(R.id.btl);
        btr = (ImageButton)findViewById(R.id.btr);
        blue = (ImageView)findViewById(R.id.blue);
        btred = (ImageButton)findViewById(R.id.btred);
        btblue = (ImageButton)findViewById(R.id.btblue);
        btyellow = (ImageButton)findViewById(R.id.btyellow);
        btgreen = (ImageButton)findViewById(R.id.btgreen);
        blue.setImageDrawable(getResources().getDrawable(R.drawable.blx));
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBtConnected == false){
                    Connect();
                }
                else{
                    Disconnect();
                }

            }
        });
        btu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtU();
            }
        });
        btd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtD();
            }
        });
        btl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtL();
            }
        });
        btr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtR();
            }
        });
        btred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btr();
            }
        });
        btgreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btg();
            }
        });
        btyellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bty();
            }
        });
        btblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btb();
            }
        });
    }


    public void BtU(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("U".toString().getBytes());
            }
            catch (IOException e)
            {
                Disconnect();
            }
        }
    }
    public void BtD(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("D".toString().getBytes());
            }
            catch (IOException e)
            {
                Disconnect();
            }
        }
    }
    public void BtL(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("L".toString().getBytes());
            }
            catch (IOException e)
            {
                Disconnect();
            }
        }
    }
    public void BtR(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("R".toString().getBytes());
            }
            catch (IOException e)
            {
                Disconnect();
            }
        }
    }
    public void Btr(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("r".toString().getBytes());
            }
            catch (IOException e)
            {
                Disconnect();
            }
        }
    }
    public void Btb(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("b".toString().getBytes());
            }
            catch (IOException e)
            {
                Disconnect();
            }
        }
    }
    public void Btg(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("g".toString().getBytes());
            }
            catch (IOException e)
            {
                Disconnect();
            }
        }
    }
    public void Bty(){
        if (btSocket != null){
            try
            {
                btSocket.getOutputStream().write("y".toString().getBytes());
            }
            catch (IOException e)
            {
                Disconnect();
            }
        }
    }

    public void Disconnect(){
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
                isBtConnected = false;
                btSocket = null;
                blue.setImageDrawable(getResources().getDrawable(R.drawable.blx));
                msg("Disconnected");
            }
            catch (IOException e)
            { msg("Error");}
        }
    }
    public void Connect(){
        Intent bconnect = new Intent(MainActivity.this, BConnect.class );
        startActivityForResult(bconnect, REQUEST_CODE_EXAMPLE);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(address != null){
            new ConnectBT().execute();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Kiểm tra requestCode có trùng với REQUEST_CODE vừa dùng
        if(requestCode == REQUEST_CODE_EXAMPLE) {
            // resultCode được set bởi DetailActivity
            // RESULT_OK chỉ ra rằng kết quả này đã thành công
            if(resultCode == Activity.RESULT_OK) {
                // Nhận dữ liệu từ Intent trả về
                address = data.getStringExtra(BConnect.EXTRA_ADDRESS);
                info = data.getStringExtra(BConnect.EXTRA_INFO);
                // Sử dụng kết quả result bằng cách hiện Toast
            }
            else {
                address = null;
                info = null;
            }
        }
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
        }, 1000);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                    blue.setImageDrawable(getResources().getDrawable(R.drawable.blv));
                    address = null;

                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                blue.setImageDrawable(getResources().getDrawable(R.drawable.blx));
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
                saveData(info);

            }
            progress.dismiss();
        }
    }

    private void saveData(String data){
        try{
            FileOutputStream ofs = this.openFileOutput(fileName,MODE_PRIVATE);
            ofs.write(data.getBytes());
            ofs.write("\n".getBytes());
            ofs.close();
        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}