package org.bullecarree.improv.referee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ImprovListActivity extends FragmentActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    
/** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.improv_list);
    }

    // Create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.list_menu_add_improv:
            addImprov();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /* LATER
        switch (item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                    .getMenuInfo();
            Uri uri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/"
                    + info.id);
            getContentResolver().delete(uri, null, null);
            fillData();
            return true;
        }
        return super.onContextItemSelected(item);
        */
        return false;
    }

    private void addImprov() {
        Intent i = new Intent(this, ImprovDetailActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    // Opens the second activity if an entry is clicked
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        /* LATER
//        super.onListItemClick(l, v, position, id);
//        Intent i = new Intent(this, TodoDetailActivity.class);
//        Uri todoUri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + id);
//        i.putExtra(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
//
//        // Activity returns an result if called with startActivityForResult
//        startActivityForResult(i, ACTIVITY_EDIT);
//        */
    //}

    // Called with the result of the other activity
    // requestCode was the origin request code send to the activity
    // resultCode is the return code, 0 is everything is ok
    // intend can be used to get data
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//            ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        // LATER menu.add(0, DELETE_ID, 0, R.string.menu_delete);
//    }


    
}
