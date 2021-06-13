import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     *
     * 2D Jump and Run with in-Game map editor
     *
     * Contorls:
     *
     * Spacebar: Jump / Double jump after collision with wall
     * E: Change game mode to editor
     *
     * Editor mode:
     *
     * Use ASDW to fly
     *
     * press E to change game mode back to normal
     *
     * Left click on empty space: Place new Platform
     *
     * When platform is green: Click on green cube and drag to change size
     *
     * Left click on platform: Move platform
     *
     * When platform is blue: Click and drag to change it´s position
     *
     * Right click on platform to remove it
     *
     *
     */

    final static int width = 800, height = 600, gravity = 900, jumpHeight = 675, playerSpeed = 400, maxPlayerOffsetToCenter = 200;
    final static double jumpBoost = 0.5, scaling = 1;

    final static Color platformColor = Color.GRAY, playerColor = Color.BLACK, backgroundColor = Color.WHITE;

    static String worldName = "Default";

    private World world;
    static int gameState = 0;
    static double relativePlayerPositionX = 0, relativePlayerPositionY = 0;

    private Parent createContend(){

        Pane root = new Pane();
        world = new World(root, worldName);
        MainMenu menu = new MainMenu(world, root);

        root.getChildren().add(menu.getMenuGroup());

        root.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        return root;
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setScene(new Scene(createContend()));
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.getScene().setFill(backgroundColor);
        Controls.addControls(primaryStage, world);

        if(scaling != 1) {
            primaryStage.getScene().getRoot().setScaleX(scaling);
            primaryStage.getScene().getRoot().setScaleY(scaling);
        }

        primaryStage.show();
    }


}