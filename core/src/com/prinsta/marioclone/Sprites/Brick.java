package com.prinsta.marioclone.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.prinsta.marioclone.MarioClone;
import com.prinsta.marioclone.Scenes.Hud;

public class Brick extends InteractiveTileObject {

    public Brick(World world, TiledMap map, Rectangle bounds)
    {
        super(world, map, bounds);
        fixture.setUserData(this);
        this.setCategoryFilter(MarioClone.BRICK_BIT);
    }

    @Override
    public void headHit() {
        setCategoryFilter(MarioClone.DESTROYED_BIT);
        getCell().setTile(null);
        fixture = null;
        Hud.addScore(200);
    }
}
