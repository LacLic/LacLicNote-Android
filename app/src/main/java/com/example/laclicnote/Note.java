package com.example.laclicnote;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private int ID;
    private int imgId;
    private boolean favorite;
    private String title;
    private String snapShot;
    private String content;
    private Date genTime;
    private Date lastModifiedTime;
    // TODO: picture*9

    @NonNull
    @Override
    public String toString() {
        return "{ID: " + Integer.toString(ID) + ", title: " + title + "}";
    }

    @Override
    public int hashCode() {
        return ID;
    }

    public Note() {}

    public Note(int ID, int imgId, boolean favorite, String title, String snapShot, String content,
                Date genTime, Date lastModifiedTime) {
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

    public Date getGenTime() {
        return genTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnapShot(String snapShot) {
        this.snapShot = snapShot;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

}
