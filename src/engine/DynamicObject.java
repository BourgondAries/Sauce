package engine;

import java.io.IOException;

public class DynamicObject extends StaticObject {
	
	// Setup movement
	protected float mass;
	protected XYZRAxes speed;
	protected XYZRAxes impulse;
	protected worldStates world_state;

	public DynamicObject(String image_path, XYZRAxes position, float mass) throws IOException, IllegalArgumentException {
		
		super(image_path, position);
		
		if (mass<=0) throw new IllegalArgumentException("Mass cannot be less than or equal to zero"); //IllegalArgumentException, IllegalStateEx, NumberFormatEx, NullPointerEx, IndexOutOfBoundsEx, StringIndexOutOfBoundsEx, ArrayIndexOutOfBoundsEx, RuntimeEx
		
		this.mass = mass;
		this.speed = new XYZRAxes(0,0,0,0);
		this.impulse = new XYZRAxes(0,0,0,0);
		this.world_state = worldStates.inAir;
	}
	
	public DynamicObject(XYZRAxes position, XYAxes size, float mass) throws IllegalArgumentException {
		
		super(position, size);
		
		if (mass<=0) throw new IllegalArgumentException("Mass cannot be less than or equal to zero"); //IllegalArgumentException, IllegalStateEx, NumberFormatEx, NullPointerEx, IndexOutOfBoundsEx, StringIndexOutOfBoundsEx, ArrayIndexOutOfBoundsEx, RuntimeEx
		
		this.mass = mass;
		this.speed = new XYZRAxes(0,0,0,0);
		this.impulse = new XYZRAxes(0,0,0,0);
		this.world_state = worldStates.inAir;
	}
	
	// States
	public static enum worldStates {
		inAir, onGround, inMagma;
	}
	
	public void inAir() {
		world_state = worldStates.inAir;
	}
	
	public void inMagma() {
		world_state = worldStates.inMagma;
	}
	
	public void onGround() {
		world_state = worldStates.onGround;
	}
	
	public boolean isInAir() {
		return world_state == worldStates.inAir;
	}
	
	public boolean isInMagma() {
		return world_state == worldStates.inMagma;
	}
	
	public boolean isOnGround() {
		return world_state == worldStates.onGround;
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
	
	// Impulse
	public void setImpulse(XYZRAxes impulse) {
		this.impulse.setX(impulse.getX());
		this.impulse.setY(impulse.getY());
		this.impulse.setZ(impulse.getZ());
		this.impulse.setRotation(impulse.getRotation());
	}
	
	public float getImpulseX() {
		return impulse.getX();
	}
	
	public void setImpulseX(float impulse_x) {
		impulse.setX(impulse_x);
	}
	
	public void addImpulseX(float impulse_x) {
		impulse.addX(impulse_x);
	}
	
	public float getImpulseY() {
		return impulse.getY();
	}
	
	public void setImpulseY(float impulse_y) {
		impulse.setY(impulse_y);
	}
	
	public void addImpulseY(float impulse_y) {
		impulse.addY(impulse_y);
	}
	
	public float getImpulseZ() {
		return impulse.getZ();
	}
	
	public void setImpulseZ(float impulse_z) {
		impulse.setZ(impulse_z);
	}
	
	public void addImpulseZ(float impulse_z) {
		impulse.addZ(impulse_z);
	}
	
	public float getImpulseRotation() {
		return impulse.getRotation();
	}
	
	public void setImpulseRotation(float impulse_cw) {
		impulse.setRotation(impulse_cw);
	}
	
	public void addImpulseRotation(float impulse_cw) {
		impulse.addRotation(impulse_cw);
	}

	// Update object position and speed
	public void update() {
		
		// Update position
		addX(getSpeedX());
		addY(getSpeedY());
		addZ(getSpeedZ());
		addRotation(getSpeedRotation());
		
		// Update speed
		addSpeedX(getImpulseX()/getMass());
		addSpeedY(getImpulseY()/getMass());
		addSpeedZ(getImpulseZ()/getMass());
		addSpeedRotation(getImpulseRotation()/getMass());
		
		// Reset impulses
		setImpulseX(0);
		setImpulseY(0);
		setImpulseZ(0);
		setImpulseRotation(0);
	}
}
