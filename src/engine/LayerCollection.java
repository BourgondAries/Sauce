package engine;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import engine.Layer;


/**
 * Class that contains a z-ordered list of different layers.
 * If a layer has a higher z value than another, all the 
 * objects in that top layer will be drawn over all other objects of
 * lower layers.
 * 
 * @author Kevin R. Stravers
 *
 */
public class LayerCollection implements Drawable
{
	public ArrayList<Pair<Layer, Integer>> 
		m_layers = new ArrayList<>();

	/**
	 * Add a layer to the LayerCollection.
	 * A lower z position defines a layer that
	 * is drawn behind a higher z_position
	 * @param layer The layer to be drawn.
	 * @param z_position Position compared to other layers, lower is behind others.
	 */
	public void add ( Layer layer, Integer z_position )
	{
		Pair<Layer, Integer> to_add = new Pair<>(layer, z_position);
		int index = findIndexOfUsingZPosition ( to_add );
		
		if (index < 0)
		{
			System.out.println("Added a layer at: " + (-index-1));
			m_layers.add(-index - 1, to_add);
		}
		else
		{
			m_layers.set(index, to_add);
		}
	}

	
	/**
	 * Removes a layer that exists.
	 * @param layer
	 */
	public void remove ( Layer layer )
	{
		int index = findIndexOfUsingReference ( new Pair<Layer, Integer>( layer, null ) );
		System.out.println("Raw layer finding: " + index);
		if (!(index < 0))
		{
			System.out.println("Removed a layer reference");
			System.out.println("The layer had the zval of : " + m_layers.get(index).second);
			m_layers.remove(index);
		}
	}
	
	/**
	 * Removes an element at a specified z-value.
	 * @param z_value The z-depth to remove from the list.
	 */
	public void remove ( Integer z_value )
	{
		int index = findIndexOfUsingZPosition (new Pair<Layer, Integer>( null, z_value ));
		if (index < 0)
		{
			return;
		}
		else
		{
			System.out.println("Removed a zvalue");
			m_layers.remove( index );
		}
	}
	
	/**
	 * This method returns the index of the layer depth.
	 * Searched for binarily.
	 * 
	 * @param pair The pair to search for.
	 * @return The index of the find, or the index if it were to exist. See javadoc binarySearch.
	 */
	private int findIndexOfUsingZPosition ( Pair<Layer, Integer> pair )
	{
		Comparator<Pair<Layer, Integer>> comparator = new Comparator<Pair<Layer, Integer>>()
		{
		      public int compare(Pair<Layer, Integer> lhs, Pair<Layer, Integer> rhs) 
		      {
		    	  return lhs.second.compareTo(rhs.second);
		      }
		};
		
		return Collections.binarySearch(m_layers, pair, comparator);
	}
	
	/**
	 * This method returns the index of the layer reference.
	 * Searched for binarily.
	 * 
	 * WARNING: Do not change this to binary search.
	 * The list is sorted according to the z-value depth, not the value of the layer reference.
	 * 
	 * @param pair The pair to search for, Integer has nothing to say.
	 * @return The index of the find, or the index if it were to exist. See javadoc binarySearch.
	 */
	private int findIndexOfUsingReference ( Pair<Layer, Integer> pair )
	{
		for ( int i = 0; i < m_layers.size(); ++i )
		{
			if ( m_layers.get(i).first == pair.first )
			{
				return i;
			}
		}
		return -1;
	}


	public void draw(RenderTarget target, RenderStates states) 
	{
		for ( Pair<Layer, Integer> layer : m_layers )
		{
			target.draw(layer.first);
		}
	}
}