package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Orders;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<Orders> listOrders;

    public OrdersAdapter(Context context, List<Orders> listOrders) {
        this.context = context;
        this.listOrders = listOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Orders orders = listOrders.get(position);
        holder.txtorder.setText("Đơn hàng: " + orders.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rcDetail.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(orders.getItem().size());

        // adapter detail
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(context, orders.getItem());
        holder.rcDetail.setLayoutManager(layoutManager);
        holder.rcDetail.setAdapter(orderDetailAdapter);
        holder.rcDetail.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtorder;
        RecyclerView rcDetail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtorder = itemView.findViewById(R.id.id_order);
            rcDetail = itemView.findViewById(R.id.rc_order_detail);
        }
    }
}
