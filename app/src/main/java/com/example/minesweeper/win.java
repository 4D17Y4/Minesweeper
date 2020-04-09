package com.example.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class win extends Activity {
    Button exit,ok;
    TextView score;
    int points;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);
        exit=findViewById(R.id.exit);
        ok=findViewById(R.id.ok);
        score=findViewById(R.id.score);
        final Intent info=getIntent();
        float time= info.getLongExtra("total_time",0);
        points=(int)(100000000.0/time);
        score.setText("SCORE:"+points);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height =dm.heightPixels;
        getWindow().setLayout((int)(width*0.7),(int)(height*0.5));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_Activity.fa.finish();
                Intent i= new Intent(win.this,Main2Activity.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                finish();

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                game_Activity.fa.finish();

            }
        });

        String scores=load();
        if(scores=="") {
            String new_scores=points+" 0 0 \n";
            save(new_scores);
        }
        else
        {
            String Scores;
            int s1= Integer.parseInt(scores.substring(0,scores.indexOf(" ")));
            scores=scores.substring(scores.indexOf(" ")+1);
            int s2= Integer.parseInt(scores.substring(0,scores.indexOf(" ")));
            scores=scores.substring(scores.indexOf(" ")+1);
            int s3=Integer.parseInt(scores.substring(0,scores.indexOf(" ")));

            if(points>s1)
                Scores=points+" "+s1+" "+s2;
            else if(points>s2)
                Scores=s1+" "+points+" "+s2;
            else if(points>s3)
                Scores=s1+" "+s2+" "+points;
            else
                Scores=s1+" "+s2+" "+s3;

            save(Scores+" 0 0 0\n");
        }

    }
    public String load()
    {
        FileInputStream fis =null;
        String text="";
        try {
            fis = openFileInput("high_scores2");
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
    public void save(String scores)
    {
        FileOutputStream fos=null;
        try{
            fos = openFileOutput("high_scores2",MODE_PRIVATE);
            fos.write(scores.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

