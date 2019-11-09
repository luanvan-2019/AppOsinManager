package com.example.coosinmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinmanager.R;
import com.example.coosinmanager.model.NhanVien;
import com.example.coosinmanager.model.OnItemClickListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.NhanVienHolder> {

    ArrayList<NhanVien> mangNhanVien;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public NhanVienAdapter(ArrayList<NhanVien> mangNhanVien) {
        this.mangNhanVien = mangNhanVien;
    }

    @NonNull
    @Override
    public NhanVienAdapter.NhanVienHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_qlnv,null);
        return new NhanVienHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhanVienAdapter.NhanVienHolder holder, int position) {
        NhanVien nhanVien = mangNhanVien.get(position);
        holder.txtName.setText(nhanVien.getName());
        holder.txtType.setText(nhanVien.getType());
        holder.txtPhone.setText(nhanVien.getPhone());
        holder.txtID.setText(nhanVien.getID());
    }

    @Override
    public int getItemCount() {
        return mangNhanVien.size() > 0 ? mangNhanVien.size() : 0;
    }

    class NhanVienHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtType,txtPhone,txtID;
        //truyen item view vao va anh xa
        public NhanVienHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_name);
            txtType = itemView.findViewById(R.id.txt_type);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            txtID = itemView.findViewById(R.id.txt_mnv);

            PushDownAnim.setPushDownAnimTo(itemView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, getLayoutPosition());
                }
            });
            PushDownAnim.setPushDownAnimTo(itemView).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(view, getLayoutPosition());
                    return true;
                }
            });
        }
    }
}
