package com.vnshine.learnjapanese.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vnshine.learnjapanese.DataBase.DatabaseHelper;
import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by phoenix on 30/10/17.
 */

public class ListViewAdapter extends ArrayAdapter<Sentence> {
    Context context;
    List<Sentence> sentences;
    LayoutInflater inflater;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    MediaPlayer mp;

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull List<Sentence> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
        sentences = objects;
        mp = new MediaPlayer();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sentence, parent, false);
            holder = new ViewHolder();
            holder.meaning = convertView.findViewById(R.id.meaning);
            holder.favorite = convertView.findViewById(R.id.favorite);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        final Sentence sentence = sentences.get(position);
        holder.meaning.setText(getLanguage(sentence));
        if (sentence.getFavorite() == 0) {
            holder.favorite.setChecked(false);
        } else holder.favorite.setChecked(true);
        final int identifier = context.getResources().getIdentifier(sentence.getVoice()
                + "_f", "raw", context.getPackageName());
        setClickListenner(convertView, identifier);
        setFavorite(holder.favorite, sentence);
        return convertView;
    }

    private String getLanguage(Sentence sentence) {
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            return sentence.getEnglish();
        }
        return sentence.getVietnamese();
    }

    private void setClickListenner(View view, final int identifier) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mp.isPlaying()) {
                    mp.stop();
                    mp = MediaPlayer.create(context,identifier);
                    mp.start();
                } else {
                    mp = MediaPlayer.create(context, identifier);
                    mp.start();
                }

            }
        });
    }

    private void setFavorite(CheckBox checkBox, Sentence sentence) {
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        dbHelper = new DatabaseHelper(context);
        if (checkBox.isChecked()) {
            sentence.setFavorite(1);
            updateFavorite(sentence);
        } else {
            sentence.setFavorite(0);
            updateFavorite(sentence);
        }
    }

    public void updateFavorite(Sentence sentence) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("favorite", sentence.getFavorite());
        // updating row
        db.update("sentences", values, "_id = ?",
                new String[]{String.valueOf(sentence.getId())});
        db.close();
    }

    class ViewHolder {
        TextView meaning;
        CheckBox favorite;
    }
}
