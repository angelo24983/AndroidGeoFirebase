package it.aguzzo.androidgeofirebase;

/**
 * Created by GZZNGL83P24A323Y on 26/03/2018.
 */

public class Favourite {

    private String type;
    private String description;

    public Favourite() {
    }

    public Favourite(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
