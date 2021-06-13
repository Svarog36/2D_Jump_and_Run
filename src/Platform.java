import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Platform extends GameObject {

    Platform(int x, int y, int width, int height) {
        super(x, y, width, height, Main.platformColor);
    }

    void setColor(Color c){
        ((Rectangle)super.getR()).setFill(c);
    }

    Color getColor(){
        return (Color) ((Rectangle)super.getR()).getFill();
    }

}