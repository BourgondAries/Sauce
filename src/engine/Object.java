package engine;

import game.Main;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;

public class Object {
	// Setup static data
	private Sprite sprite;
	private boolean dynamic;
	private float x;
	private float y;
	private float z;
	private float width;
	private float height;
	private float rotation;
	
	// Setup dynamic data
	private float mass;
	private float speed_x;
	private float speed_y;
	private float speed_cv;
	private float force_x;
	private float force_y;
	private float force_cv;
	
	// Setup static object
	public Object(String image_path, float x, float y, float z, float width, float height, float rotation) throws IOException {
		this.dynamic = false;
		this.sprite = checkTextures(image_path);
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
	}
	
	// Setup dynamic object
	public Object(String image_path, float x, float y, float z, float width, float height, float rotation, float mass, float speed_x, float speed_y, float speed_cv) throws IOException {
		this.dynamic = true;
		this.sprite = checkTextures(image_path);
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.mass = mass;
		this.speed_x = speed_x;
		this.speed_y = speed_y;
		this.speed_cv = speed_cv;
		this.force_x = 0;
		this.force_y = 0;
		this.force_cv = 0;
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getRotation() {
		return rotation;
	}

	public float getMass() {
		return mass;
	}

	public void addForceX(float force_x) {
		this.force_x += force_x;
	}

	public void addForceY(float force_y) {
		this.force_y += force_y;
	}

	public void addForceCV(float force_cv) {
		this.force_cv += force_cv;
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	// Check if image already exists in memory
	private Sprite checkTextures(String image_path) throws IOException {
		Texture tex = new Texture();
		tex.loadFromFile(Paths.get(image_path));
		
		for (int i = 0; i < Main.textures.size(); i++) { // lag ferdig PathedTextures=to arrays, en med pathes til bildene
			if (Main.textures.get(i)==tex) {
				return new Sprite (Main.textures.get(i));
			}
		}
		
		Main.textures.add(tex);
		return new Sprite(Main.textures.get(Main.textures.size()-1));
	}
	
	// Update object position and speed
	public void update() {
		// Update speed
		speed_x += force_x/mass;
		speed_y += force_y/mass;
		speed_cv += force_cv/mass;
		
		// Reset forces
		force_x = 0;
		force_y = 0;
		force_cv = 0;
		
		// Update position
		x += speed_x;
		y += speed_y;
		
		// Update rotation
		rotation += speed_cv;
		
		while (rotation > 2*Math.PI) {
			rotation -= 2*Math.PI;
		}
		
		while (rotation < 0) {
			rotation += 2*Math.PI;
		}
	}
}
