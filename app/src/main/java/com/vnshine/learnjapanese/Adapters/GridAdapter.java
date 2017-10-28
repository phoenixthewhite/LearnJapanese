package com.vnshine.learnjapanese.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.vnshine.learnjapanese.CustomFont.TextView2;
import com.vnshine.learnjapanese.Models.ItemGridView;
import com.vnshine.learnjapanese.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by phoenix on 26/10/17.
 */

public class GridAdapter extends ArrayAdapter<ItemGridView> {
    Context context;
    LayoutInflater inflater;
    List<ItemGridView> items;
    InputStream is;

    public GridAdapter(@NonNull Context context, int resource, @NonNull List<ItemGridView> objects) {
        super(context, resource, objects);
        this.context = context;
        inflater = LayoutInflater.from(context);
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_category,parent,false);
            holder.categoryName = convertView.findViewById(R.id.category_name);
            holder.categoryImage = convertView.findViewById(R.id.category_image);
            convertView.setTag(holder);
        } holder = (ViewHolder) convertView.getTag();
        ItemGridView item = items.get(position);
        holder.categoryImage.setImageResource(item.getImage());
        holder.categoryName.setText(item.getCategory().getEnglish());
        return convertView;
    }

    class ViewHolder{
        ImageView categoryImage;
        TextView2 categoryName;
    }
}
