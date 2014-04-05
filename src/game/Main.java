package game;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsfml.graphics.*;
import org.jsfml.window.*;


public class Main
{
	// Entry point of the program
	public static void main ( String[] args ) throws IOException
	{	
		new Main();
	}
	
	
	
	
	
	
	
	// Entry point of the game
	public Main()
	{
		game_state = states.shaft;
		wnd = new RenderWindow(new VideoMode(1920, 1000, 32), "Shact");
		view = new View ( wnd.getDefaultView().getCenter(), wnd.getDefaultView().getSize() );
		wnd.setFramerateLimit(60);
		wnd.setView(view);
		this.run();
	}
	
	
	// Globals. All globals should be in Main.
	public static states 		game_state;
	public static View 			view;
	public static RenderWindow 	wnd;

	
	public enum states
	{
		// Menu states
		menu, // The menu state, with this active, menu will be entered from the Main method.
		scoreboard, // Scoreboard of the game, a simple window
		options, // Volume, some other stuff
		about, // To feed our ego
		exit,
		
		// The actual game states:
		tutorial, // Travel downward from the satellite. 
		surface, // We've reached the surface, commence downward travel
		core, // Core gameplay
		shaft // When we travel upward in the game, comes after core
	}

	
	private void run()
	{
		try {
		
			// Run program until close
			while ( wnd.isOpen ( ) )
			{		
				// Initialize and run a state in the game
				switch ( game_state )
				{
					case menu:
						new Menu();
						break;
					case tutorial:
						new Tutorial();
						break;
					case surface:
						break;
					case core:
						new Core();
						break;
					case shaft:
						new Shaft();
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
		wnd.close();
		System.exit(0);
	}
	
	
	static String file2str ( String path ) throws IOException 
	{
	  byte encoded[] = Files.readAllBytes(Paths.get(path));
	  return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString(); // UTF-8 Everywhere!
	}
}
