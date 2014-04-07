package engine;

import org.jsfml.graphics.*;

/**
 * An absolutely positioned item on the screen. This "absolute" item simply
 * holds an internal "native" view; which is used to draw on a rendertarget.
 * This class implements the necessary utilities to accomplish this.
 * 
 * Best used by extending a class (example: HUD) and delegate a Layer to
 * super.draw. The reason for using a layer is so that the view is only
 * changed once, instead of multiple times (thus causing overhead, altho small.
 * AFAIK setting views is only an (C++) std::shared_ptr<sf::View> equivalent
 * copy in Java.
 * 
 * @author bourgondaries
 *
 */
public class Absolute
{
	private ConstView m_view;
	
	public void setAbsoluteView ( ConstView view )
	{
		m_view = view;
	}
	
	protected <T extends Drawable> void draw  ( RenderTarget target, T drawable )
	{
		ConstView oldview = target.getView();
		target.setView(m_view);
		target.draw(drawable);
		target.setView(oldview);
	}
}
