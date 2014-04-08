package game;


import org.jsfml.system.Vector2f;
import engine.DynamicObject;


public class Malm extends DynamicObject
{
	private long m_score = 0;
	private static final float 
		CM_DIMENSION_RANDOM = 15.f,
		CM_DIMENSION_MIN = 3.5f;
	
	public Malm()
	{
		super.fetchImpulse().y = -(float) (Math.random() * 4.f);
		super.fetchImpulse().x = (float) (Math.random() * 2.f - 2.f);
		super.setSize( new Vector2f ((float) (Math.random() * CM_DIMENSION_RANDOM + CM_DIMENSION_MIN), (float) (Math.random() * CM_DIMENSION_RANDOM + CM_DIMENSION_MIN)) );
		m_score = (long) (super.getSize().x * super.getSize().y);
	}
	
	public Malm( Vector2f position )
	{
		this();
		super.setPosition(position);
	}
	
	public static float getMaxScore()
	{
		return (float) Math.pow(CM_DIMENSION_RANDOM + CM_DIMENSION_MIN, 2); 
	}
	
	public long getScore()
	{
		return m_score;
	}
}
