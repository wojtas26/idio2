package cz.idio.api;

import com.google.gson.annotations.SerializedName;

public class AddPayItem {

    @SerializedName("Name")
    private String name;

    @SerializedName("Len")
    private int len;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}

