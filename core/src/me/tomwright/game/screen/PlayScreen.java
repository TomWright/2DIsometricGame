package me.tomwright.game.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import me.tomwright.game.entity.Player;

public class PlayScreen extends Screen {

    private IsometricTiledMapRenderer renderer;
    private TiledMap map;
    private OrthographicCamera camera;

    private Player player;

    private int tick = 0;

    @Override
    public void create() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/iso_map.tmx");

        camera = new OrthographicCamera();

        float unitScale = 1f;
        renderer = new IsometricTiledMapRenderer(map, unitScale);

        player = new Player(new Sprite(new Texture("img/player.png")), (TiledMapTileLayer) map.getLayers().get(0));
    }

    @Override
    public void update(float deltaTime) {
        player.update(deltaTime);
    }

    @Override
    public void render() {
        renderer.setView(camera);
        // You can pass  int[] to determine which layers to render.
        renderer.render();

//        tick++;
//        if (tick > 400) {
//            tick = 0;
//        }
//        if (tick <= 100) {
//            camera.position.x++;
//        } else if (tick <= 200) {
//            camera.position.y--;
//        } else if (tick <= 300) {
//            camera.position.x--;
//        } else if (tick <= 400) {
//            camera.position.y++;
//        }
//        camera.update();

        Batch batch = renderer.getBatch();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch, camera);
        batch.end();
    }

    @Override
    public void dispose() {
        if (map != null) {
            map.dispose();
        }
        if (renderer != null) {
            renderer.dispose();
        }
        if (player != null) {
            player.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

}
