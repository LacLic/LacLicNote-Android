package com.example.laclicnote;

public class Note {
    private int ID;
    private int imgId;
    private String title;
    private String snapShot;
    private String content;
    private Time genTime;
    private Time lastModifiedTime;
    // TODO: picture*9

    public Note(int ID, int imgId, String title, String snapShot, String content,
                Time genTime, Time lastModifiedTime) {
        this.ID = ID;
        this.imgId = imgId;
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

    public String getTitle() {
        return title;
    }

    public String getSnapShot() {
        return snapShot;
    }

    public String getContent() {
        return content;
    }

    public Time getGenTime() {
        return genTime;
    }

    public Time getLastModifiedTime() {
        return lastModifiedTime;
    }
}
