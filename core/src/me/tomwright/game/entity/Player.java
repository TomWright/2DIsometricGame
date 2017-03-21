package me.tomwright.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.tomwright.game.map.MapHelper;

public class Player extends Sprite {

    private Vector3 velocity;

    private Vector3 position;

    private Vector2 renderPos;

    private float speed;

    private float gravity;

    private TiledMapTileLayer groundLayer, collisionLayer;

    public Player(Sprite sprite, TiledMapTileLayer groundLayer) {
        super(sprite);

        velocity = new Vector3(0, 0, 0);
        position = new Vector3(0, 0, 100);
        renderPos = new Vector2(0, 0);
        speed = 60 * 3f;
        gravity = 60 * 1f;

        this.groundLayer = groundLayer;
        this.collisionLayer = collisionLayer;
    }

    public void update(float delta) {
        updatePosition(delta);
    }

    private void updatePosition(float delta) {
        // Ensure the downwards velocity is correct
        handleGravity(delta);

        // Ensure X velocity is correct
        // Ensure Z velocity is correct
        handleMovementVelocity(delta);

        handleMovement();
    }

    /**
     * The velocity is correct.
     * Now act on the velocity.
     * Can the player move in the desired direction?
     * This method should update the position vector.
     */
    private void handleMovement() {
        TiledMapTile currentTile = MapHelper.getTileAt(position, groundLayer);
        MapProperties currentTileProperties = currentTile.getProperties();

        Vector3 newPosition = new Vector3(position.x, position.y, position.z);

        if (velocity.x > 0) {
            // Moving right
            newPosition.x += velocity.x;
        } else if (velocity.x < 0) {
            // Move left
            newPosition.x += velocity.x;
        }

        if (velocity.y > 0) {
            // Moving up
            newPosition.y += velocity.y;
        } else if (velocity.y < 0) {
            // Move down
            newPosition.y += velocity.y;
        }
        if (velocity.z > 0) {
            // Jumping
            newPosition.z += velocity.z;
        } else if (velocity.z < 0) {
            // Falling
            newPosition.z += velocity.z;

            if (currentTileProperties.containsKey("ground_level")) {
                Object groundLevel = currentTileProperties.get("ground_level");
                float ground = (Float.parseFloat((String) groundLevel));
                if (newPosition.z < ground) {
                    newPosition.z = ground;
                }
            }
        }
        if (newPosition.z < 0) {
            newPosition.z = 0;
        }

        boolean canMove = true;
        if (canMove) {
            this.position.x = newPosition.x;
            this.position.y = newPosition.y;
            this.position.z = newPosition.z;
        }
    }

    private void setXAndYAccordingToPosition(OrthographicCamera camera) {
        Vector2 renderPos = MapHelper.positionToIsoXY(this.position);

        setX(renderPos.x);
        setY(renderPos.y);

        this.renderPos.x = renderPos.x;
        this.renderPos.y = renderPos.y;// - this.position.z;
    }

    /**
     * Is the Player moving? Adjust the velocity accordingly.
     * @param delta Time since last render
     */
    private void handleMovementVelocity(float delta) {
        float deltaSpeed = speed * delta;

        boolean movingRightKeyPressed = (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)),
                movingLeftKeyPressed = (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)),
                movingUpKeyPressed = (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)),
                movingDownKeyPressed = (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN));

        // Ensure X velocity is correct
        if (movingRightKeyPressed) {
            velocity.x = deltaSpeed;
        } else if (movingLeftKeyPressed) {
            velocity.x = -deltaSpeed;
        } else if (velocity.x != 0) {
            // Left/Right movement keys are not pressed.
            // The left/right velocity is not 0 though.
            // Let's gradually slow down the player.
            if (Math.abs(velocity.x) <= (deltaSpeed / 2)) {
                // The next slow down will stop the player anyway. Let's full stop them.
                velocity.x = 0;
            } else if (velocity.x > 0) {
                velocity.x -= deltaSpeed / 2;
            } else if (velocity.x < 0) {
                velocity.x += deltaSpeed / 2;
            }
        }
        if (velocity.x > speed) {
            velocity.x = speed;
        } else if (0 - velocity.x < -speed) {
            velocity.x = -speed;
        }

        // Ensure Y velocity is correct
        if (movingUpKeyPressed) {
            velocity.y = deltaSpeed;
        } else if (movingDownKeyPressed) {
            velocity.y = -deltaSpeed;
        } else if (velocity.y != 0) {
            // Up/Down movement keys are not pressed.
            // The up/down velocity is not 0 though.
            // Let's gradually slow down the player.
            if (Math.abs(velocity.y) <= (deltaSpeed / 2)) {
                // The next slow down will stop the player anyway. Let's full stop them.
                velocity.y = 0;
            } else if (velocity.y > 0) {
                velocity.y -= deltaSpeed / 2;
            } else if (velocity.y < 0) {
                velocity.y += deltaSpeed / 2;
            }
        }
        if (velocity.y > speed) {
            velocity.y = speed;
        } else if (0 - velocity.y < -speed) {
            velocity.y = -speed;
        }
    }

    private void handleGravity(float delta) {
        float deltaGravity = gravity * delta;
        velocity.z -= deltaGravity;

        if (velocity.z > speed) {
            velocity.z = speed;
        } else if (velocity.z < (0 - speed)) {
            velocity.z = -speed;
        }
    }

    public void draw(Batch batch, OrthographicCamera camera) {
        setXAndYAccordingToPosition(camera);
        super.draw(batch);
    }

    public void dispose() {
        getTexture().dispose();
    }

    public Vector2 getRenderPos() {
        return renderPos;
    }
}
