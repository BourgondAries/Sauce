package engine;

import java.io.IOException;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector3f;

public class DynamicObject extends StaticObject {
	
	// Setup movement
	protected float mass;
	protected Vector3f speed;
	protected float speed_cv;
	protected Vector3f force;
	protected float force_cv;

	public DynamicObject(String image_path, Vector3f position, Vector2f size, float rotation_cv, float mass, Vector3f speed, float speed_cv) throws IOException, IllegalArgumentException {
		super(image_path, position, size, rotation_cv);
		if (mass<=0) throw new IllegalArgumentException("Mass cannot be less than or equal to zero"); //IllegalArgumentException, IllegalStateEx, NumberFormatEx, NullPointerEx, IndexOutOfBoundsEx, StringIndexOutOfBoundsEx, ArrayIndexOutOfBoundsEx, RuntimeEx
		this.mass = mass;
		this.speed = speed;
		this.speed_cv = speed_cv;
		this.force = Vector3f.ZERO;
		this.force_cv = 0;
	}
	
	public float getMass() {
		return mass;
	}

	public void addForce(Vector3f force, float force_cv) {
		this.force = Vector3f.add(this.force, force);
		this.force_cv += force_cv;
	}

	// Update object position and speed
	public void update() {
		
		// Update speed
		speed = Vector3f.add(speed, Vector3f.div(force, mass));
		speed_cv = force_cv/mass;
		
		// Reset forces
		force = Vector3f.ZERO;
		force_cv = 0;
		
		// Update position
		position = Vector3f.add(position, speed);
		
		// Update rotation
		rotation_cv += speed_cv;
		
		while (rotation_cv > 2*Math.PI) {
			rotation_cv -= 2*Math.PI;
		}
		
		while (rotation_cv < 0) {
			rotation_cv += 2*Math.PI;
		}
	}
}
