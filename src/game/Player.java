package game;


import java.io.IOException;

import engine.*;


public class Player extends DynamicObject
{
	
	// CM: C stands for "Constant", M stands for "Class Member"
	// I tend to make static/constant PODs (like jump force) ALL_CAPS.
	public static float CM_JUMPFORCE = -50.f;
	private final static int CM_MAX_HEALTH = 5;
	private int m_health = CM_MAX_HEALTH;
	private SyncTrack 
		m_impact_sound,
		m_heal_sound;
	
	public Player()
	{
		try 
		{
			m_impact_sound = Formulas.loadSound("sfx/explosion_punchy_impact_02.ogg");
			m_impact_sound.fetchTrack().setVolume(35.f);
			m_heal_sound = Formulas.loadSound("sfx/heart_beat.ogg");
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
	}

	public void setImpactSound(String str)
	{
		try 
		{
			m_impact_sound = Formulas.loadSound(str);
		}
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
	}
	
	public void setHealth(int health)
	{
		m_health = health;
	}
	
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
		m_impact_sound.play();
	}
	
	public void takeDamage(boolean playsound)
	{
		if ( m_health == 0)
			return;
		--m_health;
		if (playsound)
			m_impact_sound.play();
	}
	
	public void repairDamage()
	{
		if ( m_health == CM_MAX_HEALTH )
			return;
		++m_health;
		m_heal_sound.play();
	}
	
	public void repairDamage(boolean playsound)
	{
		if ( m_health == CM_MAX_HEALTH )
			return;
		++m_health;
		if (playsound)
			m_heal_sound.play();
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
		fetchImpulse().y += CM_JUMPFORCE;
	}
	
	public void jumpDown() 
	{
		fetchImpulse().y -= CM_JUMPFORCE;
	}
}