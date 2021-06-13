import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class GameObject {

    private Node r;

    GameObject(int x, int y, int width, int height, Color color){
        r = new Rectangle();
        r.setTranslateX(x);
        r.setTranslateY(y);
        ((Rectangle)r).setWidth(width);
        ((Rectangle)r).setHeight(height);
        ((Rectangle) r).setFill(color);

    }

    Node getR() {
        return r;
    }

    double getX(){
        return r.getTranslateX();
    }

    double getY(){
        return r.getTranslateY();
    }

    int getWidth(){
        return (int)((Rectangle) r).getWidth();
    }

    int getHeight(){
        return (int)((Rectangle) r).getHeight();
    }

    void setHeight(int height){
        ((Rectangle) r).setHeight(height);
    }

    void setWidth(int width){
        ((Rectangle) r).setWidth(width);
    }

}