package com.prinsta.marioclone.Sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.math.Vector2;
import com.prinsta.marioclone.MarioClone;
import com.prinsta.marioclone.Screens.PlayScreen;
import com.badlogic.gdx.utils.Array;

public class Mario extends Sprite {

    public enum State{FALLING, JUMPING, STANDING, RUNNING};
    public State previousState;
    public State currentState;
    public World world;
    public Body b2Body;

    private TextureRegion marioStand;
    private Animation<TextureRegion> marioRun, marioJump;
    private float stateTimer;
    private boolean runningRight;

    public Mario(World world, PlayScreen screen)
    {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(getTexture(),i * 16, 11, 16, 16));

        marioRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 6; i++)
            frames.add(new TextureRegion(getTexture(),i * 16, 11, 16, 16));
        marioJump = new Animation<TextureRegion>(0.1f, frames);


        // Gets Static Mario sprite and attaches it to Box2D body
        marioStand = new TextureRegion(getTexture(),1, 11, 16, 16);
        defineMario();
        setBounds(0,0,16/ MarioClone.PPM, 16 / MarioClone.PPM);
        setRegion(marioStand);
    }

    private void defineMario()
    {
        BodyDef bdef = new BodyDef();
        bdef.position.set(64/ MarioClone.PPM , 64/ MarioClone.PPM);
        bdef.type = BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        fdef.filter.categoryBits = MarioClone.MARIO_BIT;
        fdef.filter.maskBits = MarioClone.DEFAULT_BIT | MarioClone.COIN_BIT | MarioClone.BRICK_BIT;
        CircleShape circle = new CircleShape();
        circle.setRadius(6/ MarioClone.PPM);

        fdef.shape = circle;
        b2Body.createFixture(fdef);

        // creates fixture for Mario's head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioClone.PPM, 7 / MarioClone.PPM),
                new Vector2(2 / MarioClone.PPM, 7 / MarioClone.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2Body.createFixture(fdef).setUserData("head");

        // create feet so mario doesnt get hung up on individual tiles
        //FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / MarioClone.PPM, -6 / MarioClone.PPM),
                new Vector2(-2 / MarioClone.PPM, -6 / MarioClone.PPM));
        fdef.shape = feet;
        fdef.isSensor = false;
        b2Body.createFixture(fdef).setUserData("feet");
    }

    public void update(float dt)
    {
        setPosition(b2Body.getPosition().x - getWidth()/2, b2Body.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;

        switch(currentState)
        {
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if ((b2Body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2Body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt: 0;
        previousState = currentState;
        return region;
    }

    private State getState()
    {
        if (b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0) && (previousState == State.FALLING))
            return State.JUMPING;
        else if (b2Body.getLinearVelocity().y < 0) return State.FALLING;
        else if (b2Body.getLinearVelocity().x != 0) return State.RUNNING;
        else return State.STANDING;

    }

    public void jump() {
        if (currentState != State.JUMPING) {
            b2Body.applyLinearImpulse(new Vector2(0, 4f), b2Body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
    }

}
