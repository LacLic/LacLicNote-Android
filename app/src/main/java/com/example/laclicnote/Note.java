package com.example.laclicnote;

import java.text.SimpleDateFormat;

public class Note {
    private int ID;
    private int imgId;
    private boolean favorite;
    private String title;
    private String snapShot;
    private String content;
    private SimpleDateFormat genTime;
    private SimpleDateFormat lastModifiedTime;
    // TODO: picture*9

    public Note(int ID, int imgId, boolean favorite, String title, String snapShot, String content,
                SimpleDateFormat genTime, SimpleDateFormat lastModifiedTime) {
        this.ID = ID;
        this.imgId = imgId;
        this.favorite = favorite;
        this.title = title;
        this.snapShot = snapShot;
        this.content = content;
        this.genTime = genTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    public int getID() {
        return ID;
    }

    public int getImgId() {
        return imgId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getTitle() {
        return title;
    }

    public String getSnapShot() {
        return snapShot;
    }

    public String getContent() {
        return content;
    }

    public SimpleDateFormat getGenTime() {
        return genTime;
    }

    public SimpleDateFormat getLastModifiedTime() {
        return lastModifiedTime;
    }
}
