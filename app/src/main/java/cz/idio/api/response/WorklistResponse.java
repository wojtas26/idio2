package cz.idio.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorklistResponse {

    @SerializedName("Day")
    private List<DayItem> day;
    @SerializedName("WorkShift_Name")
    private String workShift_Name;
    @SerializedName("HwTime")
    private String hwTime;
    @SerializedName("Work")
    private String work;
    @SerializedName("Len")
    private String pause;
   // @SerializedName("Day")
    private String days;

    public void setDay(List<DayItem> day) {
        this.day = day;
    }

    public List<DayItem> getDay() {
        return day;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getWorkShift_Name() {
        return workShift_Name;
    }

    public void setWorkShift_Name(String workShift_Name) {
        this.workShift_Name = workShift_Name;
    }

    public String getHwTime() {
        return hwTime;
    }

    public void setHwTime(String hwTime) {
        this.hwTime = hwTime;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }



}
