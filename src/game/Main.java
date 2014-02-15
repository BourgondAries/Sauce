package game;

import javax.vecmath.Vector3f;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;

import engine.DynamicObject;
import engine.Final;
import engine.Layer;
import engine.LayerCollection;
import engine.Pair;

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
		try {
		
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
						new Scoreboard();
						break;
					default:
						return;
				}
			}
			dispose();
		
		} catch (Exception exc_obj)
		{
			exc_obj.printStackTrace();
		}
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
		DynamicObject b = new DynamicObject();
		b.setSize(new Vector2f(30, 30));
		b.setRotation(30.f);
		Vector3f i = new Vector3f(0.f, 0.f, -40.001f);
		i.add(b.getSpeed().data);
		b.setSpeed(i);
		b.setOrigin(new Vector2f(15, 15));
		b.setPosition( 300, 300 );
		b.update();
		
		RenderWindow wnd = new RenderWindow(new VideoMode(800, 600, 32), "Title");
		wnd.setFramerateLimit(60);
		
		while (true)
		{ 
			wnd.clear();
			wnd.draw(b);
			wnd.display();
	
			
			b.update();
		}
//		new Main();
	}
}


























