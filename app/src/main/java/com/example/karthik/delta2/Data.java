package com.example.karthik.delta2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by karthik on 27-05-2018.
 */

public class Data {
    String team1, team2, venue, date, time;
    Bitmap img1, img2;
    int id;
Data(){

}

    Data(int id, String team1, String team2, String venue, String date, String time, Bitmap img1, Bitmap img2) {
        this.team1 = team1;
        this.team2 = team2;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.img1 = img1;
        this.img2 = img2;
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public Bitmap getImg1() {
        return img1;
    }

    public Bitmap getImg2() {
        return img2;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
