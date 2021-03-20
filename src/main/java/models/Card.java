package models;


import javafx.scene.image.Image;

public class Card {
    private final Image value;
    private final Image background;
    private final int id;

    public Card(Image value, Image background, int id) {
        this.value = value;
        this.background = background;
        this.id = id;
    }

    public Image getValue() {
        return value;
    }

    public Image getBackground() {
        return background;
    }

    public int getId() {
        return id;
    }
}
