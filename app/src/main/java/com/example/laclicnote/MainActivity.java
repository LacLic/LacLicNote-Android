package com.example.laclicnote;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList = new ArrayList<>();

    private ProgressBar progressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 进度条 Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        // 工具栏 Tool Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // 主活动浮标 Floating Action Button
        FloatingActionButton fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent add_new_note_intent = new Intent(MainActivity.this, EditNoteActivity.class);
//                Log.d("noteList", String.valueOf(noteList));
                startActivity(add_new_note_intent);
            }
        });

        // 隐藏进度条
        progressBar.setVisibility(View.GONE);

        // 列表视窗 List View
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                MainActivity.this, android.R.layout.simple_list_item_1, data);
        initNotes();
        NoteAdapter adapter = new NoteAdapter(MainActivity.this, R.layout.note_item, noteList);
        ListView mainList = (ListView) findViewById(R.id.main_list);
        mainList.setAdapter(adapter);

    }

    private void initNotes() {
        Date date = new Date(System.currentTimeMillis());
        for(int i=0;i<10;++i) {
            Note math = new Note(i*2,i*2,false,"Math","Math is...",
                    "Math is sky!",date,date);
            noteList.add(math);
            Note chemistry = new Note(i*2+1,i*2+1,false,"Chemistry","Chem is...",
                    "Chem is try.",date,date);
            noteList.add(chemistry);
        }
    }
}