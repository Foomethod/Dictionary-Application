package com.FWY.dictionaryapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Toolbar tb;
    Context context;
    SearchView searchView;
    TextToSpeech convert2speech;
    Dictionary dic;
    TextToSpeech.OnInitListener ttsinit;
    boolean startFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        DictionaryActivity DicActivity = new DictionaryActivity(this);
        DicActivity.createDatabase();
        DicActivity.openDatabase();
        dic = Dictionary.getInstance();
        context = this;

        ttsinit = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status){
                if(status != TextToSpeech.ERROR)
                {
                    convert2speech.setLanguage(Locale.ENGLISH);
                    convert2speech.speak(getSentence(), TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        };
        convert2speech = new TextToSpeech(getApplicationContext(), ttsinit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meh, menu);
        inflater.inflate(R.menu.options_menu, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchbar));
        searchView.setQueryHint(getString(R.string.hint));

        final ListView a = (ListView) findViewById(R.id.listView2);

        a.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String)parent.getItemAtPosition(position);
                searchView.setQuery(selected,false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String searchText) {

                Intent intent = new Intent(context, SearchActivity.class);

                intent.putExtra("something",searchText = searchText.substring(0,1).toUpperCase() + searchText.substring(1).toLowerCase());
                startActivity(intent);
                if(!DictionaryActivity.repetitionHistory(searchText))
                {
                    DictionaryActivity.addtoHistory(searchText);
                }
                stopSpeech(convert2speech);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {

                stopSpeech(convert2speech);
                if(!searchText.equals("")){
                    if(searchText.length() == 1)
                    {
                        searchText = searchText.substring(0).toUpperCase();
                    }
                    else if (searchText.length() > 1)
                    {
                        String searchableword = searchText.substring(1).toLowerCase();
                        searchText = searchText.substring(0, 1).toUpperCase() + searchableword;
                    }
                    a.setVisibility(View.VISIBLE);
                    ListAdapter ardap = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dic.findFromDatabase(searchText));
                    a.setAdapter(ardap);
                }
                else{
                    a.setVisibility(View.GONE);
                }
                return false;
            }
        });
        TextView wordoftheday = (TextView) findViewById(R.id.wordoftheday);
        TextView definitionbox = (TextView) findViewById(R.id.meaningoftheday);

        Word wotd = dic.getWordFromDatabase();
        wordoftheday.setText(wotd.getWord());
        definitionbox.setText(wotd.getDefinition());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.about:
                Intent intent = new Intent(context,LayoutActivity.class);
                startActivity(intent);
                break;
            case R.id.rateus:
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store?hl=en"));
                startActivity(intent2);
                break;
            case R.id.history:
                Intent intent3 = new Intent(context,HistoryActivity.class);
                startActivityForResult(intent3,10);
                break;
        }
        stopSpeech(convert2speech);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK)
        {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("something",data.getStringExtra("something"));
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ttsbuttonOnClickmain(View v){
        if(convert2speech != null){
            if(convert2speech.isSpeaking()){
                convert2speech.stop();
            }
        }
        convert2speech = new TextToSpeech(getApplicationContext(), ttsinit);
    }

    public String getSentence(){
        String speakSentence;
        if(!startFlag){
            speakSentence = ((TextView) findViewById(R.id.wordoftheday)).getText().toString() + "." + ((TextView) findViewById(R.id.meaningoftheday)).getText().toString();
        }
        else{
            startFlag = false;
            speakSentence = "";
        }
        return speakSentence;
    }

    @Override
    protected void onDestroy() {
        stopSpeech(convert2speech);
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        MainActivity.super.onBackPressed();
        stopSpeech(convert2speech);
    }

    public void stopSpeech(TextToSpeech convert2speech)
    {
        if(convert2speech != null) {
            if (convert2speech.isSpeaking()) {
                convert2speech.stop();
            }
        }
    }
}
