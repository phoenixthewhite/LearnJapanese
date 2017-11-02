package com.vnshine.learnjapanese.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.vnshine.learnjapanese.Models.Category;

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
        super(context, DB_NAME, null, 1);
        mContext = context;
        doCreateDb();
    }

    public void doCreateDb(){
     db = mContext.openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
        System.out.println("MMMM"+db);
        if (db!= null){
            Log.e("bbb",db.toString());
        }
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
    static String DB_NAME= "db.sqlite";
    static String DB_PATH="/databases/";
    static InputStream is;
    public static void xuLiSaoChepCSDL(Context context){
        File file= context.getDatabasePath(DB_NAME);
        if(!file.exists()){
            try {
                copyCSDLtuAssetvaoApp(context);
                System.out.println("thanh cong");
            }
            catch (Exception e){
                System.out.println("loi1111:"+e.toString());
            }
        }
    }
    public static void copyCSDLtuAssetvaoApp(Context context){
        try{
            is = context.getAssets().open(DB_NAME);
            File f= new File(context.getApplicationInfo().dataDir+DB_PATH);
            if(!f.exists())
                f.mkdir();
            OutputStream os = new FileOutputStream(getUrl(context));
            byte[] buffer= new byte[1024];
            int length;
            while((length=is.read(buffer))>0){
                os.write(buffer,0,length);
            }
            os.flush();
            os.close();
            is.close();

        }catch(Exception e){
            System.out.println("loi2222:" +e.toString());
        }
    }
    public static String getUrl(Context context) {
        return context.getApplicationInfo().dataDir+DB_PATH+DB_NAME;
    }


    public static  void saveToSd(Context context){
        File f= new File(context.getApplicationInfo().dataDir+DB_PATH+DB_NAME);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            String outFileName = Environment.getExternalStorageDirectory()+"/database.db";
            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);
            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer))>0){
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
        }
        finally {
        }
        // Close the streams
    }

    @Override
    public synchronized void close() {
        super.close();
        db.close();
    }
    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> categories=new ArrayList<>();

        try {
            Cursor cursor = db.query("category",null,null,null,null,null,null);
            while(cursor.moveToNext()){
                // System.out.println("---------------------------------");
                int id=cursor.getInt(0);
                String english=cursor.getString(1);
                String vietnamese=cursor.getString(2);
                Category category=new Category(id,english,vietnamese);
                categories.add(category);
            }
            cursor.close();
            System.out.println("categoties : "+categories.size());
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return categories;
    }
//    public ArrayList<QuanHuyen> getAllQuanHuyen(){
//        ArrayList<QuanHuyen> quanHuyens=new ArrayList<>();
//
//        try {
//            Cursor cursor = db.query(Constraint.TABLE_HUYEN,null,null,null,null,null,Constraint.MA_TP+" ASC");
//            while(cursor.moveToNext()){
//                int id=cursor.getInt(0);
//                String name=cursor.getString(1);
//                String type=cursor.getString(2);
//                int matp=cursor.getInt(3);
//                QuanHuyen quanHuyen=new QuanHuyen(id,name,type,matp);
//                quanHuyens.add(quanHuyen);
//            }
//            cursor.close();
//            System.out.println("quan huyen: "+quanHuyens.size());
//        }catch (Exception e){
//            System.out.println(e.toString());
//        }
//        return quanHuyens;
//    }



}
