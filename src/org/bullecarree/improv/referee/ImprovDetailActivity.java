package org.bullecarree.improv.referee;

import org.bullecarree.improv.db.ImprovDbTable;
import org.bullecarree.improv.model.Improv;
import org.bullecarree.improv.model.ImprovType;
import org.bullecarree.improv.referee.contentprovider.ImprovContentProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ImprovDetailActivity extends Activity {
    
    private EditText mTitleText;

    private Uri improvUri;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        setContentView(R.layout.improv_edit);
        
        improvUri = bundle == null ? null : (Uri) bundle.getParcelable(ImprovContentProvider.CONTENT_TYPE);
        
        // Bundle extras = getIntent().getExtras() etc...
        
        mTitleText = (EditText) findViewById(R.id.improv_edit_title);
        Button btnSave = (Button) findViewById(R.id.btnImprovEditSave);
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                // TODO(pht) validate the content
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(ImprovContentProvider.CONTENT_ITEM_TYPE, improvUri);
    }
    
    private void saveState() {
        // TODO Auto-generated method stub
        
        String title = mTitleText.getText().toString();
        
        ContentValues values = new ContentValues();
        values.put(ImprovDbTable.COL_TITLE, title);
        values.put(ImprovDbTable.COL_TYPE, ImprovType.MIXT.toString());
        values.put(ImprovDbTable.COL_DURATION, 42*60);
        // TODO(pht) other stuff
        
        if (improvUri == null) {
            improvUri = getContentResolver().insert(ImprovContentProvider.CONTENT_URI, values);
        } else {
            // TODO
        }   
        
    }

}
