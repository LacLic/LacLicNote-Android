package com.example.laclicnote;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        FloatingActionButton note_fab_icon = (FloatingActionButton) view.findViewById(R.id.note_fab_icon);
        TextView note_title = (TextView) view.findViewById(R.id.note_title);
        TextView note_snaps = (TextView) view.findViewById(R.id.note_snaps);
//        note_fab_icon.setBackground();

        note_title.setText(note.getTitle());
        note_snaps.setText(note.getSnapShot());
        return view;
    }
}
