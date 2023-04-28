package cz.idio.api.response;



import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CantResponse {
    @SerializedName("CantStatus")
    private int CantStatus;

    @SerializedName("Pos")
    private int pos;

    @SerializedName("Count")
    private int count;

    @SerializedName("Vend")
    private List<Vend> vends;

    @SerializedName("Menu")
    private List<MenuItemFood> CantMenuItems;
    // konstruktory, gettery a settery

    public int getCantStatus() {
        return CantStatus;
    }

    public void setCantStatus(int CantStatus) {
        this.CantStatus = CantStatus;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Vend> getVends() {
        return vends;
    }

    public void setVends(List<Vend> vends) {
        this.vends = vends;
    }

    public List<MenuItemFood> getCantMenuItems() {
        return CantMenuItems;
    }

    public void setCantMenuItems(List<MenuItemFood> CantMenuItems) {
        this.CantMenuItems = CantMenuItems;
    }

}

