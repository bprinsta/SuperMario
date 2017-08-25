package com.prinsta.marioclone.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.prinsta.marioclone.Sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        //checks if the fixtures are from Player.class.It has UserData label of "head"
        if (a.getUserData() == "head" || b.getUserData() == "head") {
            //checks and set the fixture of head
            Fixture head = a.getUserData() == "head" ? a : b;
            //check and set the fixture of the other object(the object the head is colliding with)
            Fixture object = head == a ? b : a;

            //check if we actually have an object and its the correct type
            //both the Brick and CoinBlock objects are instances of InteractiveTileObject
            if (object.getUserData() instanceof InteractiveTileObject) {
                ((InteractiveTileObject) object.getUserData()).headHit();
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}