package engine;

import java.util.ArrayList;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;


/**
 * A layer is a plane in 3D space that contains all drawables
 * on that plane.
 * 
 * @author Kevin R. Stravers
 *
 */
public class Layer implements Drawable, Comparable<Layer>
{
	private ArrayList<Drawable> m_drawables
		= new ArrayList<>();

	/**
	 * Add a drawable to the layer,
	 * it will be drawn the next time the layer is actually drawn.
	 * 
	 * @param drawable The drawable to add.
	 */
	public void add ( Drawable drawable )
	{
		if ( drawable == null )
			throw new IllegalArgumentException("Layer.add(Drawable): The drawable must be a valid (non-nullptr) reference.");
		m_drawables.add ( drawable );
	}
	
	/**
	 * We use the reference to remove a drawable from the plane.
	 * 
	 * @param drawable The drawable to remove.
	 */
	public void remove ( Drawable drawable )
	{
		m_drawables.remove ( drawable );	
	}
	
	/**
	 * Functions used by JSFML internally to draw this object.
	 */
	public void draw (RenderTarget rendertarget, RenderStates renderstates)
	{
		for ( int i = 0; i < m_drawables.size(); ++i )
			rendertarget.draw(m_drawables.get(i));
	}

	/**
	 * Compare 2 layers, used in binary search.
	 */
	public int compareTo(Layer lhs) 
	{
		return (lhs.m_drawables == this.m_drawables ? 0 : 1);
	}

}
