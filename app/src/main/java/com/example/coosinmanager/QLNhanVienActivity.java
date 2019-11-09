package com.example.coosinmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinmanager.adapter.NhanVienAdapter;
import com.example.coosinmanager.connection.ConnectionDB;
import com.example.coosinmanager.model.LoadingDialog;
import com.example.coosinmanager.model.NhanVien;
import com.example.coosinmanager.model.OnItemClickListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class QLNhanVienActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<NhanVien> nhanViens;
    NhanVienAdapter nhanVienAdapter;
    EditText edtFind;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> type = new ArrayList<String>();
    ArrayList<String> phone = new ArrayList<String>();
    ArrayList<String> id = new ArrayList<String>();
    String[] nameArr,typeArr,phoneArr,idArr;
    String textFind;
    TextView txtTotal;

    Connection connect;
    Button btnFind,btnDisable,btnEnable,btnBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnhan_vien);

        recyclerView = findViewById(R.id.recyclerview);
        btnFind = findViewById(R.id.btn_find);
        btnDisable = findViewById(R.id.btn_nv_disable);
        btnEnable = findViewById(R.id.btn_nv_enable);
        btnBlock = findViewById(R.id.btn_nv_block);
        edtFind = findViewById(R.id.edt_find);
        txtTotal = findViewById(R.id.total);

        //back button
        Toolbar toolbar = findViewById(R.id.toolbar_qlnv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nhanViens = new ArrayList<>();

        name.clear();
        type.clear();
        phone.clear();
        id.clear();
        try {
            ConnectionDB connectionDB = new ConnectionDB();
            connect = connectionDB.CONN();
            if (connect==null){
                Toast.makeText(getApplicationContext(),"Mất kết nối!",Toast.LENGTH_LONG).show();
            }else {
                String query = "SELECT*FROM EMPLOYEE";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()){
                    name.add(rs.getString("FULL_NAME"));
                    phone.add(rs.getString("PHONE_NUM"));
                    if (rs.getInt("EMP_TYPE")==1){
                        type.add("Nhân viên vệ sinh");
                        id.add("NVVS"+rs.getInt("ID"));
                    }else if (rs.getInt("EMP_TYPE")==2){
                        type.add("Nhân viên nấu ăn");
                        id.add("NVNA"+rs.getInt("ID"));
                    }else {
                        type.add("Nhân viên đa năng");
                        id.add("NVDN"+rs.getInt("ID"));
                    }
                }
                nameArr = new String[name.size()];
                nameArr = name.toArray(nameArr);
                typeArr = new String[type.size()];
                typeArr = type.toArray(typeArr);
                phoneArr = new String[phone.size()];
                phoneArr = phone.toArray(phoneArr);
                idArr = new String[id.size()];
                idArr = id.toArray(idArr);
                for (int i = 0; i < name.size();i++){
                    nhanViens.add(new NhanVien(nameArr[i],typeArr[i],phoneArr[i],idArr[i]));
                }
                connect.close();
            }
        }
        catch (Exception e){
            Log.d("BBB",e.getMessage());
        }
        txtTotal.setText("Tổng: "+nhanViens.size());
        nhanVienAdapter = new NhanVienAdapter(nhanViens);
        recyclerView.setLayoutManager(new LinearLayoutManager(QLNhanVienActivity.this));
        recyclerView.setAdapter(nhanVienAdapter);
        if (recyclerView.getAdapter() != null) {
            ((NhanVienAdapter) recyclerView.getAdapter()).setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View v, @NonNull int position) {
                    LoadingDialog loadingDialog = new LoadingDialog();
                    loadingDialog.loading(QLNhanVienActivity.this);
                    Intent detail = new Intent(QLNhanVienActivity.this,QLNhanVienOnlyActivity.class);
                    detail.putExtra("phone",phoneArr[position]);
                    startActivity(detail);
                }

                @Override
                public void onLongClick(View v, @NonNull int position) {

                }
            });
        }

        PushDownAnim.setPushDownAnimTo(btnFind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.clear();
                type.clear();
                phone.clear();
                id.clear();
                nhanViens.clear();

                try {
                    ConnectionDB connectionDB = new ConnectionDB();
                    connect = connectionDB.CONN();
                    if (connect==null){
                        Toast.makeText(getApplicationContext(),"Mất kết nối!",Toast.LENGTH_LONG).show();
                    }else if (edtFind.getText().toString().trim().equals("")){
                        edtFind.setError("Nhập thông tin muốn tìm kiếm!");
                        edtFind.requestFocus();
                    }else{
                        String query = "SELECT*FROM EMPLOYEE WHERE ID LIKE '%"+edtFind.getText().toString().trim()+"%'" +
                                " OR FULL_NAME LIKE N'%"+edtFind.getText().toString().trim()+"%'" +
                                " OR PHONE_NUM LIKE '%"+edtFind.getText().toString().trim()+"%'";
                        Statement statement = connect.createStatement();
                        ResultSet rs = statement.executeQuery(query);
                        while (rs.next()){
                            name.add(rs.getString("FULL_NAME"));
                            phone.add(rs.getString("PHONE_NUM"));
                            if (rs.getInt("EMP_TYPE")==1){
                                type.add("Nhân viên vệ sinh");
                                id.add("NVVS"+rs.getInt("ID"));
                            }else if (rs.getInt("EMP_TYPE")==2){
                                type.add("Nhân viên nấu ăn");
                                id.add("NVNA"+rs.getInt("ID"));
                            }else {
                                type.add("Nhân viên đa năng");
                                id.add("NVDN"+rs.getInt("ID"));
                            }
                        }
                        if (name.size()==0){
                            Toast.makeText(getApplicationContext(),"Không tìm thấy nhân viên nào!",Toast.LENGTH_LONG).show();
                        }
                        nameArr = new String[name.size()];
                        nameArr = name.toArray(nameArr);
                        typeArr = new String[type.size()];
                        typeArr = type.toArray(typeArr);
                        phoneArr = new String[phone.size()];
                        phoneArr = phone.toArray(phoneArr);
                        idArr = new String[id.size()];
                        idArr = id.toArray(idArr);
                        for (int i = 0; i < name.size();i++){
                            nhanViens.add(new NhanVien(nameArr[i],typeArr[i],phoneArr[i],idArr[i]));
                        }
                        connect.close();
                    }
                }
                catch (Exception e){
                    Log.d("BBB",e.getMessage());
                }
                txtTotal.setText("Tổng: "+nhanViens.size());
                nhanVienAdapter = new NhanVienAdapter(nhanViens);
                nhanVienAdapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(QLNhanVienActivity.this));
                recyclerView.setAdapter(nhanVienAdapter);
                if (recyclerView.getAdapter() != null) {
                    ((NhanVienAdapter) recyclerView.getAdapter()).setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onClick(View v, @NonNull int position) {
                            LoadingDialog loadingDialog = new LoadingDialog();
                            loadingDialog.loading(QLNhanVienActivity.this);
                            Intent detail = new Intent(QLNhanVienActivity.this,QLNhanVienOnlyActivity.class);
                            detail.putExtra("phone",phoneArr[position]);
                            startActivity(detail);
                        }

                        @Override
                        public void onLongClick(View v, @NonNull int position) {

                        }
                    });
                }
            }
        });

        PushDownAnim.setPushDownAnimTo(btnDisable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option(1);
            }
        });

        PushDownAnim.setPushDownAnimTo(btnEnable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option(2);
            }
        });

        PushDownAnim.setPushDownAnimTo(btnBlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option(0);
            }
        });

    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void option(Integer status){
        name.clear();
        type.clear();
        phone.clear();
        id.clear();
        nhanViens.clear();

        try {
            ConnectionDB connectionDB = new ConnectionDB();
            connect = connectionDB.CONN();
            if (connect==null){
                Toast.makeText(getApplicationContext(),"Mất kết nối!",Toast.LENGTH_LONG).show();
            }else{
                String query = "SELECT*FROM EMPLOYEE WHERE USER_STATUS="+status+"";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()){
                    name.add(rs.getString("FULL_NAME"));
                    phone.add(rs.getString("PHONE_NUM"));
                    if (rs.getInt("EMP_TYPE")==1){
                        type.add("Nhân viên vệ sinh");
                        id.add("NVVS"+rs.getInt("ID"));
                    }else if (rs.getInt("EMP_TYPE")==2){
                        type.add("Nhân viên nấu ăn");
                        id.add("NVNA"+rs.getInt("ID"));
                    }else {
                        type.add("Nhân viên đa năng");
                        id.add("NVDN"+rs.getInt("ID"));
                    }
                }
                if (name.size()==0){
                    Toast.makeText(getApplicationContext(),"Không tìm thấy nhân viên nào!",Toast.LENGTH_LONG).show();
                }
                nameArr = new String[name.size()];
                nameArr = name.toArray(nameArr);
                typeArr = new String[type.size()];
                typeArr = type.toArray(typeArr);
                phoneArr = new String[phone.size()];
                phoneArr = phone.toArray(phoneArr);
                idArr = new String[id.size()];
                idArr = id.toArray(idArr);
                for (int i = 0; i < name.size();i++){
                    nhanViens.add(new NhanVien(nameArr[i],typeArr[i],phoneArr[i],idArr[i]));
                }
                connect.close();
            }
        }
        catch (Exception e){
            Log.d("BBB",e.getMessage());
        }
        txtTotal.setText("Tổng: "+nhanViens.size());
        nhanVienAdapter = new NhanVienAdapter(nhanViens);
        nhanVienAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(QLNhanVienActivity.this));
        recyclerView.setAdapter(nhanVienAdapter);
        if (recyclerView.getAdapter() != null) {
            ((NhanVienAdapter) recyclerView.getAdapter()).setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View v, @NonNull int position) {
                    LoadingDialog loadingDialog = new LoadingDialog();
                    loadingDialog.loading(QLNhanVienActivity.this);
                    Intent detail = new Intent(QLNhanVienActivity.this,QLNhanVienOnlyActivity.class);
                    detail.putExtra("phone",phoneArr[position]);
                    startActivity(detail);
                }

                @Override
                public void onLongClick(View v, @NonNull int position) {

                }
            });
        }
    }
}
