package engine;

import java.io.IOException;

import game.Main;

public class Player extends DynamicObject {
	
	public worldStates world_state;
	public gunStates gun_state;
	
	public enum worldStates {
		inAir, onGround, inMagma;
	}
	
	public enum gunStates {
		reloading, shooting, idle, outOfBullets;
	}
	
	public Player(String image_path, XYZRAxes position, XYAxes size, float mass) throws IOException {
		super(image_path, position, mass);
		setOriginCenter();
		world_state = worldStates.onGround;
		gun_state = gunStates.idle;
	}
	
	public void logic() {
		if (getBoundBottom() + getY() > Main.START_OF_MAGMA) {
			world_state = worldStates.inAir;
			force.addY(-0.01f);
		} else if (Main.START_OF_MAGMA > getBoundBottom() + getY() && getBoundBottom() + getY() > Main.BOTTOM_OF_THE_WORLD - getHeight()/2) {
			world_state = worldStates.inMagma;
			force.addY(0.04f);
		} else {
			world_state = worldStates.onGround;
			speed.setY(0);
			position.setY(Main.BOTTOM_OF_THE_WORLD - getHeight()/2);
		}
	}
	
	public void jump() {
		if (world_state == worldStates.onGround) {
			force.addY(10);
		} else if (world_state == worldStates.inMagma) {
			force.addY(5);
		}
	}
}
