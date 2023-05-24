package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.Item;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    Context context;
    List<Item> itemList;

    public OrderDetailAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtname.setText(item.getName() + "");
        holder.txtquantity.setText("Số lượng: " + item.getQuantity() + "");
        Glide.with(context).load(item.getImage()).into(holder.image_orderdetail);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image_orderdetail;
        TextView txtname, txtquantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_orderdetail = itemView.findViewById(R.id.item_orderdetail_image);
            txtname = itemView.findViewById(R.id.item_orderdetail_name);
            txtquantity = itemView.findViewById(R.id.item_orderdetail_quantity);
        }
    }
}
