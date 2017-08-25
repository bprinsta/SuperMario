package com.prinsta.marioclone.Screens;

// import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.prinsta.marioclone.MarioClone;
import com.prinsta.marioclone.Scenes.Hud;
import com.prinsta.marioclone.Sprites.Mario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.badlogic.gdx.Screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.prinsta.marioclone.Tools.B2WorldCreator;
import com.prinsta.marioclone.Tools.WorldContactListener;

public class PlayScreen implements Screen {

    private static final float FPS = 60f;
    private static final int GRAVITY = -10;

    private MarioClone game;
    private TextureAtlas atlas;

    //basic PlayScreen variables
    private OrthographicCamera camera;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TiledMap map;
    private OrthoCachedTiledMapRenderer renderer;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // Sprites
    private Mario mario;

    public PlayScreen(MarioClone game)
    {
        this.atlas = new TextureAtlas("Mario_and_Enemies.pack"); // look into LibGdx asset manager;
        this.game = game;
        camera = new OrthographicCamera();
        gamePort = new FitViewport(MarioClone.GAME_WIDTH/ MarioClone.PPM,
                MarioClone.GAME_HEIGHT/ MarioClone.PPM, camera);

        // Creates game HUD
        hud = new Hud(game.batch);

        // Loads tileMap and sets up map renderer
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthoCachedTiledMapRenderer(map, 1/ MarioClone.PPM);
        renderer.setBlending(true);

        camera.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        mario = new Mario(world, this);

        new B2WorldCreator(world, map);

        world.setContactListener(new WorldContactListener());
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        mario.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    private void handleInput()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && mario.b2Body.getLinearVelocity().y == 0)
        mario.jump();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mario.b2Body.getLinearVelocity().x <= 2)
            mario.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), mario.b2Body.getWorldCenter(), true);

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && mario.b2Body.getLinearVelocity().x >= -2)
            mario.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), mario.b2Body.getWorldCenter(), true);
    }

    private void update(float dt)
    {
        handleInput();

        world.step(1/FPS, 6, 2);

        mario.update(dt);
        hud.update(dt);

        camera.position.x = mario.b2Body.getPosition().x;

        // Update gameCamera with correct coordinates
        camera.update();
        renderer.setView(camera);
    }

    @Override
    public void resize(int width, int height)
    {
        gamePort.update(width, height);
    }

    @Override
    public void dispose()
    {
        game.batch.dispose();
        world.dispose();
        map.dispose();
        renderer.dispose();
        hud.dispose();
        b2dr.dispose();
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
