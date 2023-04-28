package cz.idio.api.response;

import com.google.gson.annotations.SerializedName;

public class Reg {
    @SerializedName("Registration_Id")
    private int registrationId;
    private String IconIdx;
    @SerializedName("HwTime")
    private String HwTime;

    public int getRegistrationId() {
        return registrationId;
    }


    public String getIconIdx() {
        return IconIdx;
    }

    public void setIconIdx(String iconIdx) {
        IconIdx = iconIdx;
    }

    public String getHwTime() {
        return HwTime;
    }

    public void setHwTime(String hwTime) {
        HwTime = hwTime;
    }
}
