package battle;

import asteroids.Action;
import asteroids.GameObject;
import asteroids.GameState;
import asteroids.Missile;
import math.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static asteroids.Constants.*;

public class NeuroShip extends GameObject {

    // define the shape of the ship
    static int[] xp = {-2, 0, 2, 0};
    static int[] yp = {2, -2, 2, 0};

    // this is the thrust poly that will be drawn when the ship
    // is thrusting
    static int[] xpThrust = {-2, 0, 2, 0};
    static int[] ypThrust = {2, 3, 2, 0};
    public static double scale = 5;

    // define how quickly the ship will rotate
    static double steerStep = 10 * Math.PI / 180;

    // this is the friction that makes the ship slow down over time
    static double loss = 0.995;

    double releaseVelocity = 0;
    double minVelocity = 2;
    public static double maxRelease = 10;
    Color color = Color.white;
    boolean thrusting = false;

    static double gravity = 0.0;

    public Action action;

    // position and velocity
    public Vector2d d;


    public NeuroShip(Vector2d s, Vector2d v, Vector2d d) {
        super(new Vector2d(s), new Vector2d(v));
        this.d = new Vector2d(d);
    }

    public NeuroShip copy() {
        NeuroShip ship = new NeuroShip(s, v, d);
        ship.action = new Action(action);
        ship.releaseVelocity = releaseVelocity;
        return ship;
    }

    public double r() {
        return scale * 2.4;
    }

//    public Ship() {
//        super(new Vector2d(), new Vector2d());
//        d = new Vector2d(0, -1);
//    }
//

    public void reset() {
        s.set(width / 2, height / 2);
        v.zero();
        d.set(0, -1);
        dead = false;
        // System.out.println("Reset the ship ");
    }

    public void update() {
        update(action);
    }

    public NeuroShip update(Action action) {

        // what if this is always on?

        // action has fields to specify thrust, turn and shooting

        // action.thrust = 1;

        if (action.thrust > 0) {
            thrusting = true;
        } else {
            thrusting = false;
        }


        d.rotate(action.turn * steerStep);
        v.add(d, action.thrust * t * 0.3 / 2);
        v.y += gravity;
        // v.x = 0.5;
        v.mul(loss);
        s.add(v);
        // now create a missile if necessary
        // if the release velocity is zero
        releaseVelocity += 1.0;
        if (action.shoot) {
            tryMissileLaunch();
        } else {
        }
        return this;
    }

    private void tryMissileLaunch() {
        // System.out.println("Trying a missile launch");
        if (releaseVelocity > maxRelease) {
            releaseVelocity = Math.max(releaseVelocity, missileMinVelocity * 2);
            Missile m = new Missile(s, new Vector2d(0, 0));
            releaseVelocity = Math.min(releaseVelocity, maxRelease);
            m.v.add(d, releaseVelocity);
            // make it clear the ship
            m.s.add(m.v, (r() + missileRadius) * 1.5 / m.v.mag());
            releaseVelocity = 0;
            // System.out.println("Fired: " + m);
            // sounds.fire();
        } else {
            // System.out.println("Failed!");
        }
    }

    public String toString() {
        return s + "\t " + v;
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(s.x, s.y);
        double rot = Math.atan2(d.y, d.x) + Math.PI / 2;
        g.rotate(rot);
        g.scale(scale, scale);
        g.setColor(color);
        g.fillPolygon(xp, yp, xp.length);
        if (thrusting) {
            g.setColor(Color.red);
            g.fillPolygon(xpThrust, ypThrust, xpThrust.length);
        }
        g.setTransform(at);
    }

    public void hit() {
        // super.hit();
        // System.out.println("Ship destroyed");
        dead = true;
        // sounds.play(sounds.bangLarge);
    }

    public boolean dead() {
        return dead;
    }


}
