package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mankomania.game.MankomaniaGame;

import java.util.ArrayList;


public class StockSelectionScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture background;
    private final SpriteBatch batch;
    private final Dialog shareSelectionDialog;
    private final ArrayList<TextureRegionDrawable> shareList;
    private final Skin skin;
    private ImageButton dryOilButton;
    private ImageButton hardSteelButton;
    private ImageButton shortCircuitButton;
    private TextButton resetButton;
    private TextButton readyButton;
    private boolean dialogDrawn;
    private static int dryOilCount;
    private static int hardSteelCount;
    private static int shortCircuitCount;
    private Label dryOilLabel;
    private Label hardSteelLabel;
    private Label shortCircuitLabel;
    private final InputMultiplexer inputMultiplexer;
    private final String dryOil = "DryOil";
    private final String hardSteel = "HardSteel";
    private final String shortCircuit = "ShortCircuit";

    public StockSelectionScreen(){
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        stage = new Stage();
        background = new Texture("background.jpg");
        batch = new SpriteBatch();
        shareList = new ArrayList<>();
        dialogDrawn = false;
        shareSelectionDialog = new Dialog("",skin, "alt");
        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }
        initImages();
    }

    private void drawImages(){
        Table tab = new Table();
        tab.align(Align.center);
        float scale = 0.7f;
        String title = "title";
        Label label = new Label("Select two shares", skin, title);
        shareSelectionDialog.setWidth(Gdx.graphics.getWidth() * scale);
        shareSelectionDialog.text(label).padTop(20f);

        dryOilButton = new ImageButton(shareList.get(0));
        hardSteelButton = new ImageButton(shareList.get(1));
        shortCircuitButton = new ImageButton(shareList.get(2));

        dryOilButton.addListener(dryOilListener());
        hardSteelButton.addListener(hardSteelListener());
        shortCircuitButton.addListener(shortCircuitListener());

        dryOilButton.addListener(changeListener());
        hardSteelButton.addListener(changeListener());
        shortCircuitButton.addListener(changeListener());

        tab.add(dryOilButton).pad(20);
        tab.add(hardSteelButton).pad(20);
        tab.add(shortCircuitButton).pad(20).row();
        tab.pad(30);

        dryOilLabel = new Label(dryOil + ": " + dryOilCount + "x", skin, title);
        hardSteelLabel = new Label(hardSteel + ": " + hardSteelCount + "x", skin, title);
        shortCircuitLabel = new Label(shortCircuit + ": " + shortCircuitCount + "x", skin, title);

        tab.add(dryOilLabel);
        tab.add(hardSteelLabel);
        tab.add(shortCircuitLabel).row();

        resetButton = new TextButton("Reset", skin);
        resetButton.getLabel().setFontScale(Gdx.graphics.getHeight()/450f);
        resetButton.addListener(resetListener());
        tab.add(resetButton).pad(30f);

        tab.add();

        readyButton = new TextButton("Ready", skin);
        readyButton.getLabel().setFontScale(Gdx.graphics.getHeight()/450f);
        readyButton.setTouchable(Touchable.disabled);
        readyButton.addListener(readyListener());
        tab.add(readyButton).pad(30f);

        shareSelectionDialog.getButtonTable().add(tab);
        shareSelectionDialog.setScale(scale);
        shareSelectionDialog.show(stage);

        shareSelectionDialog.setWidth(Gdx.graphics.getWidth() / scale);
        shareSelectionDialog.setY(1000);

        dialogDrawn = true;
    }



    private void initImages(){
        Texture share1 = new Texture(Gdx.files.internal("shares/dryOil.png"));
        Texture share2 = new Texture(Gdx.files.internal("shares/hardSteel.png"));
        Texture share3 = new Texture(Gdx.files.internal("shares/shortCircuit.png"));

        TextureRegionDrawable dryOil = new TextureRegionDrawable(share1);
        TextureRegionDrawable hardSteel = new TextureRegionDrawable(share2);
        TextureRegionDrawable shortCircuit = new TextureRegionDrawable(share3);

        shareList.add(dryOil);
        shareList.add(hardSteel);
        shareList.add(shortCircuit);
    }

    public ClickListener dryOilListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                shareAdd(dryOil);
                dryOilLabel.setText(dryOil + ": " + dryOilCount + "x");
            }
        };
    }

    public ClickListener hardSteelListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                shareAdd(hardSteel);
                hardSteelLabel.setText(hardSteel + ": " + hardSteelCount + "x");
            }
        };
    }

    public ClickListener shortCircuitListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                shareAdd(shortCircuit);
                shortCircuitLabel.setText(shortCircuit + ": " + shortCircuitCount + "x");
            }
        };
    }

    public ChangeListener changeListener(){
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if((dryOilCount+shortCircuitCount+hardSteelCount) == 1){
                    dryOilButton.setTouchable(Touchable.disabled);
                    shortCircuitButton.setTouchable(Touchable.disabled);
                    hardSteelButton.setTouchable(Touchable.disabled);
                    readyButton.setTouchable(Touchable.enabled);
                }
            }
        };
    }

    public ClickListener resetListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                dryOilCount = 0;
                hardSteelCount = 0;
                shortCircuitCount = 0;

                dryOilButton.setTouchable(Touchable.enabled);
                shortCircuitButton.setTouchable(Touchable.enabled);
                hardSteelButton.setTouchable(Touchable.enabled);
                readyButton.setTouchable(Touchable.disabled);

                dryOilLabel.setText(dryOil + ": " + dryOilCount + "x");
                shortCircuitLabel.setText(shortCircuit + ": " + shortCircuitCount + "x");
                hardSteelLabel.setText(hardSteel + ": " + hardSteelCount + "x");
            }
        };
    }

    public ClickListener readyListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                resetButton.setTouchable(Touchable.disabled);
                dispose();
            }
        };
    }

    private static void shareAdd(String shareName){
        switch (shareName){
            case "DryOil":
                dryOilCount++;
                break;

            case "ShortCircuit":
                shortCircuitCount++;
                break;

            case "HardSteel":
                hardSteelCount++;
                break;
        }
    }

    @Override
    public void render(float delta){
        super.render(delta);
        ScreenUtils.clear(1, 1, 1, 1);
        stage.act(delta);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        if (!dialogDrawn)
            drawImages();
        stage.draw();


    }

    @Override
    public void dispose() {
        inputMultiplexer.removeProcessor(stage);
        stage.dispose();
        MankomaniaGame.getInstance().setScreen(new GameScreen());
    }

    public static int getDryOilCount() {
        return dryOilCount;
    }

    public static int getHardSteelCount() {
        return hardSteelCount;
    }

    public static int getShortCircuitCount() {
        return shortCircuitCount;
    }
}

