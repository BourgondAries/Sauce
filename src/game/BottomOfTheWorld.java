package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import java.util.ArrayList;

import engine.Pair;


/**
 * This represents the bottom of the world.
 * It will be a chunk that is re-generated if invalid coordinates are accessed.
 */
public class BottomOfTheWorld implements Drawable
{
	/**
	 * This class holds the crimson red liquid under the scorp.
	 * This liquid, fiery mass of metal is supposed to burst out of 
	 * the scorp like a geyser in Iceland when part of the scorp is 
	 * punctured (when there are 0 blocks between).
	 * The geyser should be infinitely long during core mode.
	 * 
	 * The player will be trapped between 2 or multiple geysers,
	 * giving the illusion of being closed in on.
	 * There must also be a warning texture for this.
	 * Once a tile at a geyser point is destroyed, 
	 * 1 second of partial-geyser animation must be shown.
	 * YEAH LET'S IMPLEMENT IT!!! GONNA BE AWESOMMMMEEEE!!! :DDDD
	 * @author Kevin Robert Stravers
	 *
	 */
	private class RelativeMagma extends RectangleShape
	{
		public RelativeMagma()
		{
			super.setSize(new Vector2f(Main.wnd.getSize().x * 2, Main.wnd.getSize().y));
			super.setFillColor(new Color(220, 20, 60));
		}
	}
	
	
	
	/**
	 * C: Constant
	 * M: Member
	 * 
	 * These numbers describe the block width and height, as well as the number of
	 * blocks on the x and y axis respectively.
	 */
	private final static float
		CM_TILE_WIDTH = 33.f,
		CM_TILE_HEIGHT = 33.f;
	
	private final static int
		CM_TILE_COUNT_X = 60,
		CM_TILE_COUNT_Y = 15;
	
	public final static int getTileCountY()
	{
		return CM_TILE_COUNT_Y;
	}
	
	public final static float getTileHeight()
	{
		return CM_TILE_HEIGHT;
	}
	
	public final static float getTileWidth()
	{
		return CM_TILE_WIDTH;
	}
	
	private class MagmaGeyser extends RectangleShape
	{
		public static final float CM_GEYSER_HEIGHT = 1000.f;
		private boolean m_active = false;
		
		public MagmaGeyser()
		{
			super.setSize(new Vector2f(CM_TILE_WIDTH, CM_GEYSER_HEIGHT));
			super.setFillColor(new Color(220, 20, 60));
		}
		
		public void draw(RenderTarget target, RenderStates states)
		{
			if (m_active)
				super.draw(target, states);
		}
		
		public void setActive(boolean state)
		{
			m_active = state;
		}
	}
	
	/**
	 * This 2D array contains all the tiles that are to be drawn.
	 * Each list of RectangleShapes is a single column (y-axis).
	 * Every tile has its own position stored in it.
	 * position 0 is always to the left.
	 * position 0 on the columns is always the top block.
	 */
	private ArrayList< ArrayList<RectangleShape> >
		m_tiles = new ArrayList <> ( );
	private ArrayList<MagmaGeyser>
		m_geysers = new ArrayList <> ( );
		
	/**
	 * The bounds describe the outer bounds of the "ground" in
	 * pixel coordinates in the 2D plane.
	 * 
	 * This has important uses; to determine when we are out of
	 *  - or nearly - bounds, and we need to regenerate the terrain
	 *  in a certain direction.
	 */
	private Pair<Integer, Integer> 
		m_x_bounds = new Pair<>(0, (int) (((float) CM_TILE_COUNT_X) * CM_TILE_WIDTH));
		
	private RelativeMagma m_crimson = new RelativeMagma();
		
	/**
	 * Default constructor.
	 */
	public BottomOfTheWorld ( )
	{
		m_crimson.setPosition(0, CM_TILE_HEIGHT * CM_TILE_COUNT_Y);
		
		for ( int i = 0; i < CM_TILE_COUNT_X; ++i )
			m_geysers.add(new MagmaGeyser());
	}
	
	public void updateCrimsonRelativeTo(float x)
	{
		m_crimson.setPosition((float) x - Main.wnd.getSize().x, m_crimson.getPosition().y);
	}
	
