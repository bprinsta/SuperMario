package com.prinsta.marioclone.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.prinsta.marioclone.MarioClone;
import com.prinsta.marioclone.Sprites.Brick;
import com.prinsta.marioclone.Sprites.CoinBlock;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map)
    {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Create fixtures for all necessary layers
        for (int i = 2; i <= 3; i++)
        {
            for (MapObject object : map.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                bdef.type = BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioClone.PPM, (rect.getY() + rect.getHeight() / 2) / MarioClone.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / MarioClone.PPM, rect.getHeight() / 2 / MarioClone.PPM);
                fdef.shape = shape;
                body.createFixture(fdef);
            }
        }

        // Creates fixtures & bodies for coins
        for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CoinBlock(world, map, rect);
        }

        // Creates fixtures & bodies for bricks
        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(world, map, rect);
        }

      /*
        // Creates fixtures & bodies for Ground
        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ MarioClone.PPM,
                    (rect.getY() + rect.getHeight()/2)/ MarioClone.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/ MarioClone.PPM, rect.getHeight()/2/ MarioClone.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // Creates fixtures & bodies for pipes
        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ MarioClone.PPM,
                    (rect.getY() + rect.getHeight()/2)/ MarioClone.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/ MarioClone.PPM, rect.getHeight()/2/ MarioClone.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        */
    }
}
