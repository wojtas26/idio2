package cz.idio.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cz.idio.api.AddPayItem;

public class WorklistResponse {

    @SerializedName("Day")
    private List<DayItem> day;
    @SerializedName("WorkShift_Name")
    private String workShift_Name;
    @SerializedName("HwTime")
    private String hwTime;
    @SerializedName("WorkLen")
    private int workLen;
    @SerializedName("Len")
    private String pause;
    @SerializedName("Work")
    private int work;
    @SerializedName("AddPayList")
    private List<AddPayItem> addPayList;
    @SerializedName("CompHoliday")
    private int compHoliday;
    public void setDay(List<DayItem> day) {
        this.day = day;
    }

    public List<DayItem> getDay() {
        return day;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
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

    public int getWorkLen() {
        return workLen;
    }

    public void setWorkLen(int workLen) {
        this.workLen = workLen;
    }

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }
    public List<AddPayItem> getAddPayList() {
        return addPayList;
    }

    public void setAddPayList(List<AddPayItem> addPayList) {
        this.addPayList = addPayList;
    }

    public int getCompHoliday() {
        return compHoliday;
    }

    public void setCompHoliday(int compHoliday) {
        this.compHoliday = compHoliday;
    }
}
