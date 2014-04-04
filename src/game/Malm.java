package game;


import org.jsfml.system.Vector2f;
import engine.DynamicObject;


public class Malm extends DynamicObject
{
	private long m_score = 0;
	
	public Malm()
	{
		super.fetchImpulse().y = -(float) (Math.random() * 4.f);
		super.fetchImpulse().x = (float) (Math.random() * 2.f - 2.f);
		super.setSize( new Vector2f ((float) (Math.random() * 15f + 3.5f), (float) (Math.random() * 15f + 3.5f)) );
		m_score = (long) (super.getSize().x * super.getSize().y);
	}
	
	public Malm( Vector2f position )
	{
		this();
		super.setPosition(position);
	}
	
	public long getScore()
	{
		return m_score;
	}
}
