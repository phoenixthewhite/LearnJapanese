package com.vnshine.learnjapanese.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.vnshine.learnjapanese.Models.Category;
import com.vnshine.learnjapanese.Models.JapaneseSentence;
import com.vnshine.learnjapanese.Models.Meaning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 5);
        mContext = context;
        doCreateDb();
    }

    public void doCreateDb() {
        db = mContext.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "category");
        db.execSQL("DROP TABLE IF EXISTS " + "sentences");
        onCreate(db);
    }

//    @Override
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        throw new SQLiteException("Can't downgrade ");
//    }

    static String DB_NAME = "db.sqlite";
    static String DB_PATH = "/databases/";
    static InputStream is;

    public static void handleCopyingDataBase(Context context) {
        File file = context.getDatabasePath(DB_NAME);
        if (!file.exists()) {
            try {
                copyDBFromAssetsToApp(context);
                System.out.println("thanh cong");
            } catch (Exception e) {
                System.out.println("loi1111:" + e.toString());
            }
        }
    }

    public static void copyDBFromAssetsToApp(Context context) {
        try {
            is = context.getAssets().open(DB_NAME);
            File f = new File(context.getApplicationInfo().dataDir + DB_PATH);
            if (!f.exists())
                f.mkdir();
            OutputStream os = new FileOutputStream(getUrl(context));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();

        } catch (Exception e) {
            System.out.println("loi2222:" + e.toString());
        }
    }

    public static String getUrl(Context context) {
        return context.getApplicationInfo().dataDir + DB_PATH + DB_NAME;
    }


    public static void saveToSd(Context context) {
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH + DB_NAME);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            String outFileName = Environment.getExternalStorageDirectory() + "/database.db";
            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);
            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            System.out.println("aaaaaaaaaaaaaaaaaaa");
            output.flush();
            output.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        // Close the streams
    }

    @Override
    public synchronized void close() {
        super.close();
        db.close();
    }

    ArrayList<Meaning> listMeanings = new ArrayList<>();
    ArrayList<JapaneseSentence> listJapansesSentences = new ArrayList<>();


    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();

        try {
            Cursor cursor = db.query("category", null, null, null,
                    null, null, null);
            while (cursor.moveToNext()) {
                // System.out.println("---------------------------------");
                int id = cursor.getInt(0);
                String english = cursor.getString(1);
                String vietnamese = cursor.getString(2);
                Category category = new Category(id, english, vietnamese);
                categories.add(category);
            }
            cursor.close();
            System.out.println("categoties : " + categories.size());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return categories;
    }

    public void getAllSentences(String category) {
//        ArrayList<Sentence> sentences = new ArrayList<>();
        try {
            Cursor cursor = db.query("sentences", null, "category_id = ?",
                    new String[]{category}, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int category_id = cursor.getInt(1);
                String english = cursor.getString(2);
                String pinyin = cursor.getString(3);
                String japanese = cursor.getString(4);
                int favorite = cursor.getInt(5);
                String voice = cursor.getString(6);
                int status = cursor.getInt(7);
                String vietnamese = cursor.getString(8);
                JapaneseSentence japaneseSentence = new JapaneseSentence(id, category_id,
                        pinyin, japanese);
                Meaning meaning = new Meaning(id, category_id, english, favorite,
                        voice, status, vietnamese);
                this.listJapansesSentences.add(japaneseSentence);
                this.listMeanings.add(meaning);
            }
            cursor.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void getAllFavoriteSentences() {
//        ArrayList<Sentence> sentences = new ArrayList<>();
        try {
            Cursor cursor = db.query("sentences", null, "favorite = 1",
                    null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int category_id = cursor.getInt(1);
                String english = cursor.getString(2);
                String pinyin = cursor.getString(3);
                String japanese = cursor.getString(4);
                int favorite = cursor.getInt(5);
                String voice = cursor.getString(6);
                int status = cursor.getInt(7);
                String vietnamese = cursor.getString(8);
                JapaneseSentence japaneseSentence = new JapaneseSentence(id, category_id,
                        pinyin, japanese);
                Meaning meaning = new Meaning(id, category_id, english, favorite,
                        voice, status, vietnamese);
                this.listJapansesSentences.add(japaneseSentence);
                this.listMeanings.add(meaning);
            }
            cursor.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public ArrayList<Meaning> getListMeanings() {
        return this.listMeanings;
    }

    public ArrayList<JapaneseSentence> getListJapansesSentences() {
        return this.listJapansesSentences;
    }
}
