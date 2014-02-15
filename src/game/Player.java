package game;

import java.io.IOException;

import engine.DynamicObject;
import engine.XYZRAxes;
import engine.DynamicObject.worldStates;

public class Player extends DynamicObject
{
	
	private gunStates gun_state;
	
	// CM: C stands for "Constant", M stands for "Class Member"
	public static float CM_JUMPFORCE = -50.f;
	
	private enum gunStates
	{
		reloading, shooting, idle, outOfBullets;
	}
	
	public Player(String image_path, XYZRAxes position, float mass) throws IOException 
	{
		super(image_path, position, mass);
		setOriginCenter();
		gun_state = gunStates.idle;
	}

	public String toString() {
		return "Player position: (" + getX() + ", " + getY() + ", " + getZ() + ")"; 
	}

	@Override
	public void update() {
		super.update();
		
		//Gun, animation, osv logic
	}
	
	public void jump() {
		if (world_state == worldStates.onGround) {
			addImpulseY(CM_JUMPFORCE);
		} else if (world_state == worldStates.inMagma) {
			addImpulseY(CM_JUMPFORCE/2);
		}
	}
}