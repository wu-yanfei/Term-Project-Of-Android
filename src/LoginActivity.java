package com.example.bighomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText edt_phone, edt_password;
    TextView txv_forget;
    Button btn_register, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_phone = findViewById(R.id.phone);
        edt_password = findViewById(R.id.password);
        txv_forget = findViewById(R.id.forget);
        btn_register = findViewById(R.id.register);
        btn_login = findViewById(R.id.login);

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

        //进行登录
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<UserInfo> list;
                SQLiteDatabase sqLiteDatabase;

                String phone = edt_phone.getText().toString();
                String password = edt_password.getText().toString();

                if(phone.length()==11 && phone.matches("^[1][3,4,5,6,7,8,9][0-9]{9}$") && password.length() >= 8 && password.length() <= 16)
                {
                    User_Database users=new User_Database(LoginActivity.this);
                    sqLiteDatabase=users.getReadableDatabase();

                    //获取从数据库中查询到的数据
                    list=users.queryData(sqLiteDatabase, phone);

                    if(list.size() == 0)
                    {
                        Toast.makeText(LoginActivity.this, "没有用户信息", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(password.equals(list.get(0).getPassword()))
                        {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //跳转到注册页面
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //忘记密码的跳转
        txv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}