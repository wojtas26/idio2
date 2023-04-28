package cz.idio.api.response;

import android.widget.Button;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cz.idio.R;

public class Day {
    private List<MenuItemFood> cantMenuItems;

    public Day(String day,int state, int cantMenuDayId,int pos, List<MenuItemFood> cantMenuItems) {
       this.day = day;
        this.state = state;
        this.cantMenuDayId = cantMenuDayId;
        this.pos = pos;
        this.cantMenuItems = cantMenuItems;
    }
    @SerializedName("State")
    private int state;

    @SerializedName("CantMenuDay_Id")
    private int cantMenuDayId;

    @SerializedName("Pos")
    private int pos;

    private String day;
    private String MenuId;
    Button orderButton;
    // konstruktory, gettery a settery

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCantMenuDayId() {
        return cantMenuDayId;
    }

    public void setCantMenuDayId(int cantMenuDayId) {
        this.cantMenuDayId = cantMenuDayId;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public List<MenuItemFood> getCantMenuItems() {
        return cantMenuItems;
    }

    public void setCantMenuItems(List<MenuItemFood> cantMenuItems) {
        this.cantMenuItems = cantMenuItems;
    }
    public Button getOrderButton() {

        return orderButton;
    }

    public Button setOrderButton(Button orderButton) {
        this.orderButton = orderButton;
        return orderButton;
    }

}
