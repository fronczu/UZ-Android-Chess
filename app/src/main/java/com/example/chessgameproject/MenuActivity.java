package com.example.chessgameproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;


public class MenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


    }
    public void changeViewToGame(View V){ //pobieranie imion i wrzucanie ich do bundle; przelaczanie sie do widoku gry

        TextView name1 = (TextView) findViewById(R.id.editTextTextPersonName2);
        TextView name2 = (TextView) findViewById(R.id.editTextTextPersonName3);
        String match = name1.getText() + " vs " + name2.getText();
        Bundle b = new Bundle();
        b.putString("Data",match);

        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        switchActivityIntent.putExtras(b);
        startActivity(switchActivityIntent);
    }
    public void changeViewToHightscore(View V){// przelaczanie sie do tablicy wynikow

        Intent switchActivityIntent = new Intent(this, ScoreBoardActivity.class);
        startActivity(switchActivityIntent);
    }
    public void changeViewToConnection(View V){// przelaczanie sie do listy dostepnych urzadzen bluetooth

        Intent switchActivityIntent = new Intent(this, ConnectionActivity.class);
        startActivity(switchActivityIntent);
    }
}