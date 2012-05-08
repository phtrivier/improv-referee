package org.bullecarree.improv.referee;

import org.bullecarree.improv.db.ImprovDbTable;
import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovType;
import org.bullecarree.improv.referee.contentprovider.ImprovContentProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ImprovDetailActivity extends Activity {

    private EditText mTitleText;

    private Uri improvUri;

    private Spinner mTypeSpinner;

    private EditText mCategoryText;

    private EditText mPlayerText;

    private EditText mDurationText;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.improv_edit);

        improvUri = bundle == null ? null : (Uri) bundle
                .getParcelable(ImprovContentProvider.CONTENT_TYPE);

        mTypeSpinner = (Spinner) findViewById(R.id.improv_edit_type);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.improv_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);

        mTitleText = (EditText) findViewById(R.id.improv_edit_title);

        mCategoryText = (EditText) findViewById(R.id.improv_edit_category);
        mPlayerText = (EditText) findViewById(R.id.improv_edit_player_count);
        mDurationText = (EditText) findViewById(R.id.improv_edit_duration);

        Bundle extras = getIntent().getExtras();
        // Or passed from the other activity
        if (extras != null) {
            improvUri = extras
                    .getParcelable(ImprovContentProvider.CONTENT_ITEM_TYPE);
            fillData(improvUri);
        }

        Button btnSave = (Button) findViewById(R.id.btnImprovEditSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = getImprovTitle();
                Integer playerCount = getPlayerCount();
                Integer duration = getDuration();

                if (title == null || "".equals(title)) {
                    Toast.makeText(ImprovDetailActivity.this,
                            "Please give a title", Toast.LENGTH_LONG).show();
                } else if (playerCount == null || playerCount.intValue() <= 0) {
                    Toast.makeText(ImprovDetailActivity.this,
                            "Please provide a positive player count",
                            Toast.LENGTH_LONG).show();
                } else if (duration == null || duration.intValue() <= 0) {
                    Toast.makeText(ImprovDetailActivity.this,
                            "Please provide a positive duration in seconds",
                            Toast.LENGTH_LONG).show();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void fillData(Uri uri) {
        String[] projection = { ImprovDbTable.COL_TITLE,
                ImprovDbTable.COL_CATEGORY, ImprovDbTable.COL_TYPE,
                ImprovDbTable.COL_DURATION, ImprovDbTable.COL_PLAYER };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String type= cursor.getString(cursor
                    .getColumnIndexOrThrow(ImprovDbTable.COL_TYPE));
            for (int i = 0; i < mTypeSpinner.getCount(); i++) {
                String s = (String) mTypeSpinner.getItemAtPosition(i);
                if (s.equalsIgnoreCase(type)) {
                    mTypeSpinner.setSelection(i);
                }
            }

            mTitleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ImprovDbTable.COL_TITLE)));
            mDurationText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ImprovDbTable.COL_DURATION)));
            mPlayerText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ImprovDbTable.COL_PLAYER)));
            mCategoryText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ImprovDbTable.COL_CATEGORY)));

            // Always close the cursor
            cursor.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(ImprovContentProvider.CONTENT_ITEM_TYPE,
                improvUri);
    }

    private Integer getPlayerCount() {
        Integer playerCount = null;
        Editable editable = mPlayerText.getText();
        if (editable != null) {
            String playerCountStr = editable.toString().trim();
            try {
                playerCount = Integer.parseInt(playerCountStr);
            } catch (NumberFormatException e) {
                // TODO(pht) validate
            }
        }
        return playerCount;
    }

    private Integer getDuration() {
        Integer duration = null;

        Editable editable = mDurationText.getText();
        if (editable != null) {
            String durationStr = editable.toString().trim();
            try {
                duration = Integer.parseInt(durationStr);
            } catch (NumberFormatException e) {
                // TODO(pht) validate
            }
        }
        return duration;
    }

    private String getImprovTitle() {
        return mTitleText.getText().toString();
    }

    private void saveState() {

        String title = getImprovTitle();

        // FIXME(pht) probably a bad way to deal with an enum...
        String type = ImprovType.MIXT.toString();
        if ("Compared".equals(mTypeSpinner.getSelectedItem().toString())) {
            type = ImprovType.COMPARED.toString();
        }

        String category = mCategoryText.getText().toString();
        if (getPlayerCount() == null) {
            return;
        }

        if (getDuration() == null) {
            return;
        }

        if (title == null || title.equals("")) {
            return;
        }

        int playerCount = getPlayerCount().intValue();
        int duration = getDuration().intValue();

        ContentValues values = new ContentValues();
        values.put(ImprovDbTable.COL_TITLE, title);
        values.put(ImprovDbTable.COL_TYPE, type);
        values.put(ImprovDbTable.COL_CATEGORY, category);
        values.put(ImprovDbTable.COL_PLAYER, playerCount);
        values.put(ImprovDbTable.COL_DURATION, duration);

        if (improvUri == null) {
            improvUri = getContentResolver().insert(
                    ImprovContentProvider.CONTENT_URI, values);
        } else {
            getContentResolver().update(improvUri, values, null, null);
        }

    }

}
