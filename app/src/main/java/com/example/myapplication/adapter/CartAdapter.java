package com.example.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Interface.IImageClickListener;
import com.example.myapplication.R;
import com.example.myapplication.models.EventBus.TotalCountEvent;
import com.example.myapplication.models.ItemCart;
import com.example.myapplication.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<ItemCart> cartList;

    public CartAdapter(Context context, List<ItemCart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemCart itemCart = cartList.get(position);
        holder.item_cart_name.setText(itemCart.getName());
        holder.item_cart_quantity.setText(itemCart.getQuantity() + " ");
        Glide.with(context).load(itemCart.getImage()).into(holder.item_cart_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_cart_price.setText("Giá: " + decimalFormat.format(Double.parseDouble(itemCart.getPrice())));
        long total = itemCart.getQuantity() * Long.parseLong(itemCart.getPrice());
        holder.item_cart_total.setText(decimalFormat.format(Double.parseDouble(String.valueOf(total))));
        holder.item_cart_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Utils.listCartBuy.add(itemCart);
                    EventBus.getDefault().postSticky(new TotalCountEvent());
                } else {
                    for (int i = 0; i < Utils.listCartBuy.size(); i++) {
                        if (Utils.listCartBuy.get(i).getProduct_id() == itemCart.getProduct_id()) {
                            Utils.listCartBuy.remove(i);
                            EventBus.getDefault().postSticky(new TotalCountEvent());
                        }
                    }
                }
            }
        });
        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int position, int count) {
                if (count == 1) {
                    if (cartList.get(position).getQuantity() > 1) {
                        int newquantity = cartList.get(position).getQuantity() - 1;
                        cartList.get(position).setQuantity(newquantity);
                        holder.item_cart_quantity.setText(cartList.get(position).getQuantity() + " ");
                        long total = cartList.get(position).getQuantity() * Long.parseLong(cartList.get(position).getPrice());
                        holder.item_cart_total.setText(decimalFormat.format(Double.parseDouble(String.valueOf(total))));
                        EventBus.getDefault().postSticky(new TotalCountEvent());
                    } else if (cartList.get(position).getQuantity() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa thú cưng này khỏi giỏ hàng :((");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.listCart.remove(position);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TotalCountEvent());
                            }
                        });
                        builder.setNegativeButton("Giữ lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else if (count == 2) {
                    if (cartList.get(position).getQuantity() < 11) {
                        int newquantity = cartList.get(position).getQuantity() + 1;
                        cartList.get(position).setQuantity(newquantity);
                    }
                    holder.item_cart_quantity.setText(cartList.get(position).getQuantity() + " ");
                    long total = cartList.get(position).getQuantity() * Long.parseLong(cartList.get(position).getPrice());
                    holder.item_cart_total.setText(decimalFormat.format(Double.parseDouble(String.valueOf(total))));
                    EventBus.getDefault().postSticky(new TotalCountEvent());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_cart_image, item_cart_minus, item_cart_plus;
        TextView item_cart_name, item_cart_price, item_cart_quantity, item_cart_total;
        IImageClickListener listener;
        CheckBox item_cart_check;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_cart_image = itemView.findViewById(R.id.item_cart_image);
            item_cart_name = itemView.findViewById(R.id.item_cart_name);
            item_cart_price = itemView.findViewById(R.id.item_cart_price);
            item_cart_quantity = itemView.findViewById(R.id.item_cart_quantity);
            item_cart_total = itemView.findViewById(R.id.item_cart_total);
            item_cart_minus = itemView.findViewById(R.id.item_cart_minus);
            item_cart_plus = itemView.findViewById(R.id.item_cart_plus);
            item_cart_check = itemView.findViewById(R.id.item_cart_check);

            // event click
            item_cart_plus.setOnClickListener(this);
            item_cart_minus.setOnClickListener(this);
        }

        public void setListener(IImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (view == item_cart_minus) {
                listener.onImageClick(view, getAdapterPosition(), 1);
            } else if (view == item_cart_plus) {
                listener.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
