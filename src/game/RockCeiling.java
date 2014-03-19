package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import java.util.*;

public class RockCeiling implements Drawable
{
	
	private ArrayList<RectangleShape> m_tiles = new ArrayList<RectangleShape>();
	
	public RockCeiling()
	{
		for (int x = 0; x < 10; ++x)
		{
			for (int y = 0; y < 10; ++y)
			{
				RectangleShape temporary_rectangleshape = new RectangleShape();
				temporary_rectangleshape.setSize(new Vector2f(100.f, 100.f));
				temporary_rectangleshape.setFillColor(new Color(127, 127, 127));
				temporary_rectangleshape.setPosition(new Vector2f(100.f * x, - 100.f * y + Main.BOTTOM_OF_THE_WORLD_CEILING));
				temporary_rectangleshape.setOutlineColor(new Color(150, 0, 0));
				temporary_rectangleshape.setOutlineThickness(1.f);
				
				m_tiles.add(temporary_rectangleshape);
			}
		}
	}
	
	public void draw(RenderTarget rendertarget, RenderStates renderstates)
	{
		for (int i = 0; i < m_tiles.size(); ++i)
		{
			rendertarget.draw(m_tiles.get(i));
		}
	}
	
	
}
