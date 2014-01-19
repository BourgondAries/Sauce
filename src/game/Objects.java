package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;

public class Objects {
	// Setup static data
	public Sprite sprite;
	public boolean dynamic;
	public float x;
	public float y;
	public float width;
	public float height;
	public float rotation;
	
	// Setup dynamic data
	public float mass;
	public float speed_x;
	public float speed_y;
	public float speed_cv;
	public float force_x;
	public float force_y;
	public float moment_cv;
	
	// Setup static object
	public Objects(String image_path, float x, float y, float width, float height, float rotation) throws IOException {
		this.dynamic = false;
		this.sprite = checkTextures(image_path);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
	}
	
	// Setup dynamic object
	public Objects(String image_path, float x, float y, float width, float height, float rotation, float mass, float speed_x, float speed_y, float speed_cv) throws IOException {
		this.dynamic = true;
		this.sprite = checkTextures(image_path);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.mass = mass;
		this.speed_x = speed_x;
		this.speed_y = speed_y;
		this.speed_cv = speed_cv;
		this.force_x = 0;
		this.force_y = 0;
		this.moment_cv = 0;
	}
	
	// Check if image already exists in memory
	private Sprite checkTextures(String image_path) throws IOException {
		Texture tex = new Texture();
		tex.loadFromFile(Paths.get(image_path));
		
		for (int i = 0; i < Main.textures.size(); i++) {
			if (Main.textures.get(i) == tex) {
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
		speed_cv += moment_cv/mass;
		
		// Reset forces
		force_x = 0;
		force_y = 0;
		moment_cv = 0;
		
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
