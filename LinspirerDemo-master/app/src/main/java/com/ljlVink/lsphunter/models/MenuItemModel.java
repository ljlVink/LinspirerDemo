package com.ljlVink.lsphunter.models;


public class MenuItemModel {
    private int iconResource;
    private String text;

    public MenuItemModel(int iconResource, String text) {
        this.iconResource = iconResource;
        this.text = text;
    }

    public int getIconResource() {
        return iconResource;
    }

    public String getText() {
        return text;
    }
}
