package com.example.coosinmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coosinmanager.connection.ConnectionDB;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    EditText edtUserName,edtPassword;
    Button btnLogin;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUserName = findViewById(R.id.txt_username);
        edtPassword = findViewById(R.id.txt_password);
        btnLogin = findViewById(R.id.btn_login);



        PushDownAnim.setPushDownAnimTo(btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ConnectionDB connectionDB = new ConnectionDB();
                    connect = connectionDB.CONN();
                    if (edtUserName.getText().toString().trim().equals("")){
                        edtUserName.setError("Chưa nhập tài khoản!");
                        edtUserName.requestFocus();
                    }else if (edtPassword.getText().toString().trim().equals("")){
                        edtPassword.setError("Chưa nhập mật khẩu!");
                        edtPassword.requestFocus();
                    }else if (connect==null){
                        Toast.makeText(getApplicationContext(),"Không có kết nối!",Toast.LENGTH_LONG).show();
                    }else {
                        String query = "SELECT * FROM MANAGER WHERE USERNAME='"+ edtUserName.getText().toString()+"' AND PASSWORD='"+
                                edtPassword.getText().toString()+"'";
                        Statement statement =connect.createStatement();
                        ResultSet rs = statement.executeQuery(query);
                        if (rs.next()){
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Tài khoản hoặc mật khẩu không chính xác!",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e){
                    Log.d("BBB",e.getMessage());
                }
            }
        });
    }
}
