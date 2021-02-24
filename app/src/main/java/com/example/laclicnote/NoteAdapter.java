package com.example.laclicnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> mNoteList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        FloatingActionButton noteFab;
        TextView noteTitle;
        TextView noteSnaps;
        TextView genTime;
        TextView lastModiTime;

        public ViewHolder(View view) {
            super(view);
            noteFab = (FloatingActionButton) view.findViewById(R.id.note_fab_icon);
            noteTitle = (TextView) view.findViewById(R.id.note_title);
            noteSnaps = (TextView) view.findViewById(R.id.note_snaps);
            genTime = (TextView) view.findViewById(R.id.gen_time);
            lastModiTime = (TextView) view.findViewById(R.id.last_modi_time);
        }
    }

    public NoteAdapter(List<Note> noteList) {
        mNoteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = mNoteList.get(position);

        // text
        holder.noteTitle.setText(note.getTitle());
        holder.noteSnaps.setText(note.getSnapShot());

        // time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        holder.genTime.setText(sdf.format(note.getGenTime()));
        holder.lastModiTime.setText(sdf.format(note.getLastModifiedTime()));

        // fab
        Activity thisAct = (Activity) holder.itemView.getContext();
        holder.noteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_note_intent = new Intent(thisAct, EditNoteActivity.class);
                thisAct.startActivity(edit_note_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }
}
