package com.example.laclicnote;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList = new ArrayList<>();

    private ProgressBar progressBar;

    void displayRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NoteAdapter adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);
    }

    private void save() throws IOException {
        // 转换为JSON
//        String noteStr = JSON.toJSONString(noteList);
        Gson gson = new Gson();
        String noteStr = gson.toJson(noteList);
//        Log.d("save()",noteStr);

        // 文件写入
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("noteData", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(noteStr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if (writer!=null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void load() {
        // 文件读取
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("noteData");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine())!=null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // json转列表
//        JSONArray array = JSONArray.parseArray(content.toString());
//        List<Note> temp = JSONObject.parseArray(array.toJSONString(), Note.class);
//        List<Note> temp = JSON.parseObject(content.toString(),new TypeReference<List<Note>>(){});
//        List<Note> temp = JSON.parseArray(content.toString(),Note.class);
//        noteList = JSON.parseArray(content.toString(),Note.class);
        Gson gson = new Gson();
        noteList = gson.fromJson(content.toString(), new TypeToken<List<Note>>() {}.getType());
//        Log.d("Temp",temp.toString());
    }

    int findNotePos(int ID) {
        int pos = CONST.NULL;
        for(int i = noteList.size()-1;i>=0;--i) {
            if(noteList.get(i).getID()==ID) {
                pos = i;
            }
        }
        if(pos==CONST.NULL) {
            Log.d("noteList","NoteFindError");
        }
        Log.d("position",Integer.toString(pos));
        return pos;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /** extraData:
     *      DELETE: "this_pos"
     *      EDIT: "this_pos", "note_return"
     *      ADD: "note_return"
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int note_return_code = data.getIntExtra("code", CONST.NULL);
            int returnNoteID = data.getIntExtra("id", CONST.NULL);
            Note returnNote = (Note) data.getSerializableExtra("note_return");
            switch (note_return_code) {
                case CONST.DELETE:
//                    noteList.remove(findNotePos(returnNoteID));
                    findNotePos(returnNoteID);
                    break;
                case CONST.EDIT:
                    findNotePos(returnNoteID);
//                    noteList.set(findNotePos(returnNoteID), returnNote);
                    break;
                case CONST.ADD:
                    noteList.add(returnNote);
                    break;
            }

            // 写入到文件
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 重构列表视窗
            displayRecyclerView();
        }
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
                add_new_note_intent.putExtra("code",CONST.ADD);
                add_new_note_intent.putExtra("newID",noteList.isEmpty()
                        ? 1
                        : noteList.get(noteList.size()-1).getID()+1);
                startActivityForResult(add_new_note_intent,1);
            }
        });

        // 隐藏进度条
        progressBar.setVisibility(View.GONE);

        // 列表视窗 Recycler View
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                MainActivity.this, android.R.layout.simple_list_item_1, data);
        load();
//        initNotes();
        Log.d("noteList",noteList.toString());
        displayRecyclerView();
    }

//    private void initNotes() {
//        Date date = new Date(System.currentTimeMillis());
//        for(int i=0;i<10;++i) {
//            Note math = new Note(i*2,i*2,true,"Math","Math is...",
//                    "Math is sky!",date,date);
//            noteList.add(math);
//            Note chemistry = new Note(i*2+1,i*2+1,false,"Chemistry","Chem is...",
//                    "Chem is try.",date,date);
//            noteList.add(chemistry);
//        }
//    }
}