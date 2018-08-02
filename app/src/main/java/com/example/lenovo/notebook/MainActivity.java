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

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_login;
    private Button btn_regist;
    private EditText text_name;
    private EditText text_passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btn_login.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
    }
    public void initView(){
        btn_login=findViewById(R.id.login);
        btn_regist=findViewById(R.id.regist);
        text_name=findViewById(R.id.name);
        text_passwd=findViewById(R.id.passwd);
    }

    @Override
    public void onClick(View view) {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
      final   String name=text_name.getText().toString().trim();
     final    String passwd=text_passwd.getText().toString().trim();
        switch (view.getId()){
            case R.id.login:
             if ("".equals(name)|| "".equals(passwd)){
                 Toast.makeText(MainActivity.this, "请输入账号密码", Toast.LENGTH_LONG).show();
             }else {
                 Map<String, String> merchant = new HashMap<String, String>();
                 merchant.put("Acc", name.toString());
                 merchant.put("passwd",passwd.toString());
                 JSONObject jsonObject = new JSONObject(merchant);
                 JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(" http://192.168.1.101/notebook/public/index/index/login", jsonObject,
                         new Response.Listener<JSONObject>() {
                             @Override
                             public void onResponse(JSONObject response) {
                                 Log.d("t", response.toString())   ;
                                 Gson g=new Gson();
                    Result result=             g.fromJson(response.toString(), Result.class);
                    Boolean S=result.getStatus().toString().trim().equals("success");
                    Log.d("R",S+"");
if (result.getStatus().toString().trim().equals("success")){
    Intent intent=new Intent(MainActivity.this,ListActivity.class);
    intent.putExtra("name",name);
    startActivity(intent);
}else {
    Toast.makeText(MainActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
}


                             }
                         }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         Log.e("TAG", error.getMessage(), error);
                     }
                 });
                 mQueue.add(jsonObjectRequest);

             }

                break;
            case R.id.regist:
                Intent intent=new Intent(MainActivity.this,RegitActivity.class);
                startActivity(intent);
                break;
        }
    }
}
