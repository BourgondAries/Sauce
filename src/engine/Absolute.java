package engine;

import org.jsfml.graphics.*;

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
