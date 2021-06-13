import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ConcurrentModificationException;

class Controls {

    static void addControls(Stage primaryStage, World world){

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {

            if(e.getCode() == KeyCode.W || e.getCode() == KeyCode.SPACE){

                if(world.player.canJump && world.player.dy <= 100 && Main.gameState == 1) {
                    world.player.dy = -Main.jumpHeight;
                    world.player.canJump = false;
                    world.player.jumpBoost = Main.jumpBoost;

                    if(world.player.dx == 0) {
                        world.player.dx = Main.playerSpeed;
                        world.startNotice.setVisible(false);
                    }

                }else if(Main.gameState == 2)
                    world.player.dy = -Main.playerSpeed;

            } else if(e.getCode() == KeyCode.E && (Main.gameState == 1 || Main.gameState == 2)){

                world.startNotice.setVisible(false);

                if(Main.gameState == 1) {
                    Main.gameState = 2;
                    world.player.dx = 0;
                    world.player.dy = 0;
                }else {
                    Main.gameState = 1;
                    world.player.dx = Main.playerSpeed;
                }
            }

            if(Main.gameState == 2) {

                if (e.getCode() == KeyCode.A) {
                    world.player.dx = -Main.playerSpeed;
                } else if (e.getCode() == KeyCode.D) {
                    world.player.dx = Main.playerSpeed;
                } else if (e.getCode() == KeyCode.S) {
                    world.player.dy = Main.playerSpeed;
                } else if (e.getCode() == KeyCode.ENTER) {
                    world.player.getR().setTranslateX(0);
                    world.player.getR().setTranslateY(0);
                }

            }

        });

        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, e -> {

            if((e.getCode() == KeyCode.W || e.getCode() == KeyCode.SPACE) && Main.gameState == 1){
                world.player.jumpBoost = 0;
            }else if((e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) && Main.gameState == 2){
                world.player.dx = 0;
            }else if((e.getCode() == KeyCode.W || e.getCode() == KeyCode.S)  && Main.gameState == 2){
                world.player.dy = 0;
            }

        });

        primaryStage.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{

            boolean clickedOnPlatform = false;

            if(e.getButton() == MouseButton.PRIMARY) {
                for (int i = 0; i < world.platforms.size(); i++) {
                    Platform p = world.platforms.get(i);
                    if (CollisionDetection.collisionDetectionRectangle(e.getX() - primaryStage.getScene().getRoot().getTranslateX(), e.getY() - primaryStage.getScene().getRoot().getTranslateY(), 1, 1, p.getX(), p.getY(), p.getWidth(), p.getHeight()) && world.potentialPlatforms.size() == 0) {
                        if (p.getColor() == Main.platformColor) {
                            p.setColor(Color.AQUA);
                        } else {
                            p.setColor(Main.platformColor);
                        }
                        clickedOnPlatform = true;
                    }
                }
                if (!clickedOnPlatform) {
                    for (Platform p : world.platforms)
                        p.setColor(Main.platformColor);

                    if(world.potentialPlatforms.size() > 0) {
                        world.potentialPlatforms.get(0).setColor(Main.platformColor);
                        world.potentialPlatforms.get(0).getR().toBack();
                        world.platforms.add(world.potentialPlatforms.get(0));
                        world.potentialPlatforms.clear();

                    }else {
                        Platform platform = new Platform((int) (e.getX() - primaryStage.getScene().getRoot().getTranslateX()), (int) (e.getY() - primaryStage.getScene().getRoot().getTranslateY()), 20, 20);
                        platform.setColor(Color.GREEN);
                        world.potentialPlatforms.add(platform);
                        world.getWorldGroup().getChildren().add(platform.getR());
                    }
                }

                SaveLoadWorld.save(world, Main.worldName);

            }else if(e.getButton() == MouseButton.SECONDARY){
                try {
                    for (Platform p : world.platforms) {
                        if (CollisionDetection.collisionDetectionRectangle(e.getX() - primaryStage.getScene().getRoot().getTranslateX(), e.getY() - primaryStage.getScene().getRoot().getTranslateY(), 1, 1, p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
                            world.platforms.remove(p);
                            world.getWorldGroup().getChildren().remove(p.getR());
                        }
                    }
                }catch (ConcurrentModificationException exception){
                    System.out.println();
                }

                SaveLoadWorld.save(world, Main.worldName);
            }
        });

        primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, e ->{

            if(world.potentialPlatforms.size() > 0) {
                world.potentialPlatforms.get(0).setWidth((int)((e.getX() - world.potentialPlatforms.get(0).getX()) - primaryStage.getScene().getRoot().getTranslateX()));
                world.potentialPlatforms.get(0).setHeight((int)((e.getY() - world.potentialPlatforms.get(0).getY()) - primaryStage.getScene().getRoot().getTranslateY()));
            }else{

                for (Platform p : world.platforms) {
                    if(p.getColor() == Color.AQUA) {
                        p.getR().setTranslateX((int) (e.getX() - primaryStage.getScene().getRoot().getTranslateX() - p.getWidth()/2));
                        p.getR().setTranslateY((int) (e.getY() - primaryStage.getScene().getRoot().getTranslateY() - p.getHeight()/2));
                    }
                }
            }
        });
    }

}