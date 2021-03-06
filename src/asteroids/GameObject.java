package asteroids;

import math.Vector2d;

import java.awt.*;

public abstract class GameObject {
    public Vector2d s,v;
    public boolean isTarget;
    public boolean dead;
    public double r;

    protected GameObject(Vector2d s, Vector2d v) {
        this.s = new Vector2d(s);
        this.v = new Vector2d(v);
    }

    public abstract void update();
    public abstract void draw(Graphics2D g);
    public abstract GameObject copy();

    protected GameObject updateClone(GameObject copyObject) {
        copyObject.s = s.copy();
        copyObject.v = v.copy();
        copyObject.isTarget = isTarget;
        copyObject.dead = dead;
        copyObject.r = r;

        return copyObject;
    }

    public abstract boolean dead();

    public void hit() {
        dead  = true;
    }

    public double r() {
        return r;
    }

    public boolean wrappable() {
        // wrap objects by default
        return true;
    }
}
