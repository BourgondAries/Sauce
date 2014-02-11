package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import engine.*;

import java.util.ArrayList;


/*
 * This represents the bottom of the world,
 * it will be a chunk that is re-generated if the coordinates are accessed.
 * We can implement this using tiles at a random position, connected,
 * below a certain x-line.
 */

public class BottomOfTheWorld extends InfiniteBox
{
	
	private final static float
		CM_TILE_WIDTH = 33.f,
		CM_TILE_HEIGHT = 33.f;
	private ArrayList<ArrayList<StaticObject>> m_tiles = new ArrayList<>();
	
	public BottomOfTheWorld(RenderCam render_cam) {
		super(Main.BOTTOM_OF_THE_WORLD,0,false,true,render_cam);
		//m_bottom = new StaticObject(new XYZRAxes(-200,Main.BOTTOM_OF_THE_WORLD,0,0), new XYAxes(3000,300));
		setColor(new Color(255,255,255));
		setRenderable(false);
		//Core.render_cam.makeDrawable(m_bottom);
	}
	
	
	public void generateTiles()
	{
		for (int x = 0; x < 30; ++x)
		{
			
			m_tiles.add(new ArrayList<StaticObject>());
			for (int y = 0; y < 10; ++y)
			{
				StaticObject temporary_rectangleshape = new StaticObject(new XYZRAxes(CM_TILE_WIDTH * x, CM_TILE_HEIGHT * y + Main.BOTTOM_OF_THE_WORLD,0,0),new XYAxes(CM_TILE_WIDTH, CM_TILE_HEIGHT));
				temporary_rectangleshape.setColor(new Color((int) Math.random() % 5 + 127, 127, 180));
				
				m_tiles.get(x).add(temporary_rectangleshape);
			}
		}
	}
	
	// Returns the position of the erased tile, so we can create particles from there.
	public XYZAxes eraseRandomTileAtTheTop()
	{
		int x_pos_to_erase = (int) (Math.random() * (float) m_tiles.size());
		while (m_tiles.get(x_pos_to_erase).size() == 0)
		{
			if (x_pos_to_erase == m_tiles.size() - 1)
				return new XYZAxes(0.f, 0.f, 0.f);
			else
				++x_pos_to_erase;
		}
		m_tiles.get(x_pos_to_erase).remove(0);
		return new XYZAxes(m_tiles.get(x_pos_to_erase).get(0).getX(),m_tiles.get(x_pos_to_erase).get(0).getY(),m_tiles.get(x_pos_to_erase).get(0).getZ());
	}
	
	// converts pixels to a block of a tile on the tile grid
	public int getTheTopOfTheTileLine(int x)
	{
		x = x / (int) CM_TILE_WIDTH;
		return x;
	}
	
	// Queries whether a tile exists under us, if true, block movement in player.
	public boolean doesATileExistHere(float x, float y, float z)
	{
		if (z!=0) return false;
		int x_index_to_check = getTheTopOfTheTileLine((int) x);
		
		// Fetch the size of the linear array of tiles on the y axis
		int height = (int) (m_tiles.get(x_index_to_check).size() * CM_TILE_HEIGHT);
		
//		System.out.println(Main.BOTTOM_OF_THE_WORLD - position.y - height);
		
		return Main.BOTTOM_OF_THE_WORLD - y - height > (-280);
	}
	
	// Returns the position of the nearest, top tile
	public XYZAxes getCollisionTilePosition(float x, float y, float z)
	{
		int x_index_to_check = getTheTopOfTheTileLine((int) x);
		
		if (m_tiles.get(x_index_to_check).size() > 0 == false)
			return new XYZAxes(0.f, 0.f,0.f);
		int height = (int) m_tiles.get(x_index_to_check).get(0).getY();
		return new XYZAxes(m_tiles.get(x_index_to_check).get(0).getX(),m_tiles.get(x_index_to_check).get(0).getY(),m_tiles.get(x_index_to_check).get(0).getZ());
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		super.draw(target, states);
		for (int x = 0; x<m_tiles.size(); x++) {
			for (int y = 0; y<m_tiles.get(x).size(); y++) {
				if (m_tiles.get(x).get(y)!=null) m_tiles.get(x).get(y).draw(target, states);
			}
		}
	}
}














