package com.example.chessgameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ScoreBoardActivity extends AppCompatActivity {

    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_view);
        ReadDatabase();
    }


    public void ReadDatabase(){
        TextView scoreboard = (TextView) findViewById(R.id.textView3);
        database = openOrCreateDatabase("Highscore", MODE_PRIVATE, null );
        Cursor resultSet= database.rawQuery( "Select * from Users order by Time DESC limit 10", null); //pobieramy 10 najlepszych graczy
        resultSet.moveToFirst();
        StringBuilder builder= new StringBuilder();
        while(resultSet.moveToNext()){
            builder.append(resultSet.getString( 0));
            builder.append("   ");
            builder.append(resultSet.getString( 1));
            builder.append("\n");
        }
        scoreboard.setText(builder.toString());
        resultSet.close();
    }

    public void changeViewToGame(View V){

        Intent switchActivityIntent = new Intent(this, MenuActivity.class);
        startActivity(switchActivityIntent);
    }

}