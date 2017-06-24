package singlepageapp.mohanty.dinesh.com.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shop.db";
    private static  final int DATABASE_VERSION = 1;


    public  ItemDbHelper(Context context){

        super(context ,DATABASE_NAME , null , DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {


        String SQL_CREATE_PETS_TABLE = "CREATE TABLE" + " " + ShopContract.itemEntry.TABLE_NAME + " " +"("
                + ShopContract.itemEntry._ID +" " + "INTEGER PRIMARY KEY AUTOINCREMENT"
                + "," +ShopContract.itemEntry.ITEM_NAME + " " + "TEXT NOT NULL"
                + "," + ShopContract.itemEntry.ITEM_PRICE + " " + "INTEGER NOT NULL"
                + "," + ShopContract.itemEntry.ITEM_AMOUNT + " " + "INTEGER NOT NULL"
                + ")"+ ";";

        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