	/**
	 * Method that generates the tile set.
	 * Starts from 
	 */
	public void generateTiles()
	{
		m_tiles.clear();
		for (int x = 0; x < CM_TILE_COUNT_X; ++x)
		{
			m_tiles.add(new ArrayList<RectangleShape>());
			for (int y = 0; y < CM_TILE_COUNT_Y; ++y)
			{
				RectangleShape temporary_rectangleshape = new RectangleShape();
				temporary_rectangleshape.setSize(new Vector2f(CM_TILE_WIDTH, CM_TILE_HEIGHT));
				temporary_rectangleshape.setFillColor(new Color((int) Math.random() % 5 + 127, 127, 180));
				
				/*
				 * Start the tile at the m_x_bound.first coordinate;
				 * this coordinate should be changed to the desired coordinate
				 * before calling this function.
				 * 0 is a valid number.
				 */
				temporary_rectangleshape.setPosition
				(
						new Vector2f
						(
								CM_TILE_WIDTH * x + m_x_bounds.first,
								CM_TILE_HEIGHT * y
						)
				);
				temporary_rectangleshape.setOutlineColor(new Color(1, 0, 255));
				temporary_rectangleshape.setOutlineThickness(1.f);
				m_tiles.get(x).add(temporary_rectangleshape);
			}
		}
		
		// Mark the leftmost tiles with a color pattern (for debugging):
		m_tiles.get(0).get(0).setFillColor(new Color(255, 0, 0));
		m_tiles.get(9).get(0).setFillColor(new Color(0, 0, 255));
	}
	
	/**
	 * \brief Function regenerates the floor around this x-axis.
	 * 
	 * Call this function when the player's x starts getting near the bounds
	 * of the bottom of the world.
	 * 
	 * \see getXBounds
	 */
	public void regenerateAround ( int x )
	{
		m_x_bounds.first = x;
		m_x_bounds.second = (int) (((float) CM_TILE_COUNT_X) * CM_TILE_WIDTH) - x;
		generateTiles();
	}
	
	/**
	 * Method that generated blocks to the left.
	 * When walking to the left, after a set limit
	 * has been surpassed, this function is called and
	 * will fill a space to the left with some tiles.
	 * 
	 * IMPORTANT NOTE FOR FUTURE ME:
	 * When shifting left or right:
	 * left:
	 * we take the x rightmost columns and place them to the left, without
	 * interchanging the columns individually.
	 * Example:
	 * 
	 * 1 2 3 4 5 6 7 8 9 -> 7 8 9 1 2 3 4 5 6 7
	 * 
	 * The same rule is applied to the right:
	 * 
	 * 1 2 3 4 5 6 7 8 9 -> 3 4 5 6 7 8 9 1 2 3
	 * 
	 * TODO: Implement premature decay thanks to time.
	 */
	public void generateLeft()
	{
		final int AMOUNT_OF_SHIFTED_TILES = 10;
		
		// First, get the current left most tile x position:
		float prevx = m_x_bounds.first;
		
		// Lets try to move the 10 rightmost ones to the left:
		for (int x = 0; x < AMOUNT_OF_SHIFTED_TILES; ++x)
		{
			m_tiles.add(0, m_tiles.get(m_tiles.size() - 1));
			m_tiles.remove(m_tiles.size() - 1);
		}
		
		// Now translate the x coordinates:
		for (int x = 0; x < AMOUNT_OF_SHIFTED_TILES; ++x)
		{
			for (int y = 0; y < m_tiles.get(x).size(); ++y)
			{
				m_tiles.get(x).get(y).setPosition(prevx - (AMOUNT_OF_SHIFTED_TILES - x) * CM_TILE_WIDTH, m_tiles.get(x).get(y).getPosition().y);
			}
		}
		
		
		m_x_bounds.first -= (int) ((AMOUNT_OF_SHIFTED_TILES) * CM_TILE_WIDTH);
		m_x_bounds.second -= (int) ((AMOUNT_OF_SHIFTED_TILES) * CM_TILE_WIDTH);
		
	}
	
	/**
	 * Method shifts the leftmost tiles to the right and translates all bounds correctly.
	 * Performs the opposite action of generateLeft
	 */
	public void generateRight()
	{
		final int AMOUNT_OF_SHIFTED_TILES = 10;
		
		// First, get the current left most tile x position:
		float prevrx = m_x_bounds.second;
		
		// Lets try to move the 10 rightmost ones to the left:
		for (int x = 0; x < AMOUNT_OF_SHIFTED_TILES; ++x)
		{
			m_tiles.add(m_tiles.get(0));
			m_tiles.remove(0);
		}
		
		// Now translate the x coordinates:
		for (int x = 0; x < AMOUNT_OF_SHIFTED_TILES; ++x)
		{
			for (int y = 0; y < m_tiles.get(m_tiles.size() - 1 - x).size(); ++y)
			{
				m_tiles.get(m_tiles.size() - 1 - x).get(y).setPosition(prevrx + (AMOUNT_OF_SHIFTED_TILES - x - 1) * CM_TILE_WIDTH, m_tiles.get(m_tiles.size() - 1 - x).get(y).getPosition().y);
			}
		}
		
		m_x_bounds.first += (int) ((AMOUNT_OF_SHIFTED_TILES) * CM_TILE_WIDTH);
		m_x_bounds.second += (int) ((AMOUNT_OF_SHIFTED_TILES) * CM_TILE_WIDTH);
	}
	
