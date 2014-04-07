package game;


import engine.DynamicObject;


public class Player extends DynamicObject
{
	
	// CM: C stands for "Constant", M stands for "Class Member"
	// I tend to make static/constant PODs (like jump force) ALL_CAPS.
	public static float CM_JUMPFORCE = -50.f;
	private final static int CM_MAX_HEALTH = 5;
	private int m_health = CM_MAX_HEALTH;
	
	public Player()
	{}

	public String toString()
	{
		return "Player's position: (" + getPosition().x + ", " + getPosition().y + ", " + getRotation() + ")"; 
	}
	
	/**
	 * Takes a single hit, 1. The damage is in integer form.
	 * 0 means death, anything else means alive.
	 * We can change the sprite as damage is taken.
	 */
	public void takeDamage()
	{
		if ( m_health == 0)
			return;
		--m_health;
	}
	
	public void repairDamage()
	{
		if ( m_health == CM_MAX_HEALTH )
			return;
		++m_health;
	}
	
	public int getHealth()
	{
		return m_health;
	}

	public void update()
	{
		super.update();
		
		//Gun, animation, osv logic
		// very good...
	}
	
	public static int getMaxHealth()
	{
		return CM_MAX_HEALTH;
	}
	
	public void jump() 
	{
		{
			fetchImpulse().y += CM_JUMPFORCE;
		}
	}
}