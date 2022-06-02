package com.example.bighomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_phone, edt_password;
    private TextView txv_forget, txv_delete;
    private Button btn_register, btn_login;

    //记住密码和自动登录
    private CheckBox ckb_remember, ckb_autoLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_phone = findViewById(R.id.phone);
        edt_password = findViewById(R.id.password);
        txv_forget = findViewById(R.id.forget);
        txv_delete = findViewById(R.id.delete);
        btn_register = findViewById(R.id.register);
        btn_login = findViewById(R.id.login);

        ckb_remember = findViewById(R.id.remember);
        ckb_autoLogin = findViewById(R.id.autoLogin);

        sharedPreferences = getSharedPreferences("user_autoLogin", Context.MODE_PRIVATE);
        ckb_remember.setChecked(true);

        //判断记住密码多选框的状态
        if(sharedPreferences.getBoolean("rem_isCheck", false))
        {
            //自动填充账号密码
            edt_phone.setText(sharedPreferences.getString("USER_NAME", ""));
            edt_password.setText(sharedPreferences.getString("PASSWORD", ""));

            if (sharedPreferences.getBoolean("auto_isCheck", false)) {
                //跳转界面
                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //LoginActivity.this.startActivity(intent);
                Toast.makeText(LoginActivity.this, "自动登录成功", Toast.LENGTH_SHORT).show();
            }

        }

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

        //勾选自动登录必须勾选记住密码
        ckb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && !ckb_remember.isChecked())
                {
                    ckb_remember.setChecked(true);
                }
                else if(!b && ckb_remember.isChecked())
                {
                    ckb_remember.setChecked(false);
                }
            }
        });
        ckb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b && ckb_autoLogin.isChecked())
                {
                    ckb_autoLogin.setChecked(false);
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
                        //取消记住用户名、密码、
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("rem_isCheck", false);

                        editor.commit();

                        Toast.makeText(LoginActivity.this, "没有用户信息", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(password.equals(list.get(0).getPassword()))
                        {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            if (ckb_remember.isChecked())
                            {
                                //记住用户名、密码
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("USER_NAME", phone);
                                editor.putString("PASSWORD", password);
                                editor.putBoolean("rem_isCheck", ckb_remember.isChecked());
                                if(ckb_autoLogin.isChecked())
                                {
                                    editor.putBoolean("auto_isCheck", true);
                                }
                                else
                                {
                                    editor.putBoolean("auto_isCheck", false);
                                }

                                editor.commit();
                            }
                            else
                            {
                                //取消记住用户名、密码、
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("rem_isCheck", false);

                                editor.commit();
                            }
                        }
                        else
                        {
                            //取消记住用户名、密码、
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("rem_isCheck", false);

                            editor.commit();

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

        //跳转忘记密码
        txv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        //跳转到账号注销
        txv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LogoutActivity.class);
                startActivity(intent);
            }
        });
    }
}