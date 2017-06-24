package singlepageapp.mohanty.dinesh.com.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import singlepageapp.mohanty.dinesh.com.inventoryapp.data.ShopContract;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText name_field;
    EditText price_field ;
    EditText amount_field ;
    Uri currentUri;
    private boolean mPetHasChanged = false;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        name_field = (EditText)findViewById(R.id.edit_item_name);
        price_field = (EditText)findViewById(R.id.edit_item_price);
        amount_field = (EditText)findViewById(R.id.edit_item_amount);
        button =(Button)findViewById(R.id.button_adder);

        Intent intent = getIntent();
        currentUri = intent.getData();
        if(currentUri != null)
        {
            setTitle("Edit");
            getSupportLoaderManager().initLoader(0 , null , this);
        }
        else{
            setTitle("Add a Item");
            button.setVisibility(Button.INVISIBLE);
        }
        name_field.setOnTouchListener(mTouchListener);
        price_field.setOnTouchListener(mTouchListener);
        amount_field.setOnTouchListener(mTouchListener);
        button.setOnTouchListener(mTouchListener);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = Integer.parseInt(amount_field.getText().toString());
                currentAmount++;
               amount_field.setText(""+currentAmount);
            }
        });
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:

                insertItem();

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if(mPetHasChanged == true)
                {
                    showDiscard();
                }
                else {
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {ShopContract.itemEntry.ITEM_NAME ,
        ShopContract.itemEntry.ITEM_AMOUNT , ShopContract.itemEntry.ITEM_PRICE} ;

        return new CursorLoader(this , currentUri , projection , null , null , null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst())
        {
            int nameColumnIndex = data.getColumnIndex(ShopContract.itemEntry.ITEM_NAME);
            int priceColumnIndex = data.getColumnIndex(ShopContract.itemEntry.ITEM_PRICE);
            int amountColumnIndex = data.getColumnIndex(ShopContract.itemEntry.ITEM_AMOUNT);

            String name = data.getString(nameColumnIndex);
            String price = data.getString(priceColumnIndex);
            String amount = data.getString(amountColumnIndex);

            name_field.setText(name);
            price_field.setText(price);
            amount_field.setText(amount);


        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void insertItem()
    {

        ContentValues values = new ContentValues();
        String names = name_field.getText().toString();
        String prices = price_field.getText().toString();
        String amounts = amount_field.getText().toString();

        if(TextUtils.isEmpty(names) || TextUtils.isEmpty(prices) ||TextUtils.isEmpty(amounts))
        {
            Toast.makeText(this ,"Fill all the items" , Toast.LENGTH_LONG).show();
            return;
        }
        else {
            values.put(ShopContract.itemEntry.ITEM_NAME, names);
            values.put(ShopContract.itemEntry.ITEM_AMOUNT, amounts);
            values.put(ShopContract.itemEntry.ITEM_PRICE, prices);


            if(currentUri == null)

            {
                getContentResolver().insert(ShopContract.itemEntry.CONTENT_URI, values);
                finish();
            }
            else {


                getContentResolver().update(currentUri, values , null , null);
                finish();

            }
        }

    }
    private void showDeleteDialog(){

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Deleting this Pet?");
        builder.setPositiveButton("Confirm" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getContentResolver().delete(currentUri , null , null);
                finish();

            }
        });

        builder.setNegativeButton("Cancel" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if(dialogInterface != null) {
                    dialogInterface.dismiss();
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private  void showDiscard()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dicard Changes");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed()
    {

        if(mPetHasChanged == true)
        {
            showDiscard();
        }
        else
        {
            NavUtils.navigateUpFromSameTask(DetailActivity.this);
        }
    }

}