package com.example.lenovo.notebook;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.notebook.util.DBService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/*
编辑记事本页面
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
private EditText textTittle;
private EditText textContent;
private ImageButton image_goback;
private ImageButton image_save;
private String noteId=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

        image_save.setOnClickListener(this);
        image_goback.setOnClickListener(this);
        initNoteEditValue();
    }

  public void  initView(){
        textTittle=findViewById(R.id.tittle);
        textContent=findViewById(R.id.content);
        image_goback=findViewById(R.id.goback);
        image_save=findViewById(R.id.save);
    }

    @Override
    public void onClick(View view) {
        final String title = textTittle.getText().toString().trim();
        final String content = textContent.getText().toString().trim();
        switch (view.getId()){
            case R.id.goback:
                HomeActivity.this.finish();
                break;
            case R.id.save:
                if ("".equals(title) || "".equals(content)) {
                    Toast.makeText(HomeActivity.this, "标题或者内容不能为空",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                save(title,content);
                break;
        }
    }
    public void save(final String tittle,final String content){
        //提示保存
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("提示框")
                .setMessage("确定保存笔记吗？？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                ContentValues values = new ContentValues();
                                values.put("title",tittle);
                                values.put("content", content);

                                //如果noteId不为空那么就是更新操作，为空就是添加操作
                                if (null == noteId || "".equals(noteId))
                                    DBService.addNote(values);
                                else
                                    DBService.updateNoteById(
                                            Integer.valueOf(noteId),
                                            values);
                                //结束当前activity
                                Intent i=new Intent(HomeActivity.this,ListActivity.class);
                                startActivity(i);
                                Toast.makeText(HomeActivity.this, "保存成功！！",
                                        Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("取消", null).show();

    }

    /**
     * 初始化编辑页面的值（如果进入该页面时存在一个id的话），比如标题，内容。
     */
    private void initNoteEditValue() {
        // 从Intent中获取id的值
        long id = this.getIntent().getLongExtra("id", -1L);
        // 如果有传入id那么id！=-1
        if (id != -1L) {
            // 使用noteId保存id
            noteId = String.valueOf(id);

            // 查询该id的笔记
            Cursor cursor = DBService.queryNoteById((int) id);
            if (cursor.moveToFirst()) {
                // 将内容提取出来
              textTittle.setText(cursor.getString(1));
               textContent.setText(cursor.getString(2));
            }
        }
    }

}
