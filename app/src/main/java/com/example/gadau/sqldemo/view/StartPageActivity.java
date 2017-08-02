package com.example.gadau.sqldemo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gadau.sqldemo.R;

public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        Button scanButton = (Button) findViewById(R.id.button_scan);
        Button listButton = (Button) findViewById(R.id.button_list);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSearchView();
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchListView();
            }
        });
    }

    private void launchSearchView(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void launchListView(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    //Todo: Provide export to Google Docs option and erase database option here
}
