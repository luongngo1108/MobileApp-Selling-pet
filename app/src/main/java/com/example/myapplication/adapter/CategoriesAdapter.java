package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.Categories;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {
    List<Categories> array;
    Context context;

    public CategoriesAdapter(Context context, List<Categories> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        if (array == null) {
            return 0;
        }
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder {
        TextView txtName;
        ImageView image;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_categories, null);
            viewHolder.txtName = view.findViewById(R.id.item_name);
            viewHolder.image = view.findViewById(R.id.item_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtName.setText(array.get(i).getName());
        Glide.with(context).load(array.get(i).getImage()).into(viewHolder.image);
        return view;
    }
}
