package com.epam.AnotherSearch;

import org.w3c.dom.ls.LSInput;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

public class AnotherSearchActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    ListView listView = (ListView)findViewById(R.id.lvSearchResults);
    listView.setAdapter(new CategorizedAdapter());
    }
}