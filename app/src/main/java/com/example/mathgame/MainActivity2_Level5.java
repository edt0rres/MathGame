package com.example.mathgame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2_Level5 extends AppCompatActivity {

        private TextView tv_name, tv_score;
        private ImageView iv_one, iv_two, iv_lives, ImageView_sign;
        private EditText et_answer;
        private MediaPlayer mp, mp_great, mp_bad;

        int score, numRandom1, numRandom2, result, lives = 3;
        String name_player, string_score, string_lives;

        //array for images
        String number[] = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_activity2_level4);


            Toast.makeText(this, "Level 5 - Multiplication", Toast.LENGTH_SHORT).show();

            tv_name = (TextView) findViewById(R.id.tv_name);
            tv_score = (TextView) findViewById(R.id.tv_score);
            iv_one = (ImageView) findViewById(R.id.imageView_numberone);
            iv_two = (ImageView) findViewById(R.id.imageView_numbertwo);
            iv_lives = (ImageView) findViewById(R.id.imageView_lives);
            ImageView_sign = (ImageView) findViewById(R.id.imageView_sign);
            et_answer = (EditText) findViewById(R.id.editText_answer);


            name_player = getIntent().getStringExtra("player");
            tv_name.setText("Player: " + name_player);

            string_score = getIntent().getStringExtra("score");
            score = Integer.parseInt(string_score);
            tv_score.setText("Score: " + score);

            string_lives = getIntent().getStringExtra("lives");
            lives = Integer.parseInt(string_lives);
            if (lives == 3) {
                iv_lives.setImageResource(R.drawable.threehearts);
            }
            if (lives == 2) {
                iv_lives.setImageResource(R.drawable.twohearts);
            }
            if (lives == 1) {
                iv_lives.setImageResource(R.drawable.oneheart);
            }

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            mp = MediaPlayer.create(this, R.raw.levelsounds);
            mp.start();
            mp.setLooping(true);

            mp_great = MediaPlayer.create(this, R.raw.great);
            mp_bad = MediaPlayer.create(this, R.raw.bad);

            randomNum();

        }

        public void Compare(View view) {
            String answer = et_answer.getText().toString();
            if (!answer.equals("")) {
                int answer_player = Integer.parseInt(answer);
                if (result == answer_player) {
                    mp_great.start();
                    score++;
                    tv_score.setText("Score " + score);
                    et_answer.setText("");
                    DataBase();
                } else {
                    mp_bad.start();
                    lives--;
                    DataBase();

                    switch (lives) {
                        case 3:
                            iv_lives.setImageResource(R.drawable.threehearts);
                            break;
                        case 2:
                            Toast.makeText(this, "Two hearts remaining", Toast.LENGTH_SHORT).show();
                            iv_lives.setImageResource(R.drawable.twohearts);
                            break;
                        case 1:
                            Toast.makeText(this, "One heart left", Toast.LENGTH_SHORT).show();
                            iv_lives.setImageResource(R.drawable.oneheart);
                            break;
                        case 0:
                            Toast.makeText(this, "You have wasted all your hearts", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            mp.stop();
                            mp.release();
                            break;
                    }
                }
                et_answer.setText("");
                randomNum();
            } else {
                Toast.makeText(this, "Type your answer", Toast.LENGTH_LONG).show();
            }
        }

        public void randomNum() {
            if (score <= 39) {

                numRandom1 = (int) (Math.random() * 10);
                numRandom2 = (int) (Math.random() * 10);

                result = numRandom1 - numRandom2;

                if (numRandom1 >= 0 && numRandom1 <= 4) {
                    result = numRandom1 + numRandom2;
                    ImageView_sign.setImageResource(R.drawable.plussign);
                } else {
                    result = numRandom1 - numRandom2;
                    ImageView_sign.setImageResource(R.drawable.minussign);

                }

                if (result >= 0) {
                    for (int i = 0; i < number.length; i++) {
                        int id = getResources().getIdentifier(number[i], "drawable", getPackageName());
                        if (numRandom1 == i) {
                            iv_one.setImageResource(id);
                        }
                        if (numRandom2 == i) {
                            iv_two.setImageResource(id);
                        }
                    }
                } else {
                    randomNum();
                }


            } else {
                Intent intent = new Intent(this, MainActivity2_Level5.class);

                string_score = String.valueOf(score);
                string_lives = String.valueOf(lives);
                intent.putExtra("player", name_player);
                intent.putExtra("score", string_score);
                intent.putExtra("lives", string_lives);

                startActivity(intent);
                finish();
                mp.stop();
                mp.release();
            }
        }

        public void DataBase() {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "DB", null, 1);
            SQLiteDatabase DB = admin.getWritableDatabase();
            Cursor query = DB.rawQuery("select * from tracking where score = (select max(score) from tracking)", null);
            if (query.moveToFirst()) {
                String temp_name = query.getString(0);
                String temp_score = query.getString(1);

                int bestScore = Integer.parseInt(temp_score);

                if (score > bestScore) {
                    ContentValues mod = new ContentValues();
                    mod.put("name", name_player);
                    mod.put("score", score);
                    DB.update("tracking", mod, "score= " + bestScore, null);
                }
            } else {
                ContentValues insert = new ContentValues();
                insert.put("name", name_player);
                insert.put("score", score);

                DB.insert("tracking", null, insert);
                DB.close();
            }
        }

        @Override
        public void onBackPressed() {

        }
}