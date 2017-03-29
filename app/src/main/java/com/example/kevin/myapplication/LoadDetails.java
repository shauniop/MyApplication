package com.example.kevin.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
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

public class LoadDetails extends AppCompatActivity implements ZXingScannerView.ResultHandler,AdapterView.OnItemSelectedListener {
    EditText e1, e2;
    AutoCompleteTextView autoCompleteTextView1,autoCompleteTextView2;
    public static final String MY_DATA="mydata";
    //Intent intent=new Intent(this,MainActivity.class);
    SharedPreferences sharedPreferences;
    private ZXingScannerView mScannerView;
    Spinner spinner2,spinner1;
    private static final String[] paths = {"item 1", "item 2", "item 3"};
    private static final String[] paths1 = {"item 1", "item 2", "item 3"};
    private static int cam_check=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_details);
        e1 = (EditText) findViewById(R.id.editText10);
        e2 = (EditText) findViewById(R.id.editText9);
        autoCompleteTextView1= (AutoCompleteTextView) findViewById(R.id.t1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoadDetails.this,
                android.R.layout.simple_list_item_1, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autoCompleteTextView1.setAdapter(adapter);
       autoCompleteTextView2= (AutoCompleteTextView) findViewById(R.id.t2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(LoadDetails.this,
                android.R.layout.simple_list_item_1, paths1);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autoCompleteTextView2.setAdapter(adapter2);

    }



    public void QrScanner12(View view){
    String src_train_no=e1.getText().toString();
        String src_stn=e2.getText().toString();
        SharedPreferences.Editor editor=getSharedPreferences(MY_DATA, MODE_PRIVATE).edit();
        editor.putString("train_no",e1.getText().toString());
        editor.putString("load_stn",e2.getText().toString());
        editor.commit();
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
        cam_check=1;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(cam_check==1)
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
        final String train_no1 = sharedPreferences.getString("train_no", null);
        final String train_no2 = sharedPreferences.getString("train_no1", null);
        String stationno = sharedPreferences.getString("load_stn", null);
        Toast.makeText(getApplicationContext(), train_no2, Toast.LENGTH_SHORT).show();
        if(result1[1].equals(stationno))
        {
        RequestQueue queue = Volley.newRequestQueue(this); // this = context
        String url = "http://192.168.43.225/android/load.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //startActivity(intent);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();


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
                params.put("train_no",train_no1);
                params.put("result1", result1[0]);
                params.put("result2", result1[1]);
                params.put("result3", result1[2]);
                params.put("result4", result1[3]);
               Log.d("Values","");
                return params;

            }
        };
        queue.add(postRequest);


        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
    else{
    Toast.makeText(getApplicationContext(),  stationno+" is the wrong source station" , Toast.LENGTH_LONG).show();
    startActivity(new Intent(this,LoadDetails.class));
}
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        Log.v("item", (String) parent.getItemAtPosition(position));
        Toast.makeText(getApplicationContext(), id+"", Toast.LENGTH_LONG).show();

        SharedPreferences.Editor editor=getSharedPreferences(MY_DATA, MODE_PRIVATE).edit();

        editor.putString("train_no1",autoCompleteTextView1.getText().toString());
        editor.commit();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }



    }