	public Pair<Integer, Integer> getXBounds ( )
	{
		return m_x_bounds;
	}
	
	/**
	 * Remove a random tile at the top of a column.
	 * This function returns the tile's position, thus
	 * allowing the creation of particle effects in that location.
	 * 
	 * IMPORTANT: Do note that the location is the upper left corner
	 * of the tile.
	 */
	public Vector2f eraseRandomTileAtTheTop()
	{
		int x_pos_to_erase = (int) (Math.random() * (float) m_tiles.size());
		while (m_tiles.get(x_pos_to_erase).size() == 0)
		{
			if (x_pos_to_erase == m_tiles.size() - 1)
				return null;
			else
				++x_pos_to_erase;
		}
		Vector2f tmp_pos = m_tiles.get(x_pos_to_erase).get(0).getPosition();
		m_tiles.get(x_pos_to_erase).remove(0);
		
		if (tmp_pos.y == CM_TILE_HEIGHT * (CM_TILE_COUNT_Y - 1) )
		{
			m_geysers.get(x_pos_to_erase).setActive(true);
			m_geysers.get(x_pos_to_erase).setPosition(tmp_pos.x, -MagmaGeyser.CM_GEYSER_HEIGHT + CM_TILE_COUNT_Y * CM_TILE_HEIGHT);
		}
		return tmp_pos;
	}
	
	/**
	 * Method that converts an x coordinate in world space
	 * to a row index of the tiles.
	 * 
	 * @param x_position The position in world space
	 * @return The index of the row where that x coordinate falls on.
	 */
	private int getTheTopOfTheTileLine ( int x_position )
	{
		x_position = ( x_position - m_x_bounds.first ) / (int) CM_TILE_WIDTH;
		return x_position;
	}
	
	
	/**
	 * Checks whether a tile exists at a specified position.
	 * If the function goes out of bounds, it will
	 * regenerate around that position.
	 * @param position
	 * @return
	 */
	public boolean doesATileExistHere(Vector2f position)
	{
		int x_index_to_check = getTheTopOfTheTileLine((int) position.x);
		int height = 0;
		if (x_index_to_check <= 0 || x_index_to_check >= m_tiles.size() - 2)
		{
			// Need to regenerate the tile set.
			if ( position.x < (getXBounds().first + 20) )
			{
				generateLeft();
				return doesATileExistHere(position);
			}
			else if ( position.x > (getXBounds().second - 20))
			{
				regenerateAround((int) position.x);
				return doesATileExistHere(position);
			}
		}
		else
		{
			// Fetch the size of the linear array of tiles on the y axis
			height = (int) (m_tiles.get(x_index_to_check).size() * CM_TILE_HEIGHT);
		}
		
		float max_height = CM_TILE_COUNT_Y * CM_TILE_HEIGHT;
		return position.y < max_height - height;
	}
	
	// Returns the position of the nearest, top tile
	public Vector2f getCollisionTilePosition(int x)
	{
		int x_index_to_check = getTheTopOfTheTileLine((int) x);
		
		if (m_tiles.get(x_index_to_check).size() > 0 == false)
			return new Vector2f((float) x, CM_TILE_COUNT_Y * CM_TILE_HEIGHT);
		return  m_tiles.get(x_index_to_check).get(0).getPosition();
	}
	
	/**
	 * This method allows one to call the equivalent of:
	 * RenderWindow.draw (BottomOfTheWorldObject);
	 * 
	 * Draws all tiles on the screen.
	 * 
	 * Warning: drawing takes O(n) time where n is the tile.
	 * This method can become expensive for a 100x100 grid 
	 * even on modern i7 processors.
	 * 
	 * I therefore propose a limit of 30x10 (x, y respectively).
	 * which is 300 tiles.
	 */
	public void draw ( RenderTarget rendertarget, RenderStates renderstates )
	{
		for (int x = 0; x < m_tiles.size(); ++x)
		{
			for (int y = 0; y < m_tiles.get(x).size(); ++y)
			{
				rendertarget.draw(m_tiles.get(x).get(y));
			}
		}
		rendertarget.draw(m_crimson);
		
		for (MagmaGeyser m : m_geysers)
			rendertarget.draw(m);
	}
}














