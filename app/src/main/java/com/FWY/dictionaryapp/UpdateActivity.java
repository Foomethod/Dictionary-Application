package com.FWY.dictionaryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Created by Foooooo on 04/06/16.
 */
public class UpdateActivity extends AppCompatActivity{

    TextView displayWord;
    EditText editDefinition;
    Word word;
    Dictionary dic = Dictionary.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String currentWord = intent.getStringExtra("something");
        setContentView(R.layout.updateword);
        displayWord = (TextView) findViewById(R.id.displayWord);
        editDefinition = (EditText) findViewById(R.id.editDefinition);
        displayWord.setText(currentWord);
        editDefinition.setText(dic.getDefinitionFromDatabase(currentWord));
        editDefinition.selectAll();
        super.onCreate(savedInstanceState);
    }

    public void confirmupdateButtonOnClick(View v)
    {
        String targetWord = displayWord.getText().toString();
        String newDefinition = editDefinition.getText().toString();

        if(newDefinition.trim().isEmpty())
        {
            emptyDefinitionAlert();
            editDefinition.setError("Definition field cannot be left emptied!");
        }
        else {
            word = new Word(targetWord,newDefinition);
            dic.Update(word);
            Intent intent = new Intent();
            intent.putExtra("something", targetWord);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void emptyDefinitionAlert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Null Definition Detected!").setMessage("Definition cannot be left null!").setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editDefinition.selectAll();
            }
        });
        alert.show();
    }
}
