package com.vnshine.learnjapanese.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vnshine.learnjapanese.Models.ListViewItem;
import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.util.List;

/**
 * Created by phoenix on 30/10/17.
 */

public class ListViewAdapter extends ArrayAdapter<ListViewItem> {
    Context context;
    List<ListViewItem> items;
    LayoutInflater inflater;

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull List<ListViewItem> objects) {
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
            convertView = inflater.inflate(R.layout.item_sentence,parent,false);
            holder = new ViewHolder();
            holder.sentence = convertView.findViewById(R.id.mean);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        ListViewItem item = items.get(position);
        holder.sentence.setText(item.getSentence());
        return convertView;
    }

    class ViewHolder{
        TextView sentence;
    }
}
