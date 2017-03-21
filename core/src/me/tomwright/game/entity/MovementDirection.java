package me.tomwright.game.entity;

import com.badlogic.gdx.math.Vector3;

public enum MovementDirection {

    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST;

    public static MovementDirection getFromVelocity(Vector3 velocity) {
        if (velocity.x > 0 && velocity.y > 0) {
            return NORTH_EAST;
        } else if (velocity.x < 0 && velocity.y < 0) {
            return SOUTH_WEST;
        } else if (velocity.x > 0 && velocity.y < 0) {
            return SOUTH_EAST;
        } else if (velocity.x < 0 && velocity.y > 0) {
            return NORTH_WEST;
        } else if (velocity.x > 0 && velocity.y == 0) {
            return EAST;
        } else if (velocity.x < 0 && velocity.y == 0) {
            return WEST;
        } else if (velocity.x == 0 && velocity.y > 0) {
            return NORTH;
        } else if (velocity.x == 0 && velocity.y < 0) {
            return SOUTH;
        }

        return null;
    }

}
