package game;

import java.util.ArrayList;
import org.jsfml.graphics.*;
import org.jsfml.window.*;

public class Main
{
	// Setup variables
	public static int screen_width;
	public static int screen_height;
	public static String game_title;
	public static states game_state;
	public static RenderWindow wnd;
	public static View view;
	public static ArrayList<Texture> textures;
	
	// Setup states in the game
	public enum states
	{
		menu, tutorial, surface, core, game, scoreboard;
	}
	
	private void init()
	{
		screen_width = 800;
		screen_height = 600;
		game_title = "Sauce";
		game_state = states.core;
		wnd = new RenderWindow(new VideoMode(screen_width, screen_height, 32), game_title);
		view = new View(wnd.getDefaultView().getCenter(), wnd.getDefaultView().getSize());
		wnd.setFramerateLimit(60);
		wnd.setView(view);
	}
	
	private void run()
	{		
		// Run program until close
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
	}
	
	private void dispose() 
	{
		//Close the window
		wnd.close();
		
		// Exit the system
		System.exit(0);
	}

	public static void main(String[] args)
	{
		Main main = new Main();
		main.init();
		main.run();
		main.dispose();
	}
}
