package cz.idio.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DayItem {
    @SerializedName("IsRepair")
    private boolean isRepair;
    private String fTicket;
    private String fMeals;
    @SerializedName("Reg")
    private List<Reg> reg;
    @SerializedName("Day")
    private String day;
    private String compHoliday;
    private boolean isHoliday;
    private String isBroken;
    private String id;
    private boolean isWeekend;
    @SerializedName("WorkLen")
    private int workLen;
    private String workShiftColor;
    @SerializedName("WorkShift_Name")
    private String workShiftName;
    @SerializedName("Work")
    private double work;
    @SerializedName("Tube")
    private List<Tube> tube;
    private int saldo;
    @SerializedName("WorkShift_Id")
    private int workShiftId;
    private String workShiftReq;

    //getters and setters

    public boolean isRepair() {
        return isRepair;
    }

    public void setRepair(boolean repair) {
        isRepair = repair;
    }

    public String getfTicket() {
        return fTicket;
    }

    public void setfTicket(String fTicket) {
        this.fTicket = fTicket;
    }

    public String getfMeals() {
        return fMeals;
    }

    public void setfMeals(String fMeals) {
        this.fMeals = fMeals;
    }

    public List<Reg> getReg() {
        return reg;
    }

    public void setReg(List<Reg> reg) {
        this.reg = reg;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCompHoliday() {
        return compHoliday;
    }

    public void setCompHoliday(String compHoliday) {
        this.compHoliday = compHoliday;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public String getIsBroken() {
        return isBroken;
    }

    public void setIsBroken(String isBroken) {
        this.isBroken = isBroken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public int getWorkLen() {
        return workLen;
    }

    public void setWorkLen(int workLen) {
        this.workLen = workLen;
    }

    public String getWorkShiftColor() {
        return workShiftColor;
    }

    public void setWorkShiftColor(String workShiftColor) {
        this.workShiftColor = workShiftColor;
    }

    public String getWorkShiftName() {
        return workShiftName;
    }

    public void setWorkShiftName(String workShiftName) {
        this.workShiftName = workShiftName;
    }

    public double getWork() {
        return work;
    }

    public void setWork(double work) {
        this.work = work;
    }

    public List<Tube> getTube() {
        return tube;
    }

    public void setTube(List<Tube> tube) {
        this.tube = tube;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getWorkShiftId() {
        return workShiftId;
    }

    public void setWorkShiftId(int workShiftId) {
        this.workShiftId = workShiftId;
    }

    public String getWorkShiftReq() {
        return workShiftReq;
    }

    public void setWorkShiftReq(String workShiftReq) {
        this.workShiftReq = workShiftReq;
    }


}
