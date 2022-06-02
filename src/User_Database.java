package com.example.bighomework;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public final class User_Database extends SQLiteOpenHelper {

    //数据库的创建
    public User_Database(Context context)
    {
        super(context,"user-db.db",null,1);
    }

    //数据库第一次创建时调用该方法
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table user(phone text primary key," +
                "paswd text not null," +
                "email text not null);";
        sqLiteDatabase.execSQL(sql);
    }

    //数据库版本号更新时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //添加用户数据
    public void adddata(SQLiteDatabase sqLiteDatabase,String phone,String password,String email){
        ContentValues values=new ContentValues();

        values.put("phone",phone);
        values.put("paswd",password);
        values.put("email",email);

        sqLiteDatabase.insert("user",null,values);
        sqLiteDatabase.close();
    }

    //删除用户数据
    public void delete(SQLiteDatabase sqLiteDatabase,String phone) {
        sqLiteDatabase.delete("user","phone=?", new String[]{phone+""});
        sqLiteDatabase.close();
    }

    //修改用户数据（忘记密码）
    public void update(SQLiteDatabase sqLiteDatabase,String phone,String paswd,String email){
        //创建一个ContentValues对象
        ContentValues values=new ContentValues();
        //以键值对的形式插入
        values.put("email",email);
        values.put("paswd",paswd);
        sqLiteDatabase.update("user",values,"phone=?",new String[]{phone+""});
        sqLiteDatabase.close();
    }

    //查询用户数据
    public List<UserInfo> queryData(SQLiteDatabase sqLiteDatabase, String phone1)
    {
        Cursor cursor=sqLiteDatabase.query("user",null,null,null,null,null,null);
        List<UserInfo> list= new ArrayList<UserInfo>();
        while (cursor.moveToNext()){
            if(phone1.equals(cursor.getString(0)))
            {
                String phone=cursor.getString(0);
                String password=cursor.getString(1);
                String email=cursor.getString(2);
                list.add(new UserInfo(phone,password,email));
                break;
            }

        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }
}
