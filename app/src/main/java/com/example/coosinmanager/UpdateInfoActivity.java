package com.example.coosinmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coosinmanager.connection.ConnectionDB;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateInfoActivity extends AppCompatActivity {

    String phone;
    EditText edtName, edtCMND, edtBirthday, edtAddress;
    Button btnSave, btnUpImage;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        edtName = findViewById(R.id.edt_hvt);
        edtCMND = findViewById(R.id.edt_cmnd);
        edtAddress = findViewById(R.id.edt_address);
        edtBirthday = findViewById(R.id.edt_birthday);
        btnSave = findViewById(R.id.btnSave);
        btnUpImage = findViewById(R.id.btnUpImage);

        //back button
        Toolbar toolbar = findViewById(R.id.toolbar_update_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        phone = getIntent().getStringExtra("phone");

        try {
            ConnectionDB connectionDB = new ConnectionDB();
            connect = connectionDB.CONN();
            if (connect==null){
                Toast.makeText(getApplicationContext(),"không có kết nối mạng!",Toast.LENGTH_LONG).show();
            }else {
                String query= "SELECT * FROM EMPLOYEE WHERE PHONE_NUM='"+phone+"'";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()){
                    edtName.setText(rs.getString("FULL_NAME"));
                    if (!rs.getString("CMND").equals("Chưa cập nhật")){
                        edtCMND.setText(rs.getString("CMND"));
                        edtBirthday.setText(rs.getString("BIRTHDAY"));
                        edtAddress.setText(rs.getString("ADDRESS"));
                    }
                }
            }

        }
        catch (Exception e){
            Log.d("BBB",e.getMessage());
        }

        PushDownAnim.setPushDownAnimTo(btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ConnectionDB connectionDB = new ConnectionDB();
                    connect = connectionDB.CONN();
                    if (connect==null){
                        Toast.makeText(getApplicationContext(),"không có kết nối mạng!",Toast.LENGTH_LONG).show();
                    }if (edtCMND.getText().toString().trim().equals("") || edtAddress.getText().toString().trim().equals("")||
                            edtName.getText().toString().trim().equals("")||edtBirthday.getText().toString().trim().equals("")){
                        Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                    }
                    else {
                        String query= "UPDATE EMPLOYEE SET FULL_NAME=N'"+edtName.getText().toString().trim()+"'," +
                                " CMND='"+edtCMND.getText().toString().trim()+"', BIRTHDAY='"+edtBirthday.getText().toString().trim()+"'," +
                                " ADDRESS='"+edtAddress.getText().toString().trim()+"' WHERE PHONE_NUM='"+phone+"'";
                        Statement statement = connect.createStatement();
                        statement.executeQuery(query);
                    }
                    connect.close();

                }
                catch (Exception e){
                    Log.d("BBB",e.getMessage());
                }
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();

            }
        });
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
