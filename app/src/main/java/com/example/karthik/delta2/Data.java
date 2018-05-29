package com.example.karthik.delta2;

/**
 * Created by karthik on 27-05-2018.
 */

public class Data {
    String team1,team2,venue,date,time;
    Data(String team1,String team2,String venue,String date,String time){
        this.team1=team1;
        this.team2=team2;
        this.venue=venue;
        this.date=date;
        this.time=time;

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
