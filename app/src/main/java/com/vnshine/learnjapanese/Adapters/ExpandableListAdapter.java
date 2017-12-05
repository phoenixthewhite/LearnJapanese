package com.vnshine.learnjapanese.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.vnshine.learnjapanese.DataBase.DatabaseHelper;
import com.vnshine.learnjapanese.Models.JapaneseSentence;
import com.vnshine.learnjapanese.Models.Meaning;
import com.vnshine.learnjapanese.R;
import com.vnshine.learnjapanese.Dialog.SpeechPopUp;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by phoenix on 09/11/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<JapaneseSentence> listJapaneseSentences;
    private ArrayList<Meaning> listMeanings;
    //    private HashMap<Meaning,JapaneseSentence> listHashMap;
    LayoutInflater inflater;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    MediaPlayer mp;

    public ExpandableListAdapter(Context context, ArrayList<JapaneseSentence> listJapaneseSentences,
                                 ArrayList<Meaning> listMeanings) {
        this.context = context;
        this.listJapaneseSentences = listJapaneseSentences;
        this.listMeanings = listMeanings;
        inflater = LayoutInflater.from(context);
        mp = new MediaPlayer();
    }

    @Override
    public int getGroupCount() {
        return listMeanings.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listMeanings.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listJapaneseSentences.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        MeaningHolder holder;
        convertView = null;
        if (convertView == null) {
            holder = new MeaningHolder();
            convertView = inflater.inflate(R.layout.item_list_group, parent, false);
            holder.meaning = convertView.findViewById(R.id.meaning);
            holder.favorite = convertView.findViewById(R.id.favorite);
            convertView.setTag(holder);
        } else holder = (MeaningHolder) convertView.getTag();
        Meaning meaning = listMeanings.get(groupPosition);
        holder.meaning.setText(getLanguage(meaning));
        if (meaning.getFavorite() == 0) {
            holder.favorite.setChecked(false);
        } else holder.favorite.setChecked(true);
        final int identifier = context.getResources().getIdentifier(meaning.getVoice()
                + "_f", "raw", context.getPackageName());
//        setGroupClickListener(convertView, identifier, isExpanded );
        setFavorite(holder.favorite, meaning);

        return convertView;
    }

    private void setFavorite(final CheckBox checkBox, final Meaning meaning) {
        dbHelper = new DatabaseHelper(context);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    meaning.setFavorite(1);
                    updateFavorite(meaning);
                    System.out.println("favorite: " + meaning.getFavorite());

                } else {
                    meaning.setFavorite(0);
                    updateFavorite(meaning);
                    System.out.println("Favorite: " + meaning.getFavorite());
                }
            }
        });
    }

    public void updateFavorite(Meaning meaning) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("favorite", meaning.getFavorite());
        // updating row
        db.update("sentences", values, "_id = ?",
                new String[]{String.valueOf(meaning.getId())});
        db.close();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        int identifier = context.getResources().getIdentifier(listMeanings.get(groupPosition).getVoice()
                + "_f", "raw", context.getPackageName());
        if (mp.isPlaying()) {
            mp.stop();
            mp = MediaPlayer.create(context, identifier);
            mp.start();
        } else {
            mp = MediaPlayer.create(context, identifier);
            mp.start();
        }
    }

    private String getLanguage(Meaning meaning) {
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            return meaning.getEnglish();
        }
        return meaning.getVietnamese();

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        JapaneseSentenceHolder holder;
        if (convertView == null) {
            holder = new JapaneseSentenceHolder();
            convertView = inflater.inflate(R.layout.item_list_item, parent, false);
            holder.japanese = convertView.findViewById(R.id.japanese);
            holder.pinyin = convertView.findViewById(R.id.pinyin);
            holder.listening = convertView.findViewById(R.id.listening);
            holder.speaking = convertView.findViewById(R.id.speaking);
            YoYo.with(Techniques.FadeInDown)
                    .duration(500)
                    .playOn(convertView);
            convertView.setTag(holder);
        } else holder = (JapaneseSentenceHolder) convertView.getTag();
        JapaneseSentence japaneseSentence = (JapaneseSentence) getChild(groupPosition, childPosition);
        holder.japanese.setText(japaneseSentence.getJapanese());
        holder.pinyin.setText(japaneseSentence.getPinyin());
        YoYo.with(Techniques.FadeInDown)
                .duration(500)
                .playOn(convertView);
        int identifier = context.getResources().getIdentifier(listMeanings.get(groupPosition).getVoice()
                + "_f", "raw", context.getPackageName());
        playAudio(holder.listening, identifier);
        addSpeakingListener(holder.speaking, japaneseSentence.getJapanese(),
                japaneseSentence.getPinyin(),identifier);
        return convertView;
    }

    private void addSpeakingListener(ImageView imageView, final String japanese,
                                     final String pinyin, final int identifier) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                if (isInternetAvailable()){
                    SpeechPopUp speechPopUp = new SpeechPopUp(context);
                    speechPopUp.setContent(japanese, pinyin, identifier);
                    speechPopUp.show();
//                    Toast.makeText(context, "Connection avalable",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(context, R.string.no_internet,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void playAudio(final ImageView imageView, final int identifier) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.listen_focus);
                if (mp.isPlaying()) {
                    mp.stop();
                    mp = MediaPlayer.create(context, identifier);
                    mp.start();
                } else {
                    mp = MediaPlayer.create(context, identifier);
                    mp.start();
                }
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        imageView.setImageResource(R.drawable.listen);
                    }
                });
            }
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }

    class MeaningHolder {
        TextView meaning;
        CheckBox favorite;
    }

    class JapaneseSentenceHolder {
        TextView japanese;
        TextView pinyin;
        ImageView listening;
        ImageView speaking;
    }
}
