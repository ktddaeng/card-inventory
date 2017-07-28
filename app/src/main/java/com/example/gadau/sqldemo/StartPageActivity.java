package com.example.gadau.sqldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        Toast.makeText(this, "List View Under Construction", Toast.LENGTH_SHORT).show();
    }
}
