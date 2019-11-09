package com.example.coosinmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coosinmanager.connection.ConnectionDB;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class QLNhanVienOnlyActivity extends AppCompatActivity {

    TextView txtID, txtStatus, txtNameTitle, txtNameDetails,txtPhone,txtCNMD,txtBirt,txtAddress,txtCreateAt;
    String phone;
    Connection connect;
    Button btnUpdate,btnEnable,btnBlock,btnCall,btnChat,btnShowLich;
    int REQUEST_CODE_UPDATE = 1997;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnhan_vien_only);

        txtID = findViewById(R.id.txt_mnv);
        txtStatus = findViewById(R.id.txt_status);
        txtNameTitle = findViewById(R.id.txt_hvt_avatar);
        txtNameDetails = findViewById(R.id.txt_hvt);
        txtPhone = findViewById(R.id.txt_sdt);
        txtCNMD = findViewById(R.id.txt_cmnd);
        txtBirt = findViewById(R.id.txt_ngay_sinh);
        txtAddress = findViewById(R.id.txt_address);
        txtCreateAt = findViewById(R.id.txt_create_at);
        btnUpdate = findViewById(R.id.btnUpdateTT);
        btnBlock = findViewById(R.id.btnBlock);
        btnEnable = findViewById(R.id.btnEnable);
        btnCall = findViewById(R.id.btnCall);
        btnChat = findViewById(R.id.btnChat);
        btnShowLich = findViewById(R.id.btnShowLich);

        //back button
        Toolbar toolbar = findViewById(R.id.toolbar_qlnv_only);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        phone = getIntent().getStringExtra("phone");
        txtPhone.setText(phone);

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
                    if (rs.getInt("EMP_TYPE")==1){
                        txtID.setText("NVVS"+rs.getInt("ID"));
                    }else if (rs.getInt("EMP_TYPE")==2){
                        txtID.setText("NVNA"+rs.getInt("ID"));
                    }else txtID.setText("NVDN"+rs.getInt("ID"));
                    txtNameTitle.setText(rs.getString("FULL_NAME"));
                    txtNameDetails.setText(rs.getString("FULL_NAME"));
                    if (rs.getInt("USER_STATUS")==0){
                        txtStatus.setTextColor(Color.RED);
                        txtStatus.setText("Đã khóa");
                        btnBlock.setText("Mở khóa");
                    }else if (rs.getInt("USER_STATUS")==1){
                        txtStatus.setTextColor(Color.BLUE);
                        txtStatus.setText("Chưa kích hoạt");
                    }else {
                        txtStatus.setTextColor(Color.GREEN);
                        txtStatus.setText("Đã kích hoạt");
                    }
                    txtCNMD.setText(rs.getString("CMND"));
                    txtBirt.setText(rs.getString("BIRTHDAY"));
                    txtAddress.setText(rs.getString("ADDRESS"));
                    txtCreateAt.setText(rs.getString("CREATE_AT"));
                }
            }

        }
        catch (Exception e){
            Log.d("BBB",e.getMessage());
        }

        PushDownAnim.setPushDownAnimTo(btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLNhanVienOnlyActivity.this,UpdateInfoActivity.class);
                intent.putExtra("phone",phone);
                startActivityForResult(intent,REQUEST_CODE_UPDATE);
            }
        });

        PushDownAnim.setPushDownAnimTo(btnEnable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCNMD.getText().toString().trim().equals("Chưa cập nhật")){
                    Toast.makeText(getApplicationContext(),"Vui lòng cập nhật hồ sơ để kích hoạt tài khoản!",Toast.LENGTH_LONG).show();
                }
                if (txtStatus.getText().toString().trim().equals("Đã kích hoạt")){
                    Toast.makeText(getApplicationContext(),"Tài khoản này đã được kích hoạt!",Toast.LENGTH_LONG).show();
                }
                if (txtStatus.getText().toString().trim().equals("Đã khóa")){
                    Toast.makeText(getApplicationContext(),"Tài khoản này đang bị khóa, không thể kích hoạt!",Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        ConnectionDB connectionDB = new ConnectionDB();
                        connect = connectionDB.CONN();
                        if (connect==null){
                            Toast.makeText(getApplicationContext(),"không có kết nối mạng!",Toast.LENGTH_LONG).show();
                        }else {
                            String query= "UPDATE EMPLOYEE SET USER_STATUS=2 WHERE PHONE_NUM='"+phone+"'";
                            Statement statement = connect.createStatement();
                            statement.executeQuery(query);
                        }
                        connect.close();

                    }
                    catch (Exception e){
                        txtStatus.setTextColor(Color.GREEN);
                        txtStatus.setText("Đã kích hoạt");
                        Toast.makeText(getApplicationContext(),"Kích hoạt thành công!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        PushDownAnim.setPushDownAnimTo(btnBlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        PushDownAnim.setPushDownAnimTo(btnCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "tel:" + phone;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(s));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK && data != null){
            finish();
            Intent intent = new Intent(QLNhanVienOnlyActivity.this,QLNhanVienOnlyActivity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showAlertDialog(){
        if (txtStatus.getText().toString().trim().equals("Đã kích hoạt")||txtStatus.getText().toString().trim().equals("Chưa kích hoạt")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận");
            builder.setMessage("Bạn có muốn khóa tài khoản này không?");
            builder.setCancelable(false);
            builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    try {
                        ConnectionDB connectionDB = new ConnectionDB();
                        connect = connectionDB.CONN();
                        if (connect==null){
                            Toast.makeText(getApplicationContext(),"không có kết nối mạng!",Toast.LENGTH_LONG).show();
                        }else {
                            String query= "UPDATE EMPLOYEE SET USER_STATUS=0 WHERE PHONE_NUM='"+phone+"'";
                            Statement statement = connect.createStatement();
                            statement.executeQuery(query);
                        }
                        connect.close();

                    }
                    catch (Exception e){
                        txtStatus.setTextColor(Color.RED);
                        txtStatus.setText("Đã khóa");
                        Toast.makeText(getApplicationContext(),"Tài khoản đã bị khóa!",Toast.LENGTH_LONG).show();
                    }

                }
            });
            builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận");
            builder.setMessage("Bạn có muốn mở khóa tài khoản này không?");
            builder.setCancelable(false);
            builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    try {
                        ConnectionDB connectionDB = new ConnectionDB();
                        connect = connectionDB.CONN();
                        if (connect==null){
                            Toast.makeText(getApplicationContext(),"không có kết nối mạng!",Toast.LENGTH_LONG).show();
                        }else {
                            String query= "UPDATE EMPLOYEE SET USER_STATUS=1 WHERE PHONE_NUM='"+phone+"'";
                            Statement statement = connect.createStatement();
                            statement.executeQuery(query);
                        }
                        connect.close();

                    }
                    catch (Exception e){
                        btnBlock.setText("Khóa");
                        txtStatus.setTextColor(Color.BLUE);
                        txtStatus.setText("Chưa kích hoạt");
                        Toast.makeText(getApplicationContext(),"Tài khoản đã được mở khóa!",Toast.LENGTH_LONG).show();
                    }

                }
            });
            builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
}
