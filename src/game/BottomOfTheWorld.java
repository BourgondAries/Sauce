package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import java.util.ArrayList;


/*
 * This represents the bottom of the world,
 * it will be a chunk that is re-generated if the coordinates are accessed.
 * We can implement this using tiles at a random position, connected,
 * below a certain x-line.
 */

public class BottomOfTheWorld implements Drawable
{
	
	private final static float
		CM_TILE_WIDTH = 33.f,
		CM_TILE_HEIGHT = 33.f;
	private RectangleShape m_bottom = null;
	private ArrayList<ArrayList<RectangleShape>> m_tiles = new ArrayList<>();
	
	public BottomOfTheWorld()
	{
		m_bottom = new RectangleShape();
		m_bottom.setSize(new Vector2f(3000, 300));
		m_bottom.setPosition(new Vector2f(-200, Main.BOTTOM_OF_THE_WORLD));
		m_bottom.setFillColor(new Color(90, 90, 90));
	}
	
	
	public void generateTiles()
	{
		for (int x = 0; x < 30; ++x)
		{
			m_tiles.add(new ArrayList<RectangleShape>());
			for (int y = 0; y < 10; ++y)
			{
				RectangleShape temporary_rectangleshape = new RectangleShape();
				temporary_rectangleshape.setSize(new Vector2f(CM_TILE_WIDTH, CM_TILE_HEIGHT));
				temporary_rectangleshape.setFillColor(new Color((int) Math.random() % 5 + 127, 127, 180));
				temporary_rectangleshape.setPosition(new Vector2f(CM_TILE_WIDTH * x, CM_TILE_HEIGHT * y + Main.BOTTOM_OF_THE_WORLD));
				temporary_rectangleshape.setOutlineColor(new Color(1, 0, 255));
				temporary_rectangleshape.setOutlineThickness(1.f);
				
				m_tiles.get(x).add(temporary_rectangleshape);
			}
		}
	}
	
	// Returns the position of the erased tile, so we can create particles from there.
	public Vector2f eraseRandomTileAtTheTop()
	{
		int x_pos_to_erase = (int) (Math.random() * (float) m_tiles.size());
		while (m_tiles.get(x_pos_to_erase).size() == 0)
		{
			if (x_pos_to_erase == m_tiles.size() - 1)
				return new Vector2f(0.f, 0.f);
			else
				++x_pos_to_erase;
		}
		Vector2f x = m_tiles.get(x_pos_to_erase).get(0).getPosition();
		m_tiles.get(x_pos_to_erase).remove(0);
		return x;
	}
	
	// converts pixels to a block of a tile on the tile grid
	public int getTheTopOfTheTileLine(int x)
	{
		x = x / (int) CM_TILE_WIDTH;
		return x;
	}
	
	// Queries whether a tile exists under us, if true, block movement in player.
	public boolean doesATileExistHere(Vector2f position)
	{
		int x_index_to_check = getTheTopOfTheTileLine((int) position.x);
		
		// Fetch the size of the linear array of tiles on the y axis
		int height = (int) (m_tiles.get(x_index_to_check).size() * CM_TILE_HEIGHT);
		
//		System.out.println(Main.BOTTOM_OF_THE_WORLD - position.y - height);
		
		return Main.BOTTOM_OF_THE_WORLD - position.y - height > (-280);
	}
	
	// Returns the position of the nearest, top tile
	public Vector2f getCollisionTilePosition(Vector2f position)
	{
		int x_index_to_check = getTheTopOfTheTileLine((int) position.x);
		
		if (m_tiles.get(x_index_to_check).size() > 0 == false)
			return new Vector2f(0.f, 0.f);
		int height = (int) m_tiles.get(x_index_to_check).get(0).getPosition().y;
		System.out.println(height);
		return  m_tiles.get(x_index_to_check).get(0).getPosition();
	}
	
	public void draw(RenderTarget rendertarget, RenderStates renderstates)
	{
		for (int x = 0; x < m_tiles.size(); ++x)
		{
			for (int y = 0; y < m_tiles.get(x).size(); ++y)
			{
				rendertarget.draw(m_tiles.get(x).get(y));
			}
		}
	}
}














