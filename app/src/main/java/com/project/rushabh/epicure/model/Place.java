package com.project.rushabh.epicure.model;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by rushabh.modi on 23/03/18.
 */

public class Place {

    public String name;
    public String information;
    public String address;
    public GeoPoint location;
    public String image;

    public Place() {
    }

    public Place(String name, String information, String address, GeoPoint location, String image) {
        this.name = name;
        this.information = information;
        this.address = address;
        this.location = location;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getInformation() {
        return information;
    }

    public String getAddress() {
        return address;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }
}
