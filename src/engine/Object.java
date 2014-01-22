package engine;

import game.Main;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

public class Object
{	
	// Setup static data
	private Sprite sprite;
	private boolean movable; // Betyr dette om den er bevegelig?
	
	// Kan ikke vi erstatte de 3 her av "Position" object?
	private Vector3f position;
	private Vector2f size;
	private float rotation;
	
	// Setup movable data
	private float mass;
	private Vector3f speed; // Jeg re-definerer hastighet som en Vector3f med x,y, og rotasjon.
	private Vector3f force;
	// Jeg tror ikke vi har noe hastighet i z, de blir statiske per object. Dersom det er hastighet i z, må vi re-allokere objektet i arrayen.
	// Er det noe poeng i å ha dZ?
	// Det vil se merkelig ut, og vi må ha en "isOrdered" sjekk per iterasjon, på 60 fps
	// Ok!
	
	// Setup static object
	public Object(String image_path, Vector3f position, Vector2f size, float rotation) throws IOException
	{
		this.movable = false;
		this.sprite = checkTextures(image_path);
		this.position = position;
		this.size = size;
		this.rotation = rotation;
	}
	
	public Object(String image_path, Vector3f position, Vector2f size, float rotation) throws IOException
	{
		this.movable = false;
		this.sprite = checkTextures(image_path);
		this.position = position; 
		this.size = size;
		this.rotation = rotation;
	}
	
	// Setup movable object
	public Object(String image_path, Vector3f position, Vector2f size, float rotation, float mass, Vector3f speed) throws IOException {
		this.movable = true;
		this.sprite = checkTextures(image_path);
		this.position = position;
		this.size = size;
		this.rotation = rotation;
		this.mass = mass;
		this.speed = speed;
		
		// Vi har ingen force argument, skal det vaere sånn???
		// Ok!
		this.force = new Vector3f(0.f, 0.f, 0.f);
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public boolean isMovable() {
		return movable;
	}

	
	// Vi kan si: getPosition(), og i koden skrive: getPosition().x for eksempel? SKRIV
	// Skal jeg slette de her da?
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector2f getSize()
	{
		return size;
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
			if (Main.textures.get(i) == tex) {
				return new Sprite (Main.textures.get(i));
			}
		}
		
		Main.textures.add(tex);
		return new Sprite(Main.textures.get(Main.textures.size() - 1));
	}
	
	// Update object position and speed
	public void update() {
		// Update speed
		speed.x += force.x / mass;
		speed.y += force.y / mass;
		speed.z += force.z / mass;
			
		// Reset forces
		force.x = 0;
		force.y = 0;
		force.z = 0;
		
		// Sant det... Ja...
		
		// Update position
		position.x += force.x;
		position.y += force.y;
		position.z += force.z;
		
		// Ja, det er kjempedyrt, men, det går ikke å assigne til x eller y av denne klassen, vi burde lage vår egen... Men tom for strem :((((
		
		// Update rotation
		rotation += speed.z;
		
		while (rotation > 2*Math.PI) {
			rotation -= 2*Math.PI;
		}
		
		while (rotation < 0) {
			rotation += 2*Math.PI;
		}
	}
}
