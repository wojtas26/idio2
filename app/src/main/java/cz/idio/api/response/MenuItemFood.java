package cz.idio.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuItemFood {
    @SerializedName("Type")
    private int type;
    @SerializedName("Name")
    private String nameFood;
    private int dist;
    @SerializedName("CantMenu_Id")
    private String cantMenuId;
    private boolean algWarn;
    @SerializedName("Pos")
    private int pos;
    private int exCnt;
    private int ordCnt;
    private double price;
    private String dotPrice;
    private List<MenuItemFood> menuItems;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public String getCantMenuId() {
        return cantMenuId;
    }

    public void setCantMenuId(String cantMenuId) {
        this.cantMenuId = cantMenuId;
    }

    public boolean isAlgWarn() {
        return algWarn;
    }

    public void setAlgWarn(boolean algWarn) {
        this.algWarn = algWarn;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getExCnt() {
        return exCnt;
    }

    public void setExCnt(int exCnt) {
        this.exCnt = exCnt;
    }

    public int getOrdCnt() {
        return ordCnt;
    }

    public void setOrdCnt(int ordCnt) {
        this.ordCnt = ordCnt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDotPrice() {
        return dotPrice;
    }

    public void setDotPrice(String dotPrice) {
        this.dotPrice = dotPrice;
    }


    public List<MenuItemFood> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemFood> menuItems) {
        this.menuItems = menuItems;
    }
}
