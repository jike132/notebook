package com.example.lenovo.notebook.util;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;

public class DBService {
	
	private static SQLiteDatabase db = null;
	
	static {
		db = SQLiteDatabase.openOrCreateDatabase("data/data/com.example.lenovo.notebook/NoteBook.db", null);//创建数据库
		
		String sql = "create table NoteBook(_id integer primary key autoincrement,title varchar(255),content TEXT, time  varchar(25))";//建表语句

		try{
			db.rawQuery("select count(1) from NoteBook ",null);
		}catch(Exception e){
			db.execSQL(sql);
		}
	}
	
	public static SQLiteDatabase getSQLiteDatabase(){
		return db;
	}
	
	public static Cursor queryAll(){
		return db.rawQuery("select * from NoteBook ",null);
	}//查询所有
	
	public static Cursor queryNoteById(Integer id){//根据id查询数据
		return db.rawQuery("select * from NoteBook where _id =?",new String[]{id.toString()});
	}
	
	public static void deleteNoteById(Integer id){//删除数据
		if(id == null)
			return ;
		db.delete("NoteBook", "_id=?", new String[]{id.toString()});
	}
	
	public static void updateNoteById(Integer id, ContentValues values){//更新数据
		db.update("NoteBook", values, "_id=?", new String[]{id.toString()});
	}
	
	public static void addNote(ContentValues values){//插入数据
		values.put("time", DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString());
		db.insert("NoteBook", null, values);
	}
	public static void addAllNote(ContentValues values){//添加从后台来的所有数据
		db.insert("NoteBook", null, values);
	}
	
}
