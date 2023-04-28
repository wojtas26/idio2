package cz.idio.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class Vend {
    @SerializedName("Id")
    private String id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Day")
    private List<Day> days;
    // konstruktory, gettery a settery

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

}
