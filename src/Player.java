import javafx.scene.paint.Color;

class Player extends GameObject {

    double dx,dy, jumpBoost;
    boolean grounded = false, canJump = false;
    private boolean alteredVelocityY = false, alteredVelocityX = false;
    Platform lastTouchedPlatform;

    Player(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    void update(double elapsedTime) {

        if (Main.gameState == 1) {

            if(!alteredVelocityY) {
                if (!grounded)
                    dy += elapsedTime * Main.gravity;

                dy += jumpBoost * elapsedTime * dy;

            }else{
                alteredVelocityY = false;
            }
        }

        super.getR().setTranslateY(super.getR().getTranslateY() + elapsedTime * dy);
        super.getR().setTranslateX(super.getR().getTranslateX() + elapsedTime * dx);


    }

    void alterVelocityY(double newVelo, double elapsedTime){
        alteredVelocityY = true;

        if(dy > 0)
            getR().setTranslateY(getR().getTranslateY() + newVelo + 5);
        else if(dy < 0)
            getR().setTranslateY(getR().getTranslateY() - newVelo - 5);

    }

    void alterVelocityX(double newVelo, double elapsedTime){
        alteredVelocityX = true;

        if(dx > 0)
            getR().setTranslateX(getR().getTranslateX() + newVelo + 5);
        else if(dx < 0)
            getR().setTranslateX(getR().getTranslateX() - (newVelo + 5));

        dx = -dx;
    }

}