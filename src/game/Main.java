package game;

import java.io.*;
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
		game_state = states.menu;
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
	public static int 			framerate = 60;
	public static java.util.ArrayList<engine.Pair<String, Long>>
								score_collection = new java.util.ArrayList<>();

	
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
		shaft, // When we travel upward in the game, comes after core
		enterscore
	}
	
	
	/**
	 * Store the score into the score file.
	 * 
	 */
	public static void storeScoreIntoFile()
	{
		try
		{
			FileWriter fstream = new FileWriter("score.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			for (engine.Pair<String, Long> x : score_collection)
			{
				out.write(x.first + "\n" + x.second);
			}
			out.close();
		}
		catch (Exception exc_obj)
		{
			System.err.println("Error: " + exc_obj.getMessage());
		}
	}

	
	private static void loadScoreData() throws IOException
	{
		BufferedReader buff = new BufferedReader(new FileReader("score.txt"));
		String line;
		boolean is_name = true;
		while ((line = buff.readLine()) != null) 
		{
			if (is_name)
				score_collection.add(new engine.Pair<String, Long>(line, null));
			else
				score_collection.get(score_collection.size() - 1).second = Long.valueOf(line);
			is_name = !is_name;
		}
		buff.close();
	}
	
	private void run()
	{
		try 
		{
			loadScoreData();
			for (engine.Pair<String, Long> x : score_collection)
			{
				System.out.println(x.first + ": " + x.second);
			}
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		TransmittableData data = new TransmittableData();
	
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
						data = (new Core()).run();
						break;
					case shaft:
						new Shaft(data);
						break;
					case enterscore:
						new EnterScore(data);
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
		storeScoreIntoFile();
		wnd.close();
		System.exit(0);
	}
	
	
	static String file2str ( String path ) throws IOException 
	{
	  byte encoded[] = Files.readAllBytes(Paths.get(path));
	  return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString(); // UTF-8 Everywhere!
	}
}
