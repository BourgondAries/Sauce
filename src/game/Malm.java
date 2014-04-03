package game;


import org.jsfml.system.Vector2f;
import engine.DynamicObject;


public class Malm extends DynamicObject
{
	public Malm()
	{
		super.fetchImpulse().y = -(float) (Math.random() * 4.f);
		super.fetchImpulse().x = (float) (Math.random() * 2.f - 2.f);
		super.setSize( new Vector2f (5, 5) );
	}
	
	public Malm( Vector2f position )
	{
		this();
		super.setPosition(position);
	}
	
	public long getScore()
	{
		return 1000;
	}
}
