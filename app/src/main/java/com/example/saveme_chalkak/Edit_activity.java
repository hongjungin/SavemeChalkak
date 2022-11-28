package com.example.saveme_chalkak;

import static com.example.saveme_chalkak.R.id.nameEdit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Edit_activity extends AppCompatActivity {

    private EditText name;
    private EditText age;
    private EditText record;
    private TextView avg_tv;
    private TextView sensitivity_tv;
    String sharedname = "file1";
    String shareage = "file2";
    String sharerecord = "file3";
    Button saveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        saveBtn = findViewById(R.id.saveBtn);
        name = (EditText) findViewById(R.id.nameEdit);
        age = (EditText) findViewById(R.id.ageEdit);
        record = (EditText) findViewById(R.id.text);
        avg_tv = (TextView) findViewById(R.id.textView6);
        sensitivity_tv = (TextView) findViewById(R.id.textView7);

        SharedPreferences sp_name = getSharedPreferences(sharedname, 0);
        String value1 = sp_name.getString("hong", "");
        name.setText(value1);

        SharedPreferences sp_age = getSharedPreferences(shareage, 0);
        String value2 = sp_age.getString("age", "");
        age.setText(value2);

        SharedPreferences sp_record = getSharedPreferences(sharerecord, 0);
        String value3 = sp_record.getString("record", "");
        record.setText(value3);

        //평균 심박수 받아오기
        Intent avgIntent = getIntent();
        float avg = avgIntent.getFloatExtra("hr_avg", 0);

        //민감도 받아오기
        Intent sensitivityIntent = getIntent();
        float sensitivity = sensitivityIntent.getFloatExtra("sensitivity", 1);

        avg_tv.setText(String.valueOf(avg));
        sensitivity_tv.setText(String.valueOf(sensitivity));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sp_name = getSharedPreferences(sharedname,0);
        SharedPreferences.Editor editor_name = sp_name.edit();
        String value1 = name.getText().toString();
        editor_name.putString("hong", value1);
        editor_name.commit();

        SharedPreferences sp_age = getSharedPreferences(shareage,0);
        SharedPreferences.Editor editor_age = sp_age.edit();
        String value2 = age.getText().toString();
        editor_age.putString("age", value2);
        editor_age.commit();

        SharedPreferences sp_record = getSharedPreferences(sharerecord,0);
        SharedPreferences.Editor editor_record = sp_record.edit();
        String value3 = record.getText().toString();
        editor_record.putString("record", value3);
        editor_record.commit();

    }
}
