package com.example.todo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todo.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;

    private static final String DATABASE_NAME ="TODO_DATABASE";
    private static final String TABLE_NAME= "TODO_TABLE";
    private static final String COL_1= "ID";
    private static final String COL_2= "TASK";
    private static final String COL_3= "STATUS";

    public DatabaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertTask(ToDoModel model){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,model.getTask());
        values.put(COL_3,model.getStatus());
        sqLiteDatabase.insert(TABLE_NAME,null,values);
    }

    public void updateTask(int id,String task){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,task);
        sqLiteDatabase.update(TABLE_NAME,values,"ID=?",new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3,status);
        sqLiteDatabase.update(TABLE_NAME,values,"ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,"ID=?", new String[]{String.valueOf(id)});
    }

    public List<ToDoModel> getAllTasks(){
        sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=null;
        List<ToDoModel> modelList= new ArrayList<>();

        sqLiteDatabase.beginTransaction();

        try{
            cursor=sqLiteDatabase.query(TABLE_NAME,null,
                    null,null,null,null,null);
            if(cursor!= null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModel task=new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }
        return modelList;
    }
}
