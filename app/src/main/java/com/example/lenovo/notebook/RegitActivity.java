package com.example.lenovo.notebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.notebook.bean.Result;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegitActivity extends AppCompatActivity {
private EditText name;
private EditText passwd;
private EditText passwd1;
private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regit);
        initView();
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              RequestQueue mQueue = Volley.newRequestQueue(RegitActivity.this);
              String names=name.getText().toString().trim();
              String passwds=passwd.getText().toString().trim();
              String passwds1=passwd1.getText().toString().trim();

              if("".equals(names)|| "".equals(passwds)||"".equals(passwds1)){
                  Toast.makeText(RegitActivity.this, "所有字段不能为空", Toast.LENGTH_LONG).show();
              }else if(!passwds.equals(passwds1)){
                  Toast.makeText(RegitActivity.this, passwds+":"+passwds1, Toast.LENGTH_LONG).show();
              }else {
                  Map<String, String> merchant = new HashMap<String, String>();
                  merchant.put("name", names);
                  merchant.put("passwd",passwds);
                  JSONObject jsonObject = new JSONObject(merchant);
                  JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(" http://192.168.1.101/notebook/public/index/index/regit", jsonObject,
                          new Response.Listener<JSONObject>() {
                              @Override
                              public void onResponse(JSONObject response) {
                                  Gson g=new Gson();
                                  Result result=             g.fromJson(response.toString(), Result.class);
                                  Boolean S=result.getStatus().toString().trim().equals("success");
                                  Log.d("R",S+"");
                                  if (result.getStatus().toString().trim().equals("success")){
                                      Intent intent=new Intent(RegitActivity.this,MainActivity.class);
                                      startActivity(intent);
                                  }else {
                                      Toast.makeText(RegitActivity.this, result.getStatus().toString().trim(), Toast.LENGTH_LONG).show();
                                  }

                                  Log.d("t", response.toString())   ;
                              }
                          }, new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                          Log.e("TAG", error.getMessage(), error);
                      }
                  });
                  mQueue.add(jsonObjectRequest);
              }
          }
      });
    }
  public void  initView(){
        name=findViewById(R.id.regit_name);
        passwd=findViewById(R.id.rigit_passwd);
      passwd1=findViewById(R.id.regit_passwd1);
      button=findViewById(R.id.regit_btn);
    }
}
