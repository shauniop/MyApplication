package com.example.kevin.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class UnloadDetails extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    EditText e1, e2;
    public static final String MY_DATA="mydata";
    //Intent intent=new Intent(this,MainActivity.class);
    SharedPreferences sharedPreferences;
    private ZXingScannerView mScannerView;
    private static int cam_check2=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload_details);
        e1 = (EditText) findViewById(R.id.editText12);
        e2 = (EditText) findViewById(R.id.editText13);
    }



    public void QrScanner3(View view){

        String src_stn=e2.getText().toString();
        SharedPreferences.Editor editor=getSharedPreferences(MY_DATA, MODE_PRIVATE).edit();

        editor.putString("unload_stn",e2.getText().toString());
        editor.commit();
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
        cam_check2=1;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(cam_check2==1)
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.d("handler", rawResult.getText()); // Prints scan results
        Log.d("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        String result;
        final String result1[];
        // show the scanner result into dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
        result = rawResult.getText().toString();

        result1 = result.split(",");
        // Toast.makeText(getApplicationContext(), result1[0] + " " + result1[1] + " " + result1[2], Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences(MY_DATA, MODE_PRIVATE);
        String train = sharedPreferences.getString("train", null);
        final String stationno = sharedPreferences.getString("unload_stn", null);
        Log.d("VALUES", "" + train + " " + stationno);

        if(result1[2].equals(stationno))
        {
            RequestQueue queue = Volley.newRequestQueue(this); // this = context
            String url = "http://192.168.43.225/android/unload_.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            //startActivity(intent);
                            //  Toast.makeText(getApplicationContext(), "hi" + response, Toast.LENGTH_LONG).show();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", "error");
                        }
                    }
            )


            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("result1", result1[0]);
                    params.put("result2", result1[1]);
                    params.put("result3", result1[2]);
                    params.put("result4", result1[3]);

//                Log.d("Values","");
                    return params;

                }
            };
            queue.add(postRequest);


            // If you would like to resume scanning, call this method below:
            mScannerView.resumeCameraPreview(this);
        }
        else{
            Toast.makeText(getApplicationContext(),  stationno+" is the wrong destination station" , Toast.LENGTH_LONG).show();
//
            RequestQueue queue = Volley.newRequestQueue(this); // this = context
            String url = "http://192.168.43.225/android/unload_lost.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.i("Response", response);
                            //startActivity(intent);
                            Toast.makeText(getApplicationContext(),"response:"+response, Toast.LENGTH_LONG).show();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", "error");
                            Toast.makeText(getApplicationContext(), "Error: "+error, Toast.LENGTH_LONG).show();

                        }
                    }
            )


            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("result1", result1[0]);
                    params.put("result2", result1[1]);
                    params.put("result3", result1[2]);
                    params.put("result4", result1[3]);
                    params.put("lost_stn",stationno);

//                Log.d("Values","");
                    return params;

                }
            };
            queue.add(postRequest);
            Log.d("data","before intent");
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("ATTENTION");
            alert.setMessage("PACKAGE LOST,KUCH KARO");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mScannerView.resumeCameraPreview(UnloadDetails.this);
                }
            });
            alert.show();


//
//            startActivity(new Intent(this,UnloadDetails.class));
        }
    }
}