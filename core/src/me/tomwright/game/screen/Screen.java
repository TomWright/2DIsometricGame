package me.tomwright.game.screen;

public abstract class Screen {

    public abstract void create ();

    public abstract void update (float deltaTime);

    public abstract void render (float deltaTime);

    public abstract void dispose ();

    public abstract void resize(int width, int height);

    public abstract void pause ();

    public abstract void resume ();

}
