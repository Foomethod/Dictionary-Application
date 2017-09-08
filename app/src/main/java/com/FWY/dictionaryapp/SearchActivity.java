package com.FWY.dictionaryapp;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by yumak on 17-05-2016.
 */
public class SearchActivity extends AppCompatActivity {

    String zeword;
    TextToSpeech convert2speech;
    Dictionary dic = Dictionary.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        zeword = intent.getStringExtra("something");

        if(dic.getDefinitionFromDatabase(zeword).isEmpty())
        {
            setContentView(R.layout.wordnotfound);
        }
        else {
            setContentView(R.layout.dictionary_main);TextView wordbox = (TextView) findViewById(R.id.wordbox);
            TextView definitionbox = (TextView) findViewById(R.id.definitionbox);
            wordbox.setText(zeword);
            definitionbox.setText(dic.getDefinitionFromDatabase(zeword));
        }

        super.onCreate(savedInstanceState);
    }

    public void ttsbuttonOnClick(View v){
        stopSpeech(convert2speech);
        final String theSentence = zeword + "." + ((TextView) findViewById(R.id.definitionbox)).getText().toString();
        convert2speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @TargetApi(21)
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    convert2speech.setLanguage(Locale.ENGLISH);
                    convert2speech.speak(theSentence, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meh, menu);
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    public void addbuttonOnClick(View v)
    {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            String daWord = data.getStringExtra("something");
            if(dic.getDefinitionFromDatabase(daWord) == null)
            {
                setContentView(R.layout.wordnotfound);
            }
            else {
                setContentView(R.layout.dictionary_main);TextView wordbox = (TextView) findViewById(R.id.wordbox);
                TextView definitionbox = (TextView) findViewById(R.id.definitionbox);
                wordbox.setText(daWord);
                definitionbox.setText(dic.getDefinitionFromDatabase(daWord));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void deleteButtonOnClick(View v)
    {
        stopSpeech(convert2speech);
        final String word = ((TextView) findViewById(R.id.wordbox)).getText().toString();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Confirmation").setMessage("Are you sure you want to delete the word '" + word + "'?");
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dic.Delete(word);
                finish();
            }
        });

        alert.show();
    }

    public void updateButtonOnClick(View v)
    {
        stopSpeech(convert2speech);
        String currentWord = ((TextView) findViewById(R.id.wordbox)).getText().toString();
        Intent intent = new Intent(this,UpdateActivity.class);
        intent.putExtra("something",currentWord);
        startActivityForResult(intent,1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopSpeech(convert2speech);
    }

    public void stopSpeech(TextToSpeech convert2speech)
    {
        if(convert2speech != null) {
            if (convert2speech.isSpeaking()) {
                convert2speech.shutdown();
            }
        }
    }
}
