package me.tomwright.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.tomwright.game.TomsGame;

public class MapHelper {

    public static int getXTile (Vector3 position) {
        return getXTile(position.x);
    }

    public static int getXTile (float x) {
        return (int) (x / TomsGame.TILE_WIDTH);
    }

    public static int getYTile (Vector3 position) {
        return getYTile(position.y);
    }

    public static int getYTile (float y) {
        return (int) (y / TomsGame.TILE_HEIGHT);
    }

    public static TiledMapTile getTileAt (float x, float y, TiledMapTileLayer layer) {
        int xTile = getXTile(x);
        int yTile = getYTile(y);
        TiledMapTileLayer.Cell cell = layer.getCell(xTile, yTile);
        if (cell == null) {
            return null;
        }
        return cell.getTile();
    }

    public static TiledMapTile getTileAt (Vector3 position, TiledMapTileLayer layer) {
        return getTileAt(position.x, position.y, layer);
    }

    public static Vector2 positionToIsoXY (float x, float y, float z) {
        Vector2 renderPos = new Vector2(0, 0);

        float xReduced = x / 100;
        float yReduced = y / 100;

        renderPos.x = ((xReduced * TomsGame.TILE_WIDTH) / 2);
        float xNeedsY = -((xReduced * TomsGame.TILE_HEIGHT) / 2);

        renderPos.y = ((yReduced * TomsGame.TILE_HEIGHT) / 2);
        float yNeedsX = ((yReduced * TomsGame.TILE_WIDTH) / 2);

        renderPos.x += yNeedsX;
        renderPos.y += xNeedsY;

        renderPos.y += z;

        return renderPos;
    }

    public static Vector2 positionToIsoXY (Vector3 position) {
        return positionToIsoXY(position.x, position.y, position.z);
    }

    public static Vector2 positionToIsoXY (float x, float y) {
        return positionToIsoXY(x, y, 0);
    }

    public static Vector2 positionToIsoXY (Vector2 position) {
        return positionToIsoXY(position.x, position.y);
    }

}
