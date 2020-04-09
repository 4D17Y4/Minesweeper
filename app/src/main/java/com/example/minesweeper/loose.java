package com.example.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class loose extends Activity {
    Button exit,ok;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loose);
        exit=findViewById(R.id.exit);
        ok=findViewById(R.id.ok);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setElevation(10.0f);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height =dm.heightPixels;
        getWindow().setLayout((int)(width*0.7),(int)(height*0.5));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_Activity.fa.finish();
                Intent i= new Intent(loose.this,Main2Activity.class);
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

    }
}
