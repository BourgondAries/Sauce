package engine;


import org.jsfml.graphics.RectangleShape;


/**
 * This class represents an "infinite box"; which means that one can have a textures
 * rectangle which - if the screen moves - correctly maps the texture to the end points.
 * When we move even further, the part at the end is moved forward; thus giving the illusion
 * of an infinite texture. This is much more efficient than drawing an arbitrarily large
 * rectangleshape. (Seriously, it's a resource hog).
 * 
 * I let RectangleShape do all the hard work,...
 * 
 * TODO: Implement texture mapping. Currently only a colored rectangle is allowed :/
 * 
 * @author Thormod Myrvang
 *
 */
public class InfiniteBox extends RectangleShape
{	
	/**
	 * Puts the center of the rectangle at the x position.
	 */
	public void updateViaX ( float x )
	{
		super.setPosition( x - super.getSize().x / 2.f, super.getPosition().y );
	}
	
	/**
	 * Puts the center of the rectangle at the y position.
	 */
	public void updateViaY ( float y )
	{
		super.setPosition( super.getPosition().x, y - super.getSize().y / 2.f );
	}
}
