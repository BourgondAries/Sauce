package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.system.*;
import org.jsfml.graphics.*;

import engine.*;

public class FallingHealth extends DynamicObject
{
	public FallingHealth () 
	{
		try 
		{
			this.setSize(new Vector2f(50.f, 50.f));
			this.setTexture(PathedTextures.getTexture(Paths.get("res/shaft/health.tga")));
			this.setOrigin(new Vector2f(25.f, 25.f));
			this.setOutlineThickness(7);
			this.setOutlineColor(new Color(127, 127, 127, 127));
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		this.setSpeed(new Vector3f(0.f, 1.f, 1.f));
	}
}
