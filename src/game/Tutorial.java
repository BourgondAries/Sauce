package game;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

public class Tutorial
{
	Tutorial ( )
	{
		// We need a sky background, ship in the middle.
		// Sky must be moving.
		
		m_ship = new RectangleShape ( );
		m_ship.setSize(new Vector2f(200, 200));
		m_ship.setPosition(400 - 100, 300 - 100);
		
		run();
	}
	
	RectangleShape 
		m_ship, 
		m_sky;
	
	public void run()
	{
		do 
		{
			handleEvents();
			runGameLogic();
			updateObjects();
			drawFrame();
			
			Main.wnd.close();
			Main.game_state = Main.states.exit;
		}
		while ( Main.game_state == Main.states.tutorial );
	}
	
	private void handleEvents()
	{
		
	}
	
	private void runGameLogic()
	{
		
	}
	
	private void updateObjects()
	{
		
	}
	
	private void drawFrame()
	{
		Main.wnd.clear ( );
		Main.wnd.draw ( m_sky );
		Main.wnd.draw ( m_ship );
		Main.wnd.display ( );
	}
}
