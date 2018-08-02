package com.example.lenovo.notebook;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.notebook.bean.Note;
import com.example.lenovo.notebook.bean.Result;
import com.example.lenovo.notebook.util.DBService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
记事本列表页面
 */
public class ListActivity extends AppCompatActivity implements View.OnClickListener{

    private Cursor listItemCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        // 设置添加笔记按钮事件，切换activity
     this.findViewById(R.id.addNote).setOnClickListener(this);
     this.findViewById(R.id.saveAll).setOnClickListener(this);
        this.findViewById(R.id.back).setOnClickListener(this);
        // 查询所有笔记，并将笔记展示出来
        listItemCursor = DBService.queryAll();
//使用简单适配器，将数据放进listview控件
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(ListActivity.this,
                R.layout.note_item, listItemCursor, new String[] { "_id",
                "title", "time" }, new int[] { R.id.noteId,
                R.id.noteTitle, R.id.noteCreateTime },
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ((ListView) this.findViewById(R.id.listNote)).setAdapter(adapter);

        initListNoteListener();
        ListActivity.this.onResume();
    }

    /**
     * 初始化笔记列表的长按和点击事件
     */
    private void initListNoteListener() {
        // 长按删除
        ((ListView) this.findViewById(R.id.listNote))
                .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int position, final long id) {
                        new AlertDialog.Builder(ListActivity.this)
                                .setTitle("提示框")
                                .setMessage("确认删除该笔记？？")
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0,int arg1) {
                                                DBService.deleteNoteById((int) id);
                                                //删除后刷新列表
                                                ListActivity.this.onResume();
                                                Toast.makeText(
                                                        ListActivity.this,
                                                        "删除成功！！",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }).setNegativeButton("取消", null).show();
                        return true;
                    }
                });

        //点击进行修改操作
        ((ListView) this.findViewById(R.id.listNote))
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent in = new Intent();
                        in.setClassName(view.getContext(),
                                "com.example.lenovo.notebook.HomeActivity");
                        //将id数据放置到Intent
                        in.putExtra("id", id);
                        startActivity(in);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 要求刷新主页列表笔记
        if (listItemCursor != null) {
            listItemCursor.requery();
        }
    }

    @Override
    public void onClick(View view) {
        RequestQueue  mQueue = Volley.newRequestQueue(ListActivity.this);
        switch (view.getId()){
            case R.id.addNote:
                Intent in = new Intent();
                in.setClassName(getApplicationContext(),
                        "com.example.lenovo.notebook.HomeActivity");
                startActivity(in);
                break;
            case R.id.saveAll:
                listItemCursor=DBService.queryAll();
                List<Note> list=new ArrayList<>();
              if (listItemCursor.moveToFirst()){
                  do {
                      Note note = new Note();
                      Intent intent = getIntent();
                      String name = intent.getStringExtra("name");
                      note.setAcc(name);
                      note.setText(listItemCursor.getString(listItemCursor.getColumnIndex("content")));
                      note.setTittle(listItemCursor.getString(listItemCursor.getColumnIndex("title")));
                      note.setTime(listItemCursor.getString(listItemCursor.getColumnIndex("time")));
                      list.add(note);
                      Log.d("list",listItemCursor.getString(listItemCursor.getColumnIndex("content")));
                  }while (listItemCursor.moveToNext());

              }
             Gson g=new Gson();
        final String d=  g.toJson(list);
                Log.d("MAP",d);

           StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,"http://192.168.1.101/notebook/public/index/index/saveAll",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("r",response.toString());
                                if (response.toString().equals("success")){
                                    Toast.makeText(ListActivity.this, "备份成功", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOGIN-ERROR", error.getMessage(), error);
                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.e("LOGIN-ERROR", new String(htmlBodyBytes), error);
                    }
                }){
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String,String> map=new HashMap<>();
                   map.put( "name",d)   ;
                   return map;
               }
           };
                mQueue.add(jsonObjectRequest);
                break;
            case R.id.back:
                StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://192.168.1.101/notebook/public/index/index/home ",
                        new Response.Listener < String > () {
                            @Override
                            public void onResponse(String response) {
                              Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();

                                ArrayList<JsonObject> jsonObjects = new Gson().fromJson(response, type);

                                ArrayList<Note> arrayList = new ArrayList<>();

                                for (JsonObject jsonObject : jsonObjects)

                                {

                                    arrayList.add(new Gson().fromJson(jsonObject, Note.class));

                                }
                                for (int i=0;i<arrayList.size();i++){
                                    ContentValues values=new ContentValues();
                                    values.put("title",arrayList.get(i).getTittle());
                                    values.put("title",arrayList.get(i).getTittle());
                                    values.put("content",arrayList.get(i).getText());
                                    values.put("time",arrayList.get(i).getTime());
                                    DBService.addAllNote(values);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG",volleyError.getMessage(), volleyError);
                    }


                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Intent i=getIntent();
                        Map<String,String> map=new HashMap<>();
                        map.put( "name",i.getStringExtra("name"))   ;
                        return map;
                    }
                };
                mQueue.add(stringRequest);
                ListActivity.this.finish();
                break;
        }
    }
}
