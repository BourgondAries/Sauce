package engine;

import java.io.IOException;

public class DynamicObject extends StaticObject {
	
	// Setup movement
	protected float mass;
	protected XYZRAxes speed;
	protected XYZRAxes force;

	public DynamicObject(String image_path, XYZRAxes position, float mass) throws IOException, IllegalArgumentException {
		
		super(image_path, position);
		
		if (mass<=0) throw new IllegalArgumentException("Mass cannot be less than or equal to zero"); //IllegalArgumentException, IllegalStateEx, NumberFormatEx, NullPointerEx, IndexOutOfBoundsEx, StringIndexOutOfBoundsEx, ArrayIndexOutOfBoundsEx, RuntimeEx
		
		this.mass = mass;
		this.speed = new XYZRAxes(0,0,0,0);
		force = new XYZRAxes(0,0,0,0);
	}
	
	// Mass
	public float getMass() {
		return mass;
	}
	
	public void setMass(float mass) throws IllegalArgumentException {
		if (mass<=0) throw new IllegalArgumentException("Mass cannot be less than or equal to zero");
		this.mass = mass;
	}
	
	// Speed
	public void setSpeed(XYZRAxes speed) {
		this.speed.setX(speed.getX());
		this.speed.setY(speed.getY());
		this.speed.setZ(speed.getZ());
		this.speed.setRotation(speed.getRotation());
	}
	
	public float getSpeedX() {
		return speed.getX();
	}
	
	public void setSpeedX(float speed_x) {
		speed.setX(speed_x);
	}
	
	public void addSpeedX(float speed_x) {
		speed.addX(speed_x);
	}
	
	public float getSpeedY() {
		return speed.getY();
	}
	
	public void setSpeedY(float speed_y) {
		speed.setY(speed_y);
	}
	
	public void addSpeedY(float speed_y) {
		speed.addY(speed_y);
	}
	
	public float getSpeedZ() {
		return speed.getZ();
	}
	
	public void setSpeedZ(float speed_z) {
		speed.setZ(speed_z);
	}
	
	public void addSpeedZ(float speed_z) {
		speed.addZ(speed_z);
	}
	
	public float getSpeedRotation() {
		return speed.getRotation();
	}
	
	public void setSpeedRotation(float speed_cw) {
		speed.setRotation(speed_cw);
	}
	
	public void addSpeedRotation(float speed_cw) {
		speed.addRotation(speed_cw);
	}
	
	// Force
	public void setForce(XYZRAxes force) {
		this.force.setX(force.getX());
		this.force.setY(force.getY());
		this.force.setZ(force.getZ());
		this.force.setRotation(force.getRotation());
	}
	
	public float getForceX() {
		return force.getX();
	}
	
	public void setForceX(float force_x) {
		force.setX(force_x);
	}
	
	public void addForceX(float force_x) {
		force.addX(force_x);
	}
	
	public float getForceY() {
		return force.getY();
	}
	
	public void setForceY(float force_y) {
		force.setY(force_y);
	}
	
	public void addForceY(float force_y) {
		force.addY(force_y);
	}
	
	public float getForceZ() {
		return force.getZ();
	}
	
	public void setForceZ(float force_z) {
		force.setZ(force_z);
	}
	
	public void addForceZ(float force_z) {
		force.addZ(force_z);
	}
	
	public float getForceRotation() {
		return force.getRotation();
	}
	
	public void setForceRotation(float force_cw) {
		force.setRotation(force_cw);
	}
	
	public void addForceRotation(float force_cw) {
		force.addRotation(force_cw);
	}

	// Update object position and speed
	public void update() {
		
		// Update speed
		addSpeedX(getForceX()/getMass());
		addSpeedY(getForceY()/getMass());
		addSpeedZ(getForceZ()/getMass());
		addSpeedRotation(getForceRotation()/getMass());
		
		// Reset forces
		setForceX(0);
		setForceY(0);
		setForceZ(0);
		setForceRotation(0);
		
		// Update position
		addX(getSpeedX());
		addY(getSpeedY());
		addZ(getSpeedZ());
		addRotation(getSpeedRotation());
	}
}
