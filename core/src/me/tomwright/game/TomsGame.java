package me.tomwright.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import me.tomwright.game.screen.PlayScreen;
import me.tomwright.game.screen.Screen;
import me.tomwright.game.screen.ScreenManager;

public class TomsGame extends ApplicationAdapter {

	public static final int TILE_WIDTH = 100;
	public static final int TILE_HEIGHT = 50;

	public TomsGame () {
		Screen screen = new PlayScreen();
		ScreenManager.setCurrentScreen(screen);

	}

	@Override
	public void create () {
		if (ScreenManager.hasCurrentScreen()) {
			ScreenManager.getCurrentScreen().create();
		}
	}

	private void update (float deltaTime) {
		if (ScreenManager.hasCurrentScreen()) {
			ScreenManager.getCurrentScreen().update(deltaTime);
		}
	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (ScreenManager.hasCurrentScreen()) {
			ScreenManager.getCurrentScreen().render();
		}
	}
	
	@Override
	public void dispose () {
		if (ScreenManager.hasCurrentScreen()) {
			ScreenManager.getCurrentScreen().dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		if (ScreenManager.hasCurrentScreen()) {
			ScreenManager.getCurrentScreen().resize(width, height);
		}
	}

	@Override
	public void pause() {
		if (ScreenManager.hasCurrentScreen()) {
			ScreenManager.getCurrentScreen().pause();
		}
	}

	@Override
	public void resume() {
		if (ScreenManager.hasCurrentScreen()) {
			ScreenManager.getCurrentScreen().resume();
		}
	}

}
