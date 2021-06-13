import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

class World {

    List<Platform> platforms;
    List<Platform> potentialPlatforms;
    private Group worldGroup = new Group();
    Player player;
    private long timestamp2 = 0, timestamp1;
    private String worldName;

    Timeline timeline;


    Label startNotice;

    World(Pane root, String worldName) {

        platforms = new ArrayList<>();
        potentialPlatforms = new ArrayList<>();
        this.worldName = worldName;

        addStartNotice();

        timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {

            timestamp2 = System.currentTimeMillis();

            if(timestamp1 != 0)
                update((double)(timestamp2 - timestamp1)/1000, root);

            timestamp1 = System.currentTimeMillis();

        }));
        timeline.setCycleCount(Animation.INDEFINITE);

    }

    private void update(double elapsedTime, Pane root) {

        if(Main.gameState == 1) {
            for (Platform p : platforms) {

               stopPiercing(player, p, elapsedTime);
               collisionDetection(p);

            }
        }

        player.update(elapsedTime);

        calculateCameraView(root);
    }

    private void collisionDetection(Platform p){

        if (CollisionDetection.collisionDetectionRectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight() + 1, p.getX(), p.getY(), p.getWidth(), p.getHeight())) {

            if (p.getY() > player.getY() && (p.getX() < player.getX() || p.getX() + p.getWidth() > player.getX() + player.getWidth())) {
                if (player.dy > 0) {
                    player.dy = 0;
                    player.getR().setTranslateY(p.getY() - player.getHeight());
                    player.grounded = true;
                    player.canJump = true;
                    player.lastTouchedPlatform = p;
                }
            } else if (p.getY() < player.getY() && (p.getX() < player.getX() && p.getX() + p.getWidth() > player.getX() + player.getWidth())) {
                if (player.dy < 0) {
                    player.dy = 0;
                    player.getR().setTranslateY(p.getY() + p.getHeight());
                    player.lastTouchedPlatform = p;
                }
            }

            if (p.getX() > player.getX() && p.getY() < player.getY() && p.getY() + p.getHeight() > player.getY() + player.getHeight()) {
                player.dx = -Main.playerSpeed;
                player.getR().setTranslateX(p.getX() - player.getWidth());
                player.canJump = true;
                player.lastTouchedPlatform = p;
            } else if (p.getX() < player.getX() && p.getY() < player.getY() && p.getY() + p.getHeight() > player.getY() + player.getHeight()) {
                player.dx = Main.playerSpeed;
                player.getR().setTranslateX(p.getX() + p.getWidth());
                player.canJump = true;
                player.lastTouchedPlatform = p;
            }

        } else {
            if(p == player.lastTouchedPlatform)
                player.grounded = false;
        }

    }

    void calculateCameraView(Pane root) {

        Main.relativePlayerPositionX = player.getR().getTranslateX() + root.getTranslateX() - Main.width/2; // Entfernung zur Mitte des Fensters
        Main.relativePlayerPositionY = player.getR().getTranslateY() + root.getTranslateY() - Main.height/2;

        if (Main.relativePlayerPositionX > Main.maxPlayerOffsetToCenter && Main.relativePlayerPositionX > 0) {
            root.setTranslateX((-player.getR().getTranslateX() + Main.maxPlayerOffsetToCenter + (double)Main.width / 2) * Main.scaling);
        }else if(Main.relativePlayerPositionX < -Main.maxPlayerOffsetToCenter && Main.relativePlayerPositionX < 0){
            root.setTranslateX((-player.getR().getTranslateX() - Main.maxPlayerOffsetToCenter + (double)Main.width / 2) * Main.scaling);
        }

        if (Main.relativePlayerPositionY > Main.maxPlayerOffsetToCenter && Main.relativePlayerPositionY > 0) {
            root.setTranslateY((-player.getR().getTranslateY() + Main.maxPlayerOffsetToCenter + (double)Main.height / 2) * Main.scaling);
        }else if(Main.relativePlayerPositionY < -Main.maxPlayerOffsetToCenter && Main.relativePlayerPositionY < 0){
            root.setTranslateY((-player.getR().getTranslateY() - Main.maxPlayerOffsetToCenter + (double)Main.height / 2) * Main.scaling);
        }

    }

    private void stopPiercing(Player player, Platform p, double elapsedTime){

        if(player.dy > 0) {
            if (CollisionDetection.collisionDetectionRectangle(player.getX(), player.getY() + player.getHeight(), player.getWidth(), elapsedTime * player.dy, p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
                if (elapsedTime * player.dy > p.getY() - (player.getY() + player.getHeight())) {
                    if (p.getY() - (player.getY() + player.getHeight()) > 0)
                        player.alterVelocityY(p.getY() - (player.getY() + player.getHeight()), elapsedTime);
                }
            }
        }else if(player.dy < 0){
            if (CollisionDetection.collisionDetectionRectangle(player.getX(), player.getY() - elapsedTime * -player.dy, player.getWidth(), elapsedTime * -player.dy, p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
                if (elapsedTime * -player.dy > player.getY() - (p.getY() + p.getHeight())) {
                    if (player.getY() - (p.getY() + p.getHeight()) > 0)
                        player.alterVelocityY(player.getY() - (p.getY() + p.getHeight()), elapsedTime);
                }
            }
        }

        if(player.dx > 0){
            if (CollisionDetection.collisionDetectionRectangle(player.getX() + player.getWidth(), player.getY(), elapsedTime * player.dx, player.getHeight(), p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
                if (elapsedTime * player.dx > p.getX() - (player.getX() + player.getWidth())) {
                    if (p.getX() - (player.getX() + player.getWidth()) > 0)
                        player.alterVelocityX(p.getX() - (player.getX() + player.getWidth()), elapsedTime);
                }
            }
        }else if(player.dx < 0){
            if (CollisionDetection.collisionDetectionRectangle(player.getX() - elapsedTime * -player.dx, player.getY(), elapsedTime * -player.dx, player.getHeight(), p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
                if (elapsedTime * -player.dx >  player.getX() - (p.getX() + p.getWidth())) {
                    if (player.getX() - (p.getX() + p.getWidth()) > 0)
                        player.alterVelocityX(player.getX() - (p.getX() + p.getWidth()), elapsedTime);
                }
            }
        }

    }

    void addDefaultStartBuild(){
       initialisePlayer(0,0,10,10);

       addNewPlatform(-200,300,400,20);
       addNewPlatform(-220, 280,20,40);
       addNewPlatform(200,280,20,40);
    }

    private void addStartNotice(){

        startNotice = new Label();

        startNotice.setText("Press Space or E to start");
        startNotice.setTranslateX(0);
        startNotice.setTranslateY(0);
        startNotice.setScaleX(2);
        startNotice.setScaleY(2);
        startNotice.setTextFill(Color.BLUE);
        worldGroup.getChildren().add(startNotice);
    }

    void addNewPlatform(int x, int y, int width, int height) {
        platforms.add(new Platform(x, y, width, height));
        worldGroup.getChildren().add(platforms.get(platforms.size() - 1).getR());
    }

    void initialisePlayer(int x, int y, int width, int height){
        player = new Player(x, y, width, height, Main.playerColor);
        worldGroup.getChildren().add(player.getR());
    }

    Group getWorldGroup() {
        return worldGroup;
    }
}