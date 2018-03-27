package it.aguzzo.androidgeofirebase;

/**
 * Created by GZZNGL83P24A323Y on 26/03/2018.
 */

public class Favourite {

    private String type;
    private String description;
    private String address;
    private String image;
    private float markerColor;
    private int textColor;

    public Favourite() {
    }

    public Favourite(String type, String description, String address, String image, float markerColor, int textColor) {
        this.type = type;
        this.description = description;
        this.address = address;
        this.image = image;
        this.markerColor = markerColor;
        this.textColor = textColor;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() { return address; }

    public String getImage() { return image; }

    public float getMarkerColor() { return markerColor; }

    public int getTextColor() { return textColor; }

}