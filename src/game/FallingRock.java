package game;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.*;

import engine.DynamicObject;
import engine.PathedTextures;

public class FallingRock extends DynamicObject
{
	public FallingRock () 
	{
		try 
		{
			double stone_type = Math.random();
			if (stone_type > 0.8)
				this.setTexture(PathedTextures.getTexture(Paths.get("res/shaft/stone_1_blur.tga")), true);
			else if (stone_type > 0.6)
				this.setTexture(PathedTextures.getTexture(Paths.get("res/shaft/stone_2_blur.tga")), true);
			else if (stone_type > 0.4)
				this.setTexture(PathedTextures.getTexture(Paths.get("res/shaft/stone_3_blur.tga")), true);
			else if (stone_type > 0.2)
				this.setTexture(PathedTextures.getTexture(Paths.get("res/shaft/stone_4_blur.tga")), true);
			else if (stone_type > 0.0)
				this.setTexture(PathedTextures.getTexture(Paths.get("res/shaft/stone_5_blur.tga")), true);
			this.setOutlineColor(new Color(255, 0, 0));
			this.setOutlineThickness(3);
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
	}
}
