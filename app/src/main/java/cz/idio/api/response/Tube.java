package cz.idio.api.response;

import com.google.gson.annotations.SerializedName;

public class Tube {
    @SerializedName("Len")
    int len;
    @SerializedName("Name")
    String names;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        len = len;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String name) {
        names = name;
    }



}
