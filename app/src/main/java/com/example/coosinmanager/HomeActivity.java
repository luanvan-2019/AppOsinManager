package com.example.coosinmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thekhaeng.pushdownanim.PushDownAnim;

public class HomeActivity extends AppCompatActivity {

    CardView cardQLNV, cardQLLich, cardQLThongKe, cardQLGia,cardQLQC,cardCSKH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardQLNV = findViewById(R.id.card_ql_nhanvien);
        cardQLLich = findViewById(R.id.card_ql_lich);
        cardQLThongKe = findViewById(R.id.card_ql_thongke);
        cardQLGia = findViewById(R.id.card_ql_gia);
        cardQLQC = findViewById(R.id.card_quangcao);
        cardCSKH = findViewById(R.id.card_contact);

        PushDownAnim.setPushDownAnimTo(cardQLNV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,QLNhanVienActivity.class);
                startActivity(intent);
            }
        });
    }
}
