package com.elliot.mitchell.hudlapp.enums;

/**
 * Created by Elliot on 3/3/2015.
 */
public enum MenuAction {
    SEARCH(0),FAVORITES(1);

    public int id;

    MenuAction(int id){
        this.id = id;
    }

    public static MenuAction getMenuActionById(int id){
        for (MenuAction eMA : MenuAction.values()) {
            if(eMA.id == id){
                return eMA;
            }
        }

        return null;
    }
}
