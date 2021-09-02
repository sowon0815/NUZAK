package org.techtown.nuzak;

public class Story {
    int id;
    String Title;
    String Text;
    String Image;

    public Story(){}
    public Story(String Title, String Image){
        this.Title = Title;
        this.Image = Image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public String getText() {
        return Text;
    }

    public String getImage() {
        return Image;
    }
}
