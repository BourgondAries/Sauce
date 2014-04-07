package game;

import java.io.IOException;
import java.nio.file.Paths;

import engine.DynamicObject;
import engine.PathedTextures;

public class FallingRock extends DynamicObject
{
	public FallingRock () 
	{
		try 
		{
			this.setTexture(PathedTextures.getTexture(Paths.get("res/shaft/rock.tga")));
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
	}
}
