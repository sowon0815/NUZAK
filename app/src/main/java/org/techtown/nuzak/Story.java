package org.techtown.nuzak;

public class Story {
    int id;
    String Title;
    String Text;
    String Image;
    String Keyword;
    int level;

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

    public void setKeyword(String keyword) { Keyword = keyword; }

    public void setLevel(int level) {this.level = level;}

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

    public String getKeyword() { return Keyword; }

    public int getLevel() { return this.level; }
}
