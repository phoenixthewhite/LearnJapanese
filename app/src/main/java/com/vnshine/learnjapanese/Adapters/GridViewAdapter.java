package com.vnshine.learnjapanese.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vnshine.learnjapanese.Activities.CategoryActivity;
import com.vnshine.learnjapanese.CustomFont.TextView2;
import com.vnshine.learnjapanese.Models.GridViewItem;
import com.vnshine.learnjapanese.R;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by phoenix on 26/10/17.
 */

public class GridViewAdapter extends ArrayAdapter<GridViewItem> {
    Context context;
    LayoutInflater inflater;
    List<GridViewItem> items;
    InputStream is;

    public GridViewAdapter(@NonNull Context context, int resource, @NonNull List<GridViewItem> objects) {
        super(context, resource, objects);
        this.context = context;
        inflater = LayoutInflater.from(context);
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_category, parent, false);
            holder.categoryName = convertView.findViewById(R.id.category_name);
            holder.categoryImage = convertView.findViewById(R.id.category_image);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final GridViewItem item = items.get(position);
        holder.categoryImage.setImageResource(item.getImage());
        holder.categoryName.setText(getLanguage(item));
        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(Locale.getDefault().getDisplayLanguage());
                Intent intent = new Intent(finalConvertView.getContext(), CategoryActivity.class);
                intent.putExtra("category",getLanguage(item));
                intent.putExtra("category_id",item.getCategory().getId());
                finalConvertView.getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
    }

    private String getLanguage(GridViewItem item) {
        if (Locale.getDefault().getDisplayLanguage().equals("English")){
            return item.getCategory().getEnglish();
        }
        return item.getCategory().getVietnamese();
    }
}
