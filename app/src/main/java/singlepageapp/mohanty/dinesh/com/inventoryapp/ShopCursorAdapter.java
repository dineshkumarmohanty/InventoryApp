package singlepageapp.mohanty.dinesh.com.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import singlepageapp.mohanty.dinesh.com.inventoryapp.data.ShopContract;

public class ShopCursorAdapter extends CursorAdapter {


    public ShopCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.item_amount);


        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ShopContract.itemEntry.ITEM_NAME);
        int breedColumnIndex = cursor.getColumnIndex(ShopContract.itemEntry.ITEM_AMOUNT);
        int priceIndex = cursor.getColumnIndex(ShopContract.itemEntry.ITEM_PRICE);



        // Read the pet attributes from the Cursor for the current pet
        final String petName = cursor.getString(nameColumnIndex);
       final  String petBreed = cursor.getString(breedColumnIndex);
        final String price = cursor.getString(priceIndex);

        int idIndex = cursor.getColumnIndex(ShopContract.itemEntry._ID);
       final int index = cursor.getInt(idIndex);



            // Update the TextViews with the attributes for the current pet
            nameTextView.setText(petName);
            summaryTextView.setText(petBreed);

            //add a button click
        Button button = (Button)view.findViewById(R.id.list_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view != null)
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ShopContract.itemEntry.ITEM_NAME , petName);
                    contentValues.put(ShopContract.itemEntry.ITEM_PRICE , price);
                    int amount = Integer.parseInt(petBreed);
                    amount++;
                    contentValues.put(ShopContract.itemEntry.ITEM_AMOUNT , amount);
                    context.getContentResolver().update(ContentUris.withAppendedId(ShopContract.itemEntry.CONTENT_URI, index)
                    , contentValues ,null, null);
                }
            }
        });




    }
}