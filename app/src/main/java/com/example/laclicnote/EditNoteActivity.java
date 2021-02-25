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

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    private Note thisNote;
    private int enterCode;
    private int thisPos;

    private Note noteReturn() {
        TextView noteTitle = findViewById(R.id.title_note_edit);
        TextView noteSnaps = findViewById(R.id.snaps_note_edit);
        Switch noteIsFav = findViewById(R.id.isFav_note_edit);
        TextInputEditText noteContent = findViewById(R.id.content_note_edit);
        Note return_note = new Note(thisNote.getID(),thisNote.getImgId(),noteIsFav.isChecked(),
                noteTitle.getText().toString(),noteSnaps.getText().toString(),
                noteContent.getText().toString(),thisNote.getGenTime(), new Date(System.currentTimeMillis()));
        return return_note;
    }

    private void quitAlert(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditNoteActivity.this);
        dialog.setTitle("Warning");
        dialog.setMessage(message);
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
            quitAlert("Are you sure to discard all your changes?");
            return true;
        }else {
            return super.onKeyDown(keyCode,event);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent_return = new Intent();
        switch (item.getItemId()) {
            case android.R.id.home:
                quitAlert("Are you sure to discard all your changes?");
                break;
            case R.id.delete_edit_note:
                quitAlert("Do you really want to delete this note?");
                intent_return.putExtra("code",CONST.DELETE);
                intent_return.putExtra("this_pos",thisPos);
                setResult(RESULT_OK, intent_return);
                break;
            case R.id.confirm_edit_note:
                intent_return.putExtra("note",noteReturn());
                switch (enterCode) {
                    case CONST.EDIT:
                        intent_return.putExtra("code",CONST.EDIT);
                        intent_return.putExtra("this_pos",thisPos);
                        break;
                    case CONST.ADD:
                        intent_return.putExtra("code",CONST.ADD);
                        break;
                }
                intent_return.putExtra("note_return",noteReturn());
                setResult(RESULT_OK, intent_return);
                finish();
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
        thisNote = (Note) intent.getSerializableExtra("this_note");
        thisPos = intent.getIntExtra("this_pos",CONST.NULL);
        enterCode = intent.getIntExtra("code",CONST.NULL);

        // 定位并填写元素
        if(enterCode==CONST.EDIT) {
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