package com.example.laclicnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class EditNoteActivity extends AppCompatActivity {

    private void quitAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditNoteActivity.this);
        dialog.setTitle("Warning");
        dialog.setMessage("Are you sure to discard all your changes?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            quitAlert();
            return true;
        }else {
            return super.onKeyDown(keyCode,event);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                quitAlert();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);


        // 工具栏 Tool Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); // 设置返回键可用

        // 获取传入值
        Intent intent = getIntent();
        Note thisNote = (Note) intent.getSerializableExtra("thisNote");

        // 定位并填写元素
        if(thisNote!=null) {
            TextView noteTitle = findViewById(R.id.title_note_edit);
            noteTitle.setText(thisNote.getTitle());
            TextView noteSnaps = findViewById(R.id.snaps_note_edit);
            noteSnaps.setText(thisNote.getSnapShot());
            Switch noteIsFav = findViewById(R.id.isFav_note_edit);
            noteIsFav.setChecked(thisNote.isFavorite());
            TextInputEditText noteContent = findViewById(R.id.content_note_edit);
            noteContent.setText(thisNote.getContent());
        }

    }
}