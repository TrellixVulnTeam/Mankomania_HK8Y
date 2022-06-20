package horseRaceLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

import playerLogic.Player;

public class Horse extends Actor {
    private int movedSteps;
    private Texture horseTexture;
    private Player player;

    private static float horseHeight;
    private static float horseWidth;

    public Horse() {
        movedSteps = 0;
    }

    public Horse(float x, float y, Texture texture, Player p) {
        this.setX(x);
        this.setY(y);
        movedSteps = 0;
        horseTexture = texture;
        player = p;
        horseHeight = Gdx.graphics.getHeight() / 5.5f;
        horseWidth = horseHeight * 1.2f;
    }

    public Player getPlayer() {
        return player;
    }

    public void moveForward() {
        this.clearActions();
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(this.getX() + Gdx.graphics.getWidth() / 11.5f, this.getY());
        moveAction.setDuration(0.2f);
        this.addAction(moveAction);
        movedSteps++;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(horseTexture, this.getX(), this.getY(), horseWidth, horseHeight);
    }

    public int getMovedSteps() {
        return movedSteps;
    }
}
