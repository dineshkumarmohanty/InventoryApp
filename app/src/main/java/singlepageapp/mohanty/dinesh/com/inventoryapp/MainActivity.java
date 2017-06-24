package singlepageapp.mohanty.dinesh.com.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import singlepageapp.mohanty.dinesh.com.inventoryapp.data.ItemDbHelper;
import singlepageapp.mohanty.dinesh.com.inventoryapp.data.ShopContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

ShopCursorAdapter shopCursorAdapter;
    ItemDbHelper itemDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemDbHelper = new ItemDbHelper(this);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , DetailActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView)findViewById(R.id.list);
        shopCursorAdapter = new ShopCursorAdapter(this , null);
        listView.setAdapter(shopCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this , DetailActivity.class);
                Uri uri = ContentUris.withAppendedId(ShopContract.itemEntry.CONTENT_URI , id);
                intent.setData(uri);
                startActivity(intent);
                view.setTag(position);



            }
        });

        View emptyView = findViewById(R.id.empty_view) ;
        listView.setEmptyView(emptyView);
        getSupportLoaderManager().initLoader(0, null, this );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_delete:

              deleteDilog();

                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] Projection = {ShopContract.itemEntry._ID ,
                ShopContract.itemEntry.ITEM_NAME,
                ShopContract.itemEntry.ITEM_AMOUNT , ShopContract.itemEntry.ITEM_PRICE};

        return new CursorLoader(this ,
                ShopContract.itemEntry.CONTENT_URI ,
                Projection ,
                null ,
                null ,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {

        shopCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {

        shopCursorAdapter.swapCursor(null);

    }

    private void deleteDilog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Item ?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContentResolver().delete(ShopContract.itemEntry.CONTENT_URI , null ,null);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
