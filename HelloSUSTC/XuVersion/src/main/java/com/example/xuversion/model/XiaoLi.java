package com.example.xuversion.model;

public class XiaoLi {
    private String info;
    private int imageID;

    /**
     * constructor
     * @param info xiaoli information
     * @param imageID image id
     */
    public XiaoLi(String info, int imageID){
        this.info = info;
        this.imageID = imageID;
    }

    /**
     * get information
     * @return information
     */
    public String getInfo() {
        return info;
    }

    /**
     * set information
     * @param info information
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * get image id
     * @return image id
     */
    public int getImageID() {
        return imageID;
    }

}
