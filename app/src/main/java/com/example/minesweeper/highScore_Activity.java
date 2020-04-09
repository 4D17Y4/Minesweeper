package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class highScore_Activity extends AppCompatActivity {
    RelativeLayout high_score_screen;
    TextView first,second,third;
    public static final String file_name="high_scores2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        high_score_screen=findViewById(R.id.high_score_screen);
        first=findViewById(R.id.s1);
        second=findViewById(R.id.s2);
        third=findViewById(R.id.s3);
        high_score_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String scores=load();
        if(scores!="")
        {
           // Toast.makeText(this, ""+scores, Toast.LENGTH_SHORT).show();
          first.setText(""+scores.substring(0,scores.indexOf(" ")));
          scores=scores.substring(scores.indexOf(" ")+1);
            second.setText(""+scores.substring(0,scores.indexOf(" ")));
            scores=scores.substring(scores.indexOf(" ")+1);
            third.setText(""+scores.substring(0,scores.indexOf(" ")));
        }
    }
    public String load()
    {
        FileInputStream fis =null;
        String text="";
       try {
           fis = openFileInput(file_name);
           InputStreamReader isr =new InputStreamReader(fis);
           BufferedReader br=new BufferedReader(isr);
           StringBuilder sb=new StringBuilder();
           text+=br.readLine();
           return text;
       }
       catch (FileNotFoundException e)
       {
           e.printStackTrace();
       } catch (IOException e) {
           text="";
           e.printStackTrace();
       }
       return text;
    }
}
