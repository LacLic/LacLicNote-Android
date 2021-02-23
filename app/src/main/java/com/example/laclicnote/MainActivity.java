package com.example.laclicnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList = new ArrayList<>();

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
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                MainActivity.this, android.R.layout.simple_list_item_1, data);
        initNotes();
        NoteAdapter adapter = new NoteAdapter(MainActivity.this, R.layout.note_item, noteList);
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
//                Log.d("noteList", String.valueOf(noteList));
                startActivity(add_new_note_intent);
            }
        });
    }

    private void initNotes() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat time = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        for(int i=0;i<10;++i) {
            Note math = new Note(i*2,i*2,"Math","Math is...",
                    "Math is sky!",time,time);
            noteList.add(math);
            Note chemistry = new Note(i*2+1,i*2+1,"Chemistry","Chem is...",
                    "Chem is try.",time,time);
            noteList.add(chemistry);
        }
    }
}