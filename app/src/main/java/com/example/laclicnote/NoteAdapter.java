package com.example.laclicnote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class NoteAdapter extends ArrayAdapter<Note> {
    private int resourceId;
    public NoteAdapter(Context context, int textViewRsrId, List<Note> objects) {
        super(context, textViewRsrId, objects);
        resourceId = textViewRsrId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Note note = getItem(position); // 获取当前Note实例
        Context thisAct = getContext();
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        FloatingActionButton note_fab_icon = (FloatingActionButton) view.findViewById(R.id.note_fab_icon);
        note_fab_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_note_intent = new Intent(thisAct, EditNoteActivity.class);
                thisAct.startActivity(edit_note_intent);
            }
        });
        TextView note_title = (TextView) view.findViewById(R.id.note_title);
        TextView note_snaps = (TextView) view.findViewById(R.id.note_snaps);
        TextView note_gen_time = (TextView) view.findViewById(R.id.gen_time);
        TextView note_last_modi_time = (TextView) view.findViewById(R.id.last_modi_time);
//        note_fab_icon.setBackground();

        note_title.setText(note.getTitle());
        note_snaps.setText(note.getSnapShot());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        note_gen_time.setText(sdf.format(note.getGenTime()));
        note_last_modi_time.setText(sdf.format(note.getLastModifiedTime()));

        return view;
    }
}
