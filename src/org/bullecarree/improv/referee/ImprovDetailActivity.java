package org.bullecarree.improv.referee;

import java.util.ArrayList;
import java.util.List;

import org.bullecarree.improv.db.ImprovDbTable;
import org.bullecarree.improv.model.ImprovType;
import org.bullecarree.improv.referee.contentprovider.ImprovContentProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

    private ArrayAdapter mTypeAdapter;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.improv_edit);

        mTypeSpinner = (Spinner) findViewById(R.id.improv_edit_type);
        mTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.improv_type, android.R.layout.simple_spinner_item);
        mTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(mTypeAdapter);

        mTitleText = (EditText) findViewById(R.id.improv_edit_title);

        mCategoryText = (EditText) findViewById(R.id.improv_edit_category);
        mPlayerText = (EditText) findViewById(R.id.improv_edit_player_count);
        mDurationText = (EditText) findViewById(R.id.improv_edit_duration);

        Bundle extras = getIntent().getExtras();
        // Or passed from the other activity
        
        boolean isEdit = false;
        
        if (extras == null) {
            improvUri = null;
            isEdit = false;
        } else {
            improvUri = extras
                    .getParcelable(ImprovContentProvider.CONTENT_ITEM_TYPE);
            fillData(improvUri);
            isEdit = true;
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
                            R.string.improv_edit_error_no_title
                            , Toast.LENGTH_LONG).show();
                } else if (playerCount.intValue() < 0) {
                    Toast.makeText(ImprovDetailActivity.this,
                            R.string.improv_edit_error_negative_player_count,
                            Toast.LENGTH_LONG).show();
                } else if (duration == null || duration.intValue() <= 0) {
                    Toast.makeText(ImprovDetailActivity.this,
                            R.string.improv_edit_error_negative_duration,
                            Toast.LENGTH_LONG).show();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        Button btnDelete = (Button) findViewById(R.id.btnImprovDelete);
        btnDelete.setEnabled(isEdit);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ImprovDetailActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.improv_edit_confirm_delete_title)
                .setMessage(R.string.improv_edit_confirm_delete_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentImprov();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
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
            String type = cursor.getString(cursor
                    .getColumnIndexOrThrow(ImprovDbTable.COL_TYPE));
            if (type == ImprovType.MIXT.toString()) {
                mTypeSpinner.setSelection(0);
            } else {
                mTypeSpinner.setSelection(1);
            }
//            
//            
//            for (int i = 0; i < mTypeSpinner.getCount(); i++) {
//                String s = (String) mTypeSpinner.getItemAtPosition(i);
//                if (s.equalsIgnoreCase(type)) {
//                    mTypeSpinner.setSelection(i);
//                }
//            }

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
        Integer playerCount = 0;
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

    private void deleteCurrentImprov() {
        getContentResolver().delete(improvUri, null, null);
        setResult(RESULT_OK);
        finish();
    }
    
    private void saveState() {

        String title = getImprovTitle();

        String type = ImprovType.MIXT.toString();
        
        String spinnerString = mTypeSpinner.getSelectedItem().toString();
        String comparedText =getString(R.string.typeCompared); 
        
        if (comparedText.equals(spinnerString)) {
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
