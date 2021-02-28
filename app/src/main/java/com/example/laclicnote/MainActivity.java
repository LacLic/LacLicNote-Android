package com.example.laclicnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean displayFav = false;
    private int sequenceCondition = CONST.GENERATE_TIME;

    private List<Note> noteList = new ArrayList<>();

    private Menu mainMenu;

    void displayRecyclerView(List<Note> lst) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NoteAdapter adapter = new NoteAdapter(lst);
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
        mainMenu = menu;
        MenuItem searchAction = (MenuItem) menu.findItem(R.id.search_main);
        initSearchView(searchAction);
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
                    noteList.remove(findNotePos(returnNoteID));
                    break;
                case CONST.EDIT:
                    noteList.set(findNotePos(returnNoteID), returnNote);
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
            displayRecyclerView(noteList);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sequenceCondition==CONST.GENERATE_TIME) {
            sortNoteListGen();
        }else if(sequenceCondition==CONST.MODIFIED_TIME) {
            sortNoteListModi();
        }

        if(displayFav) {
            MenuItem searchItem = mainMenu.findItem(R.id.search_main);
            displayRecyclerView(searchFav());
            searchItem.setVisible(false);
        }else {
            displayRecyclerView(noteList);
        }
    }

    private void initSearchView(MenuItem item) {
        // 定位searchView
        final SearchView searchView = (SearchView) item.getActionView();
//        Log.d("item search view","");

        // 监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MenuItem favItem = mainMenu.findItem(R.id.display_fav_main);
                MenuItem seqItem = mainMenu.findItem(R.id.sequence_main);
                if(newText.isEmpty()) {
                    favItem.setVisible(true);
                    seqItem.setVisible(true);
                }
                else {
                    favItem.setVisible(false);
                    seqItem.setVisible(false);
                }
                List<Note> filteredModelList = searchTitle(newText);
                displayRecyclerView(filteredModelList);
                return true;
            }
        });
    }

    private List<Note> searchTitle(String newText) {
        List<Note> result = new ArrayList<>();
        for(Iterator<Note> it = noteList.iterator(); it.hasNext();){
            Note temp = it.next();
            if(temp.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                result.add(temp);
            }
        }
        return result;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 进度条 Progress Bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);


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

        // 载入文件
        load();

        // 搜索
//        MenuItem searchAction = (MenuItem) findViewById(R.id.search);
//
//        initSearchView(searchAction);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.sequence_main:
                if(sequenceCondition==CONST.GENERATE_TIME) {
                    sortNoteListModi();
                    item.setTitle(R.string.genTime);
                    sequenceCondition = CONST.MODIFIED_TIME;
                }else if(sequenceCondition==CONST.MODIFIED_TIME) {
                    sortNoteListGen();
                    item.setTitle(R.string.modiTime);
                    sequenceCondition = CONST.GENERATE_TIME;
                }

                // 保存
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(displayFav) {
                    displayRecyclerView(searchFav());
                }else {
                    displayRecyclerView(noteList);
                }
                break;

            case R.id.display_fav_main:
                MenuItem searchItem = mainMenu.findItem(R.id.search_main);
                if(displayFav) {
                    item.setIcon(R.drawable.baseline_favorite_border_black_24dp);
                    displayRecyclerView(noteList);
                    searchItem.setVisible(true);
                }else{
                    item.setIcon(R.drawable.baseline_favorite_black_24dp);
                    displayRecyclerView(searchFav());
                    searchItem.setVisible(false);
                }
                displayFav = !displayFav;

        }
        return true;
    }

    private List<Note> searchFav() {
        List<Note> result = new ArrayList<>();
        for(Iterator<Note> it = noteList.iterator(); it.hasNext();){
            Note temp = it.next();
            if(temp.isFavorite()) {
                result.add(temp);
            }
        }
        return result;
    }

    private void sortNoteListGen() {
        Collections.sort(noteList, new Comparator<Note>(){
            @Override
            public int compare(Note o1, Note o2) {
                return o1.getGenTime().compareTo(o2.getGenTime());
            }
        });
    }

    private void sortNoteListModi() {
        Collections.sort(noteList, new Comparator<Note>(){
            @Override
            public int compare(Note o1, Note o2) {
                return o1.getLastModifiedTime().compareTo(o2.getLastModifiedTime());
            }
        });
    }
}