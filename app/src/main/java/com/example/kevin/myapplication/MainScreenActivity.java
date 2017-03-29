package com.example.kevin.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainScreenActivity extends AppCompatActivity  {
    EditText e1, e2;
ProgressDialog progressDialog=null;
    //Intent intent=new Intent(this,MainActivity.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);



    }

    public void formSub(View v) {

        RequestQueue queue = Volley.newRequestQueue(this); // this = context
        String url = "http://192.168.43.225/android/login_copy.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response1", response);
                        //startActivity(intent);
                        ;

                        String res = "success";
                        String result="";
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            result=""+ jsonObject.get("result");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (result.equals(res)) { Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                            start();

                        } else {



                            progressDialog.dismiss();
                            Toast toast=new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast.makeText(getApplicationContext(), "enter valid username and password", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        }



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
                params.put("name", e1.getText().toString());
                params.put("password", e2.getText().toString());

//                Log.d("Values","");
                return params;

            }
        };



        progressDialog.show();
        queue.add(postRequest);

    }

    public void start() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}