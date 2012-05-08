package org.bullecarree.improv.referee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ImprovListActivity extends FragmentActivity {

    public static final int ACTIVITY_CREATE = 0;
    public static final int ACTIVITY_EDIT = 1;
    public static final int ACTIVITY_REFEREE = 0;

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
        case R.id.list_menu_play_game:
            playGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addImprov() {
        Intent i = new Intent(this, ImprovDetailActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void playGame() {
        Intent i = new Intent(this, ImprovRefereeActivity.class);
        startActivityForResult(i, ACTIVITY_REFEREE);
    }

    
    // Called with the result of the other activity
    // requestCode was the origin request code send to the activity
    // resultCode is the return code, 0 is everything is ok
    // intend can be used to get data
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }
    
}
