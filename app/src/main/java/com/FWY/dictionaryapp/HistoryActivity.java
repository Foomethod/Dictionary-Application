package com.FWY.dictionaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Foooooo on 06/06/16.
 */
public class HistoryActivity extends AppCompatActivity{

    ListView historyList;
    ListAdapter adapter;
    ArrayList<String> arr;
    String clicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.history);
        historyList = (ListView) findViewById(R.id.historyList);
        arr = DictionaryActivity.getHistory();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        historyList.setAdapter(adapter);

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clicked = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("something",clicked);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        super.onCreate(savedInstanceState);
    }

    public void clearButtonOnClick(View v)
    {
        DictionaryActivity.clearHistory();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DictionaryActivity.getHistory());
        historyList.setAdapter(adapter);
    }

}
