import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

class MainMenu {

    private Group menuGroup;

    MainMenu(World world, Pane root){
        menuGroup = new Group();
        Button playButton = new Button("Play");
        playButton.setTranslateX(Main.width/2);
        playButton.setTranslateY(Main.height/2);
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               startWorld(world, root);
            }
        });

        menuGroup.getChildren().add(playButton);
    }

    private void startWorld(World world, Pane root){

        SaveLoadWorld.load(Main.worldName, world);
        world.calculateCameraView(root);
        root.getChildren().add(world.getWorldGroup());

        menuGroup.setVisible(false);
        Main.gameState = 1;

        world.timeline.play();
    }

    Group getMenuGroup() {
        return menuGroup;
    }
}