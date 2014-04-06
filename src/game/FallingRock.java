package game;

import engine.DynamicObject;

public class FallingRock extends DynamicObject
{
	public FallingRock () 
	{
		
	}
	
	protected void finalize()
	{
		System.out.println("Object finally released!");
	}
	
	
}
