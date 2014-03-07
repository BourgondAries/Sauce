package game;


import engine.DynamicObject;


public class Player extends DynamicObject
{
	
//	private gunStates gun_state;
	
	// CM: C stands for "Constant", M stands for "Class Member"
	public static float CM_JUMPFORCE = -50.f;
	
//	private enum gunStates
//	{
//		reloading, shooting, idle, outOfBullets;
//	}
	
	public Player()
	{}

	public String toString()
	{
		return "Player's position: (" + getPosition().x + ", " + getPosition().y + ", " + getRotation() + ")"; 
	}

	public void update()
	{
		super.update();
		
		//Gun, animation, osv logic
		// very good...
	}
	
	public void jump() 
	{
//		if (world_state == worldStates.onGround) 
		{
			fetchImpulse().y += CM_JUMPFORCE;
		}
//		else if (world_state == Environment.inMagma) 
//		{
//			Final<Vector3f> const_impulse = getImpulse();
//			Vector3f impulse = const_impulse.data;
//			impulse.y += CM_JUMPFORCE / 2;
//			setImpulse(impulse);
//		}
	}
}