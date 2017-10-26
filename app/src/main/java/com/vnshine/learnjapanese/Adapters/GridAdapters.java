package com.vnshine.learnjapanese.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnshine.learnjapanese.Models.Category;
import com.vnshine.learnjapanese.R;

import java.util.List;

/**
 * Created by phoenix on 26/10/17.
 */

public class GridAdapters extends ArrayAdapter<Category> {
    Context context;
    LayoutInflater inflater;
    List<Category> categories;

    public GridAdapters(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.context = context;
        inflater = LayoutInflater.from(context);
        categories = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_category,parent,false);
            holder.categoryName = convertView.findViewById(R.id.category_name);
            holder.cateporyImage = convertView.findViewById(R.id.category_image);
            convertView.setTag(holder);
        } holder = (ViewHolder) convertView.getTag();

        Category category = categories.get(position);
        holder.cateporyImage.setImageResource(category.getId());
        holder.categoryName.setText(category.getName());
        return convertView;
    }

    class ViewHolder{
        ImageView cateporyImage;
        TextView categoryName;
    }
}
