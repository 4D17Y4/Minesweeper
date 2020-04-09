package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class game_Activity extends AppCompatActivity {
    int height = 0, width = 0, tile_size = 120, no_of_mines = 15, no_of_minesPlanted = 0, no_of_tiles_tapped = 0, cons = 0, position1 = -1;

    long startTime = 0, millis = 0;

    Button info;    LinearLayout LL;    GridView game;  TextView timerTextView,Flags;

    public static Activity fa;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        startInfo();

            LL = findViewById(R.id.LL);
            game = findViewById(R.id.game);
            info = findViewById(R.id.i);
            timerTextView = findViewById(R.id.timer_text);
            Flags =findViewById(R.id.flags);
        Flags.setText(""+no_of_mines);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInfo();
            }
        });
        game.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cons == 0) {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    cons = 1;
                }
                return false;
            }
        });
        game.post(new Runnable() {
            public void run() {
                height = game.getHeight();
                width = game.getWidth();
                game.setNumColumns((int) width / tile_size);
                Initial initial = new Initial();
                game.setAdapter(initial);
                final int no_columns = (width / tile_size);
                final int no_rows = (height / tile_size - 1);
                final place_mines mines = new place_mines(no_rows, no_columns, no_of_mines);

                game.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int i = position / no_columns;
                        int j = position % no_columns;
                        ImageView img = (ImageView) game.getChildAt(position);


                        int tile = mines.a[i][j];

                        if (tile == -1) {
                            timerHandler.removeCallbacks(timerRunnable);
                            Intent win = new Intent(game_Activity.this, loose.class);
                            //win.putExtra("total_time",millis);
                            startActivity(win);
                            // Toast.makeText(game_Activity.this, "you lose"+millis, Toast.LENGTH_SHORT).show();
                        } else if (tile == 0) {
                            //to remove all connected with 0
                            int x = i, y = j;

                            removeAdjacent(x, y, mines, no_columns, no_rows);
                        } else {
                            no_of_tiles_tapped++;
                            change(img, tile);


                        }

                        if (no_of_tiles_tapped == no_columns * no_rows - no_of_mines) {
                            timerHandler.removeCallbacks(timerRunnable);
                            Intent win = new Intent(game_Activity.this, win.class);
                            win.putExtra("total_time", millis);
                            startActivity(win);
                        }


                    }
                });

                game.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        position1 = position;
                        ImageView img = (ImageView) game.getChildAt(position1);
                        if (img.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState())&&no_of_minesPlanted<no_of_mines) {
                            img.setImageResource(R.drawable.flag);
                            no_of_minesPlanted++;
                            Flags.setText(""+(no_of_mines-no_of_minesPlanted));
                        }
                        else if(img.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.flag).getConstantState())&&no_of_minesPlanted<no_of_mines)
                        {
                            img.setImageResource(R.color.tile_color);
                            no_of_minesPlanted--;
                            Flags.setText(""+(no_of_mines-no_of_minesPlanted));
                        }
                        else if(no_of_minesPlanted>=no_of_mines)
                        {
                            Toast.makeText(game_Activity.this, "There are only "+no_of_mines+" mines.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

            }
        });


        fa = this;
    }

    private void change(ImageView img, int tile) {
        switch (tile) {
            case 1: {
                img.setImageResource(R.drawable.one);
                break;
            }
            case 2: {
                img.setImageResource(R.drawable.two);
                break;
            }
            case 3: {
                img.setImageResource(R.drawable.three);
                break;
            }
            case 4: {
                img.setImageResource(R.drawable.four);
                break;
            }
            case 5: {
                img.setImageResource(R.drawable.five);
                break;
            }
            case 6: {
                img.setImageResource(R.drawable.six);
                break;
            }
            case 7: {
                img.setImageResource(R.drawable.seven);
                break;
            }
            case 8: {
                img.setImageResource(R.drawable.eight);
                break;
            }
            default:
                img.setImageResource(R.drawable.zero);
                break;

        }
    }

    private void removeAdjacent(int x, int y, place_mines mines, int no_columns, int no_rows) {
        if (mines.a[x][y] == 0) {
            ImageView temp1 = (ImageView) game.getChildAt(x * no_columns + y);
            if (temp1.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState())) {
                no_of_tiles_tapped++;
                temp1.setImageResource(R.drawable.zero);
            }

            if (x > 0 && x < no_rows - 1) {
                ImageView tempUP = (ImageView) game.getChildAt((x - 1) * no_columns + y);
                if (tempUP.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState()))
                    removeAdjacent(x - 1, y, mines, no_columns, no_rows);
                ImageView tempDOWN = (ImageView) game.getChildAt((x + 1) * no_columns + y);
                if (tempDOWN.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState()))
                    removeAdjacent(x + 1, y, mines, no_columns, no_rows);
            } else if (x != 0) {
                ImageView tempUP = (ImageView) game.getChildAt((x - 1) * no_columns + y);
                if (tempUP.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState()))
                    removeAdjacent(x - 1, y, mines, no_columns, no_rows);
            } else if (x == 0) {
                ImageView tempDOWN = (ImageView) game.getChildAt((x + 1) * no_columns + y);
                if (tempDOWN.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState()))
                    removeAdjacent(x + 1, y, mines, no_columns, no_rows);
            }
            if (y > 0 && y < no_columns - 1) {
                ImageView tempLEFT = (ImageView) game.getChildAt(x * no_columns + y - 1);
                if (tempLEFT.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState()))
                    removeAdjacent(x, y - 1, mines, no_columns, no_rows);
                ImageView tempRIGHT = (ImageView) game.getChildAt(x * no_columns + y + 1);
                if (tempRIGHT.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState()))
                    removeAdjacent(x, y + 1, mines, no_columns, no_rows);
            } else if (y != 0) {
                ImageView tempLEFT = (ImageView) game.getChildAt(x * no_columns + y - 1);
                if (tempLEFT.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState()))
                    removeAdjacent(x, y - 1, mines, no_columns, no_rows);
            } else if (y == 0) {
                ImageView tempRIGHT = (ImageView) game.getChildAt((x) * no_columns + y + 1);
                if (tempRIGHT.getDrawable().getConstantState().equals(getResources().getDrawable(R.color.tile_color).getConstantState()))
                    removeAdjacent(x, y + 1, mines, no_columns, no_rows);
            }

        } else if (mines.a[x][y] > 0) {
            int tile = mines.a[x][y];
            ImageView img = (ImageView) game.getChildAt(x * no_columns + y);
            change(img, tile);
            no_of_tiles_tapped++;
            return;
        }
        return;
    }


    void startInfo() {
        Intent i = new Intent(game_Activity.this, info.class);
        startActivity(i);
    }

    class Initial extends BaseAdapter {


        @Override
        public int getCount() {
            return (width / tile_size) * ((int) height / tile_size - 1);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(game_Activity.this);
            imageView.setLayoutParams(new GridView.LayoutParams(tile_size, tile_size));
            int i = R.color.tile_color;
            imageView.setImageResource(R.color.tile_color);
            return imageView;
        }
    }

    static class place_mines {
        int[][] a;

        place_mines(int i, int j, int num) {
            a = new int[i][j];
            for (int x = 0; x < i; x++) {
                for (int y = 0; y < j; y++) {
                    a[x][y] = 0;
                }
            }
            for (int k = 0; k < num; ) {
                int temp1 = (int) (Math.random() * 100) % i;
                int temp2 = (int) (Math.random() * 100) % j;
                if (a[temp1][temp2] != -1) {
                    a[temp1][temp2] = -1;
                    k++;
                }
            }
            for (int x = 0; x < i; x++) {
                for (int y = 0; y < j; y++) {
                    if (a[x][y] == 0) {
                        int p = x - 1, l = x + 1, h = y + 1;

                        for (int u = 0; u <= 2; p++, u++) {
                            if (p < 0 || p >= i)
                                continue;
                            int q = y - 1;
                            for (int r = 0; r <= 2; r++, q++) {
                                if (q < 0 || q >= j)
                                    continue;
                                if (a[p][q] == -1)
                                    a[x][y]++;
                            }
                        }
                    }
                }
            }
        }
    }
}
