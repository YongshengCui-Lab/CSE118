package com.example.assignment2222;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    static int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "image_database";
    static String TABLE_NAME = "image_table";
    static Context context;
    static String KEY_ID = "id";
    static String KEY_NAME = "name";
    static String KEY_IMAGE = "image";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DatabaseHelper.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL( "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " STRING, " + KEY_IMAGE + " BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String DROP_TABLE = "DROP TABLE IF EXISTS";
        if(oldVersion == DATABASE_VERSION){
            db = getWritableDatabase();
            db.execSQL(DROP_TABLE + TABLE_NAME);
            onCreate(db);
            DATABASE_VERSION = newVersion;
        }
    }
    public void addImage(Image image){
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] image_bytes = bitMap_to_byte(image.getImage_source(), 100);
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, image.getName());
        values.put(KEY_IMAGE, image_bytes);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Image getImage(Image image){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_NAME + " = " + "'" + image.getName() + "'", null);

        if(cursor != null){
            cursor.moveToFirst();
            int id_index = cursor.getColumnIndex(KEY_ID), name_index = cursor.getColumnIndex(KEY_NAME), image_index = cursor.getColumnIndex(KEY_IMAGE), image_id = cursor.getInt(id_index);
            String image_name = cursor.getString(name_index);
            Bitmap image_bitmap = byte_to_bitmap(cursor.getBlob(image_index));
            Image return_image = new Image();
            return_image.setId(image_id);
            return_image.setName(image_name);
            return_image.setImage_source(image_bitmap);
            db.close();
            return return_image;
        }
        return null;
    }

    public ArrayList<Image> getAllImages(){
        ArrayList<Image> imageList = new ArrayList<Image>();

        SQLiteDatabase db = this.getReadableDatabase();

        String QUERY = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(QUERY, null);

        if(cursor.getCount() != 0){
            int id_index = cursor.getColumnIndex(KEY_ID), name_index = cursor.getColumnIndex(KEY_NAME), image_index = cursor.getColumnIndex(KEY_IMAGE);
            cursor.moveToFirst();
            do{
                int image_id = cursor.getInt(id_index);
                String image_name = cursor.getString(name_index);
                Bitmap image_bitmap = byte_to_bitmap(cursor.getBlob(image_index));

                Image image = new Image();
                image.setId(image_id);
                image.setName(image_name);
                image.setImage_source(image_bitmap);
                imageList.add(image);
            }while(cursor.moveToNext());
        }
        return imageList;
    }

    public void deleteImage(Image image){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ? AND " + KEY_NAME + " = ?", new String[]{String.valueOf(image.getId()), image.getName()});
        db.close();
    }

    public byte[] bitMap_to_byte(Bitmap image_bitmap, int quality){
        byte[] image_bytes;
        ByteArrayOutputStream stream;
        stream = new ByteArrayOutputStream();
        image_bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        image_bytes = stream.toByteArray();
        return image_bytes;
    }

    public Bitmap byte_to_bitmap(byte[] image_bytes) {
        Bitmap image_bitmap = BitmapFactory.decodeByteArray(image_bytes, 0, image_bytes.length);
        return image_bitmap;
    }

    public ArrayList<Image> searchImage(String image_name_string){
        Log.d(DatabaseHelper.class.getSimpleName(), "searchImage");

        ArrayList<Image> imageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_NAME + " LIKE " + "'%" + image_name_string +"%'";

        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() != 0){
            Log.d(DatabaseHelper.class.getSimpleName(), "if(cursor.getCount() != 0)");
            int id_index = cursor.getColumnIndex(KEY_ID),
                    name_index = cursor.getColumnIndex(KEY_NAME),
                    image_index = cursor.getColumnIndex(KEY_IMAGE);
            cursor.moveToNext();
            do {
                String image_name = cursor.getString(name_index);
                if(compare(image_name, image_name_string))
                {
                    int image_id = cursor.getInt(id_index);
                    Bitmap image_bitmap = byte_to_bitmap(cursor.getBlob(image_index));
                    Image image = new Image();
                    image.setId(image_id);
                    image.setName(image_name);
                    image.setImage_source(image_bitmap);
                    imageList.add(image);
                }
            }while(cursor.moveToNext());
        }
        return imageList;
    }

    public boolean compare(String image_db_name, String image_search_name)
    {
        int length = image_search_name.length(), starting_point = 0;
        boolean first_found = false;

        for(int i = 0; i < image_search_name.length(); i++){
            for(int j = starting_point; j < image_db_name.length(); j++){
                if(image_search_name.charAt(i) == image_db_name.charAt(j)){
                    length--;
                    starting_point = j + 1;
                    if(length == 0){
                        return true;
                    }
                    break;
                }
                else if(first_found == true){
                    return false;
                }
            }
        }
        return false;
    }
}