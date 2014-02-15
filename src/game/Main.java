package game;

import org.jsfml.graphics.*;
import org.jsfml.window.*;

public class Main
{
	
	// Setup variables
	public static states game_state;
	public static View view;
	public final static float BOTTOM_OF_THE_WORLD = 530.f;
	public final static float BOTTOM_OF_THE_WORLD_CEILING = -350.f;
	public final static float START_OF_MAGMA = 0.f;
	public static RenderWindow wnd;

	// Setup states in the game
	public enum states
	{
		menu, tutorial, surface, core, game, scoreboard;
	}

	private void run()
	{		
		while (wnd.isOpen())
		{		
			// Initialize and run a state in the game
			switch (game_state)
			{
				case menu:
					new Menu();
					break;
				case tutorial:
					break;
				case surface:
					break;
				case core:
					new Core();
					break;
				case game:
					break;
				case scoreboard:
					break;
				default:
					return;
			}
		}
		dispose();
	}
	
	public static void dispose() 
	{
		//Close the window
		wnd.close();
		
		// Exit the system
		System.exit(0);
	}

	public Main()
	{
		game_state = states.core;
		wnd = new RenderWindow(new VideoMode(800, 600, 32), "Sauce");
		view = new View(wnd.getDefaultView().getCenter(), wnd.getDefaultView().getSize());
		wnd.setFramerateLimit(60);
		wnd.setView(view);
	
		this.run();	
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
}
