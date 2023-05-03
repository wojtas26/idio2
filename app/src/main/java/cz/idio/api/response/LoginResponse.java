package cz.idio.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {
    @SerializedName("Person_Name")
    private String personName;
    @SerializedName("Status")
    private int status;
    @SerializedName("Count")
    private int count;

    @SerializedName("Person_Id")
    private int personId;


    // Gettery a settery pro všechny proměnné

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
