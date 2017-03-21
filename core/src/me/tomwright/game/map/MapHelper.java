package me.tomwright.game.map;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.tomwright.game.TomsGame;

public class MapHelper {

    public static float getGroundLevelAtTile(TiledMapTile tile) {
        float result = 0;

        MapProperties properties = null;
        if (tile != null) {
            properties = tile.getProperties();
            if (properties != null && properties.containsKey("ground_level")) {
                Object groundLevel = properties.get("ground_level");
                result = (Float.parseFloat((String) groundLevel));
            }
        }

        return result;
    }

    public static int getXTile (Vector3 position) {
        return getXTile(position.x);
    }

    public static int getXTile (float x) {
        return (int) Math.floor(x / TomsGame.TILE_WIDTH);
    }

    public static int getYTile (Vector3 position) {
        return getYTile(position.y);
    }

    public static int getYTile (float y) {
        return (int) Math.floor(y / TomsGame.TILE_HEIGHT);
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

        renderPos.x = (float) Math.floor(renderPos.x);
        renderPos.y = (float) Math.floor(renderPos.y);

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

    public static Vector3[] positionToTileXY (float x, float y, float z) {
        Vector3 bottomLeftPos = new Vector3(x - (x % 100), y - (y % 100), z),
                topLeftPos = new Vector3(bottomLeftPos.x, bottomLeftPos.y + 100, z),
                bottomRightPos = new Vector3(bottomLeftPos.x + 100, bottomLeftPos.y, z),
                topRightPos = new Vector3(bottomRightPos.x, topLeftPos.y, z);
        return new Vector3[] {bottomLeftPos, topLeftPos, topRightPos, bottomRightPos};
    }

    public static Vector2[] positionToIsoTileXY(float x, float y, float z) {
        Vector3[] coords = positionToTileXY(x, y, z);
        return new Vector2[] {
                MapHelper.positionToIsoXY(coords[0]),
                MapHelper.positionToIsoXY(coords[1]),
                MapHelper.positionToIsoXY(coords[2]),
                MapHelper.positionToIsoXY(coords[3])
        };
    }

}
