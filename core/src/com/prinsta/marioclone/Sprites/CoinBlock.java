package com.prinsta.marioclone.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.prinsta.marioclone.MarioClone;
import com.prinsta.marioclone.Scenes.Hud;


public class CoinBlock extends InteractiveTileObject{

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public CoinBlock(World world, TiledMap map, Rectangle bounds)
    {
        super(world, map, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        this.setCategoryFilter(MarioClone.COIN_BIT);
    }

    @Override
    public void headHit() {
        // setCategoryFilter(MarioClone.DESTROYED_BIT);
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}
