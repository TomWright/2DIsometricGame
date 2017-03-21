package me.tomwright.game.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

        float lerp = 2f;
        Vector3 position = camera.position;
        Vector2 renderPos = player.getRenderPos();

        position.x += (renderPos.x - position.x) * lerp * deltaTime;
        position.y += (renderPos.y - position.y + 40) * lerp * deltaTime;

        camera.update();
    }

    @Override
    public void render(float deltaTime) {
        renderer.setView(camera);
        // You can pass  int[] to determine which layers to render.
        renderer.render();

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
