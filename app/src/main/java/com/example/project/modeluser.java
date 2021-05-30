package com.example.project;

public class modeluser {
    String text;
    String img;
    String id;

    public modeluser(String id,String img,String text) {
        this.text = text;
        this.img = img;
        this.id=id;
    }
        modeluser(){}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String  getImg() {

        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
