package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //declaring variables
    private EditText et_name;
    private ImageView iv_characters;
    private TextView tv_bestScore;
    private MediaPlayer mp;

    //I had to cast the variable in order to make it integer.
    int num_random = (int) (Math.random() * 10);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables are linked to design objects

        et_name = (EditText) findViewById(R.id.txt_name);
        iv_characters = (ImageView) findViewById(R.id.imageView_Character);
        tv_bestScore = (TextView) findViewById(R.id.textView_bestScore);

        //setting the app icon to true so it can show in the app library
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // int id will hold the image routes. images will change due to randomization.
        int id;
        if (num_random == 0 || num_random == 10) {
            id = getResources().getIdentifier("zebra", "drawable", getPackageName());
            iv_characters.setImageResource(id);
        } else if (num_random == 1 || num_random == 9) {
            id = getResources().getIdentifier("giraffe", "drawable", getPackageName());
            iv_characters.setImageResource(id);
        } else if (num_random == 2 || num_random == 8) {
            id = getResources().getIdentifier("peacock", "drawable", getPackageName());
            iv_characters.setImageResource(id);
        } else if (num_random == 3 || num_random == 7) {
            id = getResources().getIdentifier("turtle", "drawable", getPackageName());
            iv_characters.setImageResource(id);
        } else if (num_random == 4 || num_random == 5 || num_random == 6) {
            id = getResources().getIdentifier("cocodrile", "drawable", getPackageName());
            iv_characters.setImageResource(id);
        }

        //Database Area = getting data from table created.
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db", null,1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor query = db.rawQuery(
                "select * from tracking where score = (select max(score) from tracking)", null);

        if(query.moveToFirst()){
            String temp_name = query.getString(0);
            String temp_score = query.getString(1);
            tv_bestScore.setText(temp_name + " scored: " + temp_score);
        }
        db.close();

        mp = MediaPlayer.create(this, R.raw.levelsounds);
        mp.start();
        mp.setLooping(true);
    }

    public void Play(View view){
        String name = et_name.getText().toString();

        if(!name.equals("")){
            mp.stop();
            mp.release();
            Intent intent = new Intent(this, MainActivity2_Level1.class);
            intent.putExtra("player", name);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Name Is Required.", Toast.LENGTH_SHORT).show();
            et_name.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_name, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onBackPressed(){

    }
}