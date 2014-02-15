package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import java.util.ArrayList;

import engine.Pair;
import engine.Utilities;


/**
 * This represents the bottom of the world.
 * It will be a chunk that is re-generated if invalid coordinates are accessed.
 */
public class BottomOfTheWorld implements Drawable
{
	
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
		CM_TILE_COUNT_X = 30,
		CM_TILE_COUNT_Y = 10;
	
	/**
	 * This 2D array contains all the tiles that are to be drawn.
	 * Each list of RectangleShapes is a single column (y-axis).
	 * Every tile has its own position stored in it.
	 * position 0 is always to the left.
	 * position 0 on the columns is always the top block.
	 */
	private ArrayList< ArrayList<RectangleShape> >
		m_tiles = new ArrayList <> ( );
		
	/**
	 * The bounds describe the outer bounds of the "ground" in
	 * pixel coordinates in the 2D plane.
	 * 
	 * This has important uses; to determine when we are out of
	 *  - or nearly - bounds, and we need to regenerate the terrain
	 *  in a certain direction.
	 */
	private Pair<Integer> 
		m_x_bounds = new Pair<>(0, (int) (((float) CM_TILE_COUNT_X) * CM_TILE_WIDTH));
	
	/**
	 * Empty default constructor. Nothing to construct.
	 */
	public BottomOfTheWorld ( )
	{ }
	
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
								CM_TILE_HEIGHT * y + Main.BOTTOM_OF_THE_WORLD
						)
				);
				temporary_rectangleshape.setOutlineColor(new Color(1, 0, 255));
				temporary_rectangleshape.setOutlineThickness(1.f);
				
				m_tiles.get(x).add(temporary_rectangleshape);
			}
		}
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
	 * TODO: Implement premature decay thanks to time.
	 */
	public void generateLeft()
	{
		final int AMOUNT_OF_SHIFTED_TILES = 10;
		
		// First, get the current left most tile x position:
		float prevx = m_tiles.get(0).get(0).getPosition().x;
		
		// Lets try to move the 10 rightmost ones to the front:
		for (int x = CM_TILE_COUNT_X - AMOUNT_OF_SHIFTED_TILES; x < CM_TILE_COUNT_X; ++x)
		{
			m_tiles.add(0, m_tiles.get(x));
			m_tiles.remove(x);
		}
		
		System.out.println("Translating");
		
		// Now translate the x coordinates:
		for (int x = 0; x < AMOUNT_OF_SHIFTED_TILES; ++x)
		{
			System.out.println(x);
			for (int y = 0; y < CM_TILE_COUNT_Y - 1; ++y)
			{
				System.out.println("y: " + y);
				System.out.println("Size of the array in x: " + m_tiles.get(x).size());
				m_tiles.get(x).get(y).setPosition(prevx - (CM_TILE_COUNT_X - x) * CM_TILE_WIDTH, m_tiles.get(x).get(y).getPosition().y);
			}
		}
		
		m_x_bounds.first = (int) m_tiles.get(0).get(0).getPosition().x;
		
	}
	
	public void generateRight()
	{
		
	}
	
	public Pair<Integer> getXBounds ( )
	{
		return m_x_bounds;
	}
	
	/**
	 * Remove a random tile at the top of a column.
	 * This function returns the tile's position, thus
	 * allowing the creation of particle effects in that location.
	 * 
	 * Do note that the location is the upper left corner
	 * of the tile.
	 */
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
	
	/**
	 * Method that converts an x coordinate in world space
	 * to a row index of the tiles.
	 * 
	 * @param x_position The position in world space
	 * @return The index of the row where that x coordinate falls on.
	 */
	public int getTheTopOfTheTileLine ( int x_position )
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
				System.out.println("REGENERATE LEFT");
//				regenerateAround((int) position.x);
				generateLeft();
				return doesATileExistHere(position);
			}
			else if ( position.x > (getXBounds().second - 20))
			{
				System.out.println("REGENERATE RIGHT");
				regenerateAround((int) position.x);
				return doesATileExistHere(position);
			}
		}
		else
		{
			// Fetch the size of the linear array of tiles on the y axis
			height = (int) (m_tiles.get(x_index_to_check).size() * CM_TILE_HEIGHT);
		}
		
		return Main.BOTTOM_OF_THE_WORLD - position.y - height > (-280);
	}
	
	// Returns the position of the nearest, top tile
	public Vector2f getCollisionTilePosition(Vector2f position)
	{
		int x_index_to_check = getTheTopOfTheTileLine((int) position.x);
		
		if (m_tiles.get(x_index_to_check).size() > 0 == false)
			return new Vector2f(0.f, 0.f);
		int height = (int) m_tiles.get(x_index_to_check).get(0).getPosition().y;
//		System.out.println(height);
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














