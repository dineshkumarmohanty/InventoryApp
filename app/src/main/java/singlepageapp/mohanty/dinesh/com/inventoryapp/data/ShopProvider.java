package singlepageapp.mohanty.dinesh.com.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class ShopProvider extends ContentProvider {

    ItemDbHelper itemDbHelper;

    public static final int ITEMS= 100;
    public static final int ITEMS_ID = 101;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(ShopContract.itemEntry.CONTENT_AUTHORITY , ShopContract.itemEntry.PATH_ITEMS ,ITEMS);
        uriMatcher.addURI(ShopContract.itemEntry.CONTENT_AUTHORITY , ShopContract.itemEntry.PATH_ITEMS + "/#", ITEMS_ID);
    }


    @Override
    public boolean onCreate() {

        itemDbHelper = new ItemDbHelper(getContext());
        return true;

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int match = uriMatcher.match(uri);
        Cursor cursor;
        SQLiteDatabase database = itemDbHelper.getReadableDatabase();

        switch (match)
        {
            case ITEMS:
                 cursor = database.query(ShopContract.itemEntry.TABLE_NAME ,
                         projection,
                         selection ,
                         selectionArgs ,
                         null ,
                         null ,
                         sortOrder);

                break;
            case ITEMS_ID:

                selection = ShopContract.itemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ShopContract.itemEntry.TABLE_NAME ,
                        projection,
                        selection ,
                        selectionArgs ,
                        null ,
                        null ,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver() , uri);
 return cursor;


    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return ShopContract.itemEntry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return ShopContract.itemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        int id;
        SQLiteDatabase database = itemDbHelper.getWritableDatabase();
        switch (match)
        {
            case ITEMS:
                 id = database.delete(ShopContract.itemEntry.TABLE_NAME , selection , selectionArgs);
                if (id != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            case ITEMS_ID:
                selection = ShopContract.itemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                id = database.delete(ShopContract.itemEntry.TABLE_NAME , selection , selectionArgs);
                if (id != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        return  id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, values, selection, selectionArgs);
            case ITEMS_ID:

                selection = ShopContract.itemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private Uri insertItem(Uri uri , ContentValues values)
    {


     SQLiteDatabase database = itemDbHelper.getWritableDatabase();
        long id = database.insert(ShopContract.itemEntry.TABLE_NAME,null , values);

        if (id != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ContentUris.withAppendedId(uri  , id);





    }
    private int updateItem(Uri uri , ContentValues values , String selection  , String[] selectionArgs)
    {
        SQLiteDatabase database =itemDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(ShopContract.itemEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
