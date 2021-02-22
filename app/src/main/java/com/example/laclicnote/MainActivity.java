package com.example.laclicnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private final String data[] = {"Apple","Banana","Orange",
            "Watermelon","Pear","Grape","Pineapple","Mango","Cherry","Apple","Banana","Orange",
            "Watermelon","Pear","Grape","Pineapple","Mango","Cherry","Apple","Banana","Orange",
            "Watermelon","Pear","Grape","Pineapple","Mango","Cherry",};
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 列表视窗
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, data);
        ListView mainList = (ListView) findViewById(R.id.main_list);
        mainList.setAdapter(adapter);


        // 主活动浮标
        FloatingActionButton fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent add_new_note_intent = new Intent(MainActivity.this, EditNewNote.class);
                startActivity(add_new_note_intent);
            }
        });
    }
}