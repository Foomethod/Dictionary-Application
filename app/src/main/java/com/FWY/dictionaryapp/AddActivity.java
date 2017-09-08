package com.FWY.dictionaryapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Foooooo on 03/06/16.
 */
public class AddActivity extends AppCompatActivity {

    EditText wordtext;
    EditText definitiontext;
    AlertDialog.Builder alert;
    Dictionary dic = Dictionary.getInstance();

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.addword);
        wordtext = (EditText)findViewById(R.id.wordtext);
        definitiontext = (EditText)findViewById(R.id.definitiontext);
        super.onCreate(savedInstanceState);
    }

    public void completeAddonClick(View v)
    {
        String newWord = wordtext.getText().toString();
        String newDefinition = definitiontext.getText().toString();

        if(newWord.trim().isEmpty() || newDefinition.trim().isEmpty()) {
            emptyRequiredFieldAlert();
            if(newWord.trim().isEmpty()) {
                wordtext.setError("Word field cannot be left emptied!");
            }
            if(newDefinition.trim().isEmpty()) {
                definitiontext.setError("Definition field cannot be left emptied!");
            }
        }
        else
        {
            if (newWord.length() == 1) {
                newWord = newWord.substring(0).toUpperCase();
            } else {
                String searchableWord = newWord.substring(1).toLowerCase();
                newWord = newWord.substring(0, 1).toUpperCase() + searchableWord;
            }

            boolean flag = dic.addNewWord(new Word(newWord,newDefinition));

            if (!flag) {
                alert = new AlertDialog.Builder(this);
                alert.setTitle("Duplication of words!");
                alert.setMessage(newWord + " Exist!");
                alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        wordtext.setText("");
                        definitiontext.setText("");
                        wordtext.requestFocus();
                        InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        input.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
                alert.show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("something", newWord);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void emptyRequiredFieldAlert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Null Word or Definition Detected!").setMessage("Word and/or Definition cannot be left null!").setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
}
