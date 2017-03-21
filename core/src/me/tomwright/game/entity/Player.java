package me.tomwright.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.tomwright.game.TomsGame;
import me.tomwright.game.map.MapHelper;

import java.util.EnumMap;
import java.util.Map;

public class Player extends Sprite {

    private Vector3 velocity;

    private Vector3 position;

    private Vector2 renderPos;

    private float speed;

    private float gravity;

    private TiledMapTileLayer groundLayer, collisionLayer;

    private boolean isJumping;

    private Texture lookingDown, lookingUp, lookingRight, lookingLeft, lookingUpLeft, lookingUpRight, lookingDownLeft, lookingDownRight;

    private MovementDirection movementDirection;

    private Map<MovementDirection, TiledMapTile> surroundingTiles = new EnumMap<MovementDirection, TiledMapTile>(MovementDirection.class);
    private TiledMapTile currentTile;

    public Player(TiledMapTileLayer groundLayer) {
        lookingDown = new Texture("img/player/front.png");
        lookingUp = new Texture("img/player/back.png");
        lookingRight = new Texture("img/player/right.png");
        lookingLeft = new Texture("img/player/left.png");
        lookingDownLeft = new Texture("img/player/down_left.png");
        lookingDownRight = new Texture("img/player/down_right.png");
        lookingUpLeft = new Texture("img/player/up_left.png");
        lookingUpRight = new Texture("img/player/up_right.png");

        Sprite sprite = new Sprite(lookingDown);
        set(sprite);

        velocity = new Vector3(0, 0, 0);
        position = new Vector3(0, 0, 100);
        renderPos = new Vector2(0, 0);

        isJumping = false;

        this.groundLayer = groundLayer;
        this.collisionLayer = collisionLayer;
    }

    private void updateSurroundingTiles() {
        currentTile = MapHelper.getTileAt(position, groundLayer);
        surroundingTiles.put(MovementDirection.NORTH, MapHelper.getTileAt(position.x, position.y + TomsGame.TILE_HEIGHT, groundLayer));
        surroundingTiles.put(MovementDirection.SOUTH, MapHelper.getTileAt(position.x, position.y - TomsGame.TILE_HEIGHT, groundLayer));
        surroundingTiles.put(MovementDirection.EAST, MapHelper.getTileAt(position.x + TomsGame.TILE_WIDTH, position.y, groundLayer));
        surroundingTiles.put(MovementDirection.WEST, MapHelper.getTileAt(position.x - TomsGame.TILE_WIDTH, position.y, groundLayer));
        surroundingTiles.put(MovementDirection.NORTH_EAST, MapHelper.getTileAt(position.x + TomsGame.TILE_WIDTH, position.y + TomsGame.TILE_HEIGHT, groundLayer));
        surroundingTiles.put(MovementDirection.NORTH_WEST, MapHelper.getTileAt(position.x - TomsGame.TILE_WIDTH, position.y + TomsGame.TILE_HEIGHT, groundLayer));
        surroundingTiles.put(MovementDirection.SOUTH_EAST, MapHelper.getTileAt(position.x + TomsGame.TILE_WIDTH, position.y - TomsGame.TILE_HEIGHT, groundLayer));
        surroundingTiles.put(MovementDirection.SOUTH_WEST, MapHelper.getTileAt(position.x - TomsGame.TILE_WIDTH, position.y - TomsGame.TILE_HEIGHT, groundLayer));
    }

    public void update(float delta) {
        updateSurroundingTiles();
        updatePosition(delta);
        updateSurroundingTiles();
    }

    private void updatePosition(float delta) {
        handleGravity(delta);

        speed = 60 * 3f;
        gravity = 60 * 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = 60 * 10f;
        }

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

            float ground = MapHelper.getGroundLevelAtTile(currentTile);

            if (newPosition.z < ground) {
                isJumping = false;
                newPosition.z = ground;
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

        this.renderPos.x = renderPos.x;
        this.renderPos.y = renderPos.y;// - this.position.z;

        setX(this.renderPos.x);
        setY(this.renderPos.y);
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
                movingDownKeyPressed = (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)),
                jumpingKeyPressed = (Gdx.input.isKeyPressed(Input.Keys.SPACE));

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

        if (jumpingKeyPressed && ! isJumping) {
            isJumping = true;
            velocity.z = 13f;
        }

        this.movementDirection = MovementDirection.getFromVelocity(this.velocity);
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

        if (this.movementDirection != null) {
            switch (this.movementDirection) {
                case NORTH:
                    this.setTexture(this.lookingUpRight);
                    break;
                case SOUTH:
                    this.setTexture(this.lookingDownLeft);
                    break;
                case EAST:
                    this.setTexture(this.lookingDownRight);
                    break;
                case WEST:
                    this.setTexture(this.lookingUpLeft);
                    break;
                case NORTH_EAST:
                    this.setTexture(this.lookingRight);
                    break;
                case SOUTH_EAST:
                    this.setTexture(this.lookingDown);
                    break;
                case SOUTH_WEST:
                    this.setTexture(this.lookingLeft);
                    break;
                case NORTH_WEST:
                    this.setTexture(this.lookingUp);
                    break;
            }
        }

        highlightCurrentTile(batch, camera);

        drawShadow(batch, camera);

        super.draw(batch);
    }

    private void highlightCurrentTile(Batch batch, OrthographicCamera camera) {
        if (currentTile == null) {
            return;
        }

        float groundLevel = MapHelper.getGroundLevelAtTile(currentTile);

        Vector2[] tileCoords = MapHelper.positionToIsoTileXY(position.x, position.y, groundLevel);

        batch.end();

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setAutoShapeType(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.begin();
        shapeRenderer.setColor(new Color(1f, 0f, 0f, 1f));

        float[] vertices = {
                tileCoords[0].x, tileCoords[0].y,
                tileCoords[1].x, tileCoords[1].y,
                tileCoords[2].x, tileCoords[2].y,
                tileCoords[3].x, tileCoords[3].y,
                tileCoords[0].x, tileCoords[0].y,
        };

        shapeRenderer.polygon(vertices);
        shapeRenderer.end();
        shapeRenderer.dispose();

        batch.begin();
    }

    private void drawShadow(Batch batch, OrthographicCamera camera) {
        int x = (int) this.renderPos.x;
        int y = (int) (this.renderPos.y - this.position.z);

        if (currentTile != null) {
            float ground = MapHelper.getGroundLevelAtTile(currentTile);
            y += ground;
        }

        float size = getWidth();
        size -= (size * 0.002f * this.position.z);

        float   width = size,
                height = size - (size * 0.3f);

        x += ((getWidth() - width) / 2);
        y += ((getWidth() - height) / 2);

        batch.end();

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setAutoShapeType(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.5f));
        shapeRenderer.ellipse(x, y, width, height);
        shapeRenderer.end();
        shapeRenderer.dispose();

        batch.begin();
    }

    public void dispose() {
        if (lookingDown != null) {
            lookingDown.dispose();
        }
        if (lookingDownRight != null) {
            lookingDownRight.dispose();
        }
        if (lookingDownLeft != null) {
            lookingDownLeft.dispose();
        }
        if (lookingLeft != null) {
            lookingLeft.dispose();
        }
        if (lookingRight != null) {
            lookingRight.dispose();
        }
        if (lookingUp != null) {
            lookingUp.dispose();
        }
        if (lookingUpLeft != null) {
            lookingUpLeft.dispose();
        }
        if (lookingUpRight != null) {
            lookingUpRight.dispose();
        }
    }

    public Vector2 getRenderPos() {
        return renderPos;
    }

    public Vector3 getPosition() {
        return position;
    }
}
