package com.prinsta.marioclone.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.prinsta.marioclone.MarioClone;

public class Hud implements Disposable{

    // Style of Display Text
    private static final LabelStyle STYLE = new LabelStyle(new BitmapFont(), Color.WHITE);

    private static final int TOP_PADDING = 0;
    private static final int BOTTOM_PADDING = 170;
    public Stage stage;

    private Integer worldTimer;
    private float timeCount;
    private static Integer score;

    private Label countdownLabel;
    private static Label scoreLabel;


    public Hud(SpriteBatch sb)
    {
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        Viewport viewport = new FitViewport(MarioClone.GAME_WIDTH, MarioClone.GAME_HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), STYLE);
        scoreLabel = new Label(String.format("%06d", score), STYLE);
        Label timeLabel = new Label("TIME", STYLE);
        Label levelLabel = new Label("1-1", STYLE);
        Label worldLabel = new Label("WORLD", STYLE);
        Label marioLabel = new Label("MARIO", STYLE);

        table.add(marioLabel).expandX().padTop(TOP_PADDING);
        table.add(worldLabel).expandX().padTop(TOP_PADDING);
        table.add(timeLabel).expandX().padTop(TOP_PADDING);
        table.row();
        table.add(scoreLabel).expandX().padBottom(BOTTOM_PADDING);
        table.add(levelLabel).expandX().padBottom(BOTTOM_PADDING);
        table.add(countdownLabel).expand().padBottom(BOTTOM_PADDING);

        stage.addActor(table);
    }

    public void dispose()
    {
        stage.dispose();
    }

    public void update(float dt)
    {
        timeCount += dt;
        if (timeCount >= 1)
        {
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;

        }
    }

    public static void addScore(int value)
    {
        score += value;
        scoreLabel.setText(String.format("%06d", score));

    }

}
