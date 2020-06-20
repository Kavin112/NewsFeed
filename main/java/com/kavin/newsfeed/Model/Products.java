package com.kavin.newsfeed.Model;

public class Products
{
    private String eventname,description, venue,image, category, eid,date, time;

    public Products()
    {


    }


    public Products(String eventname, String description, String venue, String image, String category, String eid, String date, String time)
    {
        this.eventname = eventname;
        this.description = description;
        this.venue = venue;
        this.image = image;
        this.category = category;
        this.eid = eid;
        this.date = date;
        this.time = time;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


