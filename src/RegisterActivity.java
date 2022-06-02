package com.example.bighomework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_phone, edt_password, edt_repassword, edt_email;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_phone = findViewById(R.id.phone);
        edt_password = findViewById(R.id.password);
        edt_repassword = findViewById(R.id.repassword);
        edt_email = findViewById(R.id.email);
        btn_register = findViewById(R.id.register);

        //限制手机号为大陆11位手机号
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {

                String phone = edt_phone.getText().toString();

                if (phone.length()!=11)
                {
                    edt_phone.setError("手机号位数不正确");
                }
                else if (!phone.matches("^[1][3,4,5,6,7,8,9][0-9]{9}$"))
                {
                    edt_phone.setError("请输入正确的手机号");
                }
            }
        });

        //保证密码是8~16位
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = edt_password.getText().toString();

                if(password.length() < 8 || password.length() > 16)
                {
                    edt_password.setError("密码必须是8-16位");
                }
            }
        });

        //保证两次输入的密码相同
        edt_repassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String repassword = edt_repassword.getText().toString();

                if(!repassword.equals(edt_password.getText().toString()))
                {
                    edt_repassword.setError("两次输入的密码不相同");
                }
            }
        });

        //保证邮箱的正确性
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = edt_email.getText().toString();

                if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                {
                    edt_email.setError("请输入正确的邮箱地址");
                }
            }
        });

        //点击注册提交正确的注册信息并且返回登录界面
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edt_phone.getText().toString();
                String password = edt_password.getText().toString();
                String repassword = edt_repassword.getText().toString();
                String email = edt_email.getText().toString();

                if(phone.length()==11 && phone.matches("^[1][3,4,5,6,7,8,9][0-9]{9}$") &&
                        password.length() >= 8 && password.length() <= 16 &&
                        repassword.equals(repassword) &&
                        email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))

                {
                    User_Database user_database=new User_Database(RegisterActivity.this);
                    SQLiteDatabase sqLiteDatabase=user_database.getReadableDatabase();//可读写数据库

                    //获取从数据库中查询到的数据
                    List<UserInfo> list=user_database.queryData(sqLiteDatabase, phone);

                    if(list.size() != 0)
                    {
                        Toast.makeText(RegisterActivity.this, "注册失败，该用户已存在", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        User_Database user_database1=new User_Database(RegisterActivity.this);
                        SQLiteDatabase sqLiteDatabase1=user_database.getReadableDatabase();//可读写数据库

                        user_database.adddata(sqLiteDatabase1,phone,password,email);
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}