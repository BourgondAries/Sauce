package engine;

import javax.vecmath.Vector2f;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RectangleShape;

import game.Main;


/**
 * This class represents an "infinite box"; which means that one can have a textures
 * rectangle which - if the screen moves - correctly maps the texture to the end points.
 * When we move even further, the part at the end is moved forward; thus giving the illusion
 * of an infinite texture. This is much more efficient than drawing an arbitrarily large
 * rectangleshape. (Seriously, it's a resource hog).
 * 
 * @author Thormod Myrvang
 *
 */
public class InfiniteBox extends RectangleShape
{
	private float 		stopline;				// X/Y-stop position in global coordinates
	private boolean 	fill_top_or_left;		// Top of the Y or left of the X line, yes, or no?
	private boolean 	horizontal;				// Whether the stopline is horiz or vertic.
	private boolean 	too_small_for_render;	// If it's outside of bounds.
	private RenderCam 	render_cam; 			// Doubting if needed.
	private Vector2f 	m_offset_from_center;	// Position from the center when re-calibrating the infinite box.
	
	public InfiniteBox ( float x_or_y, float z, boolean is_roof_or_left_wall, boolean is_horizontal, RenderCam render_cam ) 
	{
		super(new XYZRAxes(0, 0, z, 0),new XYAxes(Main.wnd.getSize().x, Main.wnd.getSize().y));
		fill_top_or_left = is_roof_or_left_wall;
		stopline = x_or_y;
		horizontal = is_horizontal;
		this.render_cam = render_cam;
	}
	
	/**
	 * 
	 * @param position
	 */
	public void centreAround ( Vector2f position )
	{
		
	}
	
	/**
	 * 
	 * @param offset_from_center
	 */
	public void setOffset ( Vector2f offset_from_center )
	{
		
	}
	
	/**
	 * Dark Magic
	 */
	private void updateBox()
	{
		too_small_for_render = false;
		float[] view = render_cam.getRenderView(getZ());
		if (horizontal) {
			setX(view[0]);
			setWidth(view[2]);
			if (fill_top_or_left) {
				setY(view[1]);
				if (stopline<=view[1]) {
					too_small_for_render = true;
				} else if (stopline>=view[1]+view[3]) {
					setHeight(view[3]);
				} else {
					setHeight(stopline-getY());
				}
			} else {
				if (stopline<=view[1]) {
					setY(view[1]);
				} else if (stopline>=view[1]+view[3]) {
					too_small_for_render = true;
				} else {
					setY(stopline);
				}
				setHeight(view[3]-(getY()-view[1]));
			}
		} else {
			setY(view[1]);
			setHeight(view[3]);
			if (fill_top_or_left) {
				setX(view[0]);
				if (stopline<=view[0]) {
					too_small_for_render = true;
				} else if (stopline>=view[0]+view[2]) {
					setWidth(view[2]);
				} else {
					setWidth(stopline-getX());
				}
			} else {
				if (stopline<=view[0]) {
					setX(view[0]);
				} else if (stopline>=view[1]+view[3]) {
					too_small_for_render = true;
				} else {
					setX(stopline);
				}
				setWidth(view[2]-(getX()-view[0]));
			}
		}
	}
}
