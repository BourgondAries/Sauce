package game;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.*;
import org.jsfml.window.*;



public class Main
{
	
	// Entry point of the program
	public static void main ( String[] args ) throws IOException
	{	
		new Main();
	}

	public static long getMaxScore()
	{
		Long u = 0L;
		for (engine.Pair<String, Long> x : score_collection)
			if (x != null)
				if 
				(
					!x.first.equals("God Tier") 
					&& !x.first.equals("Alexander The Great Tier") 
					&& !x.first.equals("Legendary Tier")
				)
					if (x.second != null)
						if ( x.second > u )
							u = x.second;
		return u;
	}
	
	public static void sortScores()
	{
		ArrayList<engine.Pair<String, Long>> copy = new ArrayList<>();
		
		while (score_collection.size() > 0)
		{	
			int index = -1;
			engine.Pair<String, Long> max = new engine.Pair<String, Long>("", -1L);
			for (int i = 0; i < score_collection.size(); ++i)
			{
				if (score_collection.get(i) == null)
				{
					score_collection.remove(i);
					break;
				}
				if (score_collection.get(i).second > max.second)
				{
					max = score_collection.get(i);
					index = i;
				}
			}
			if (index >= 0)
				score_collection.remove(index);
			copy.add(max);
		}
		
		score_collection = copy;
	}
	
	// Entry point of the game
	public Main() throws IOException
	{
		System.setProperty("line.separator", "\n");
		game_state = states.menu;
		wnd = new RenderWindow(new VideoMode(1920, 1000, 32), "Shact");
		Image img = new Image();
		img.loadFromFile(Paths.get("res/logo.png"));
		wnd.setIcon(img);
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
		postshaft,
		enterscore
	}
	
	
	public static String encode(String x)
	{
		StringBuilder builder = new StringBuilder();
		int i = 300;
		for (char ch : x.toCharArray())
		{
			builder.append(((char)(ch + ++i)));
		}
		return builder.toString();
	}
	
	public static String decode(String x)
	{
		StringBuilder builder = new StringBuilder();
		int i = 300;
		for (char ch : x.toCharArray())
		{
			builder.append(((char)(ch - ++i)));
		}
		return builder.toString();
	}
	
	/**
	 * Store the score into the score file.
	 * 
	 */
	public static void storeScoreIntoFile()
	{
		try
		{
			PrintWriter fstream = new PrintWriter(new File("score.txt"), "UTF-8");
			BufferedWriter out = new BufferedWriter(fstream);
			for (engine.Pair<String, Long> x : score_collection)
			{
				out.write(encode(x.first) + "\n" + encode(String.valueOf(x.second)) + "\n");
			}
			out.close();
		}
		catch (Exception exc_obj)
		{
			System.err.println("Error: " + exc_obj.getMessage());
		}
	}
	
	
	/**
	 * Load the score from the score file into "score_collection"
	 * @throws IOException
	 */
	private static void loadScoreData() throws IOException
	{
		BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream("score.txt"), "UTF-8"));
		String line;
		boolean is_name = true;
		while ((line = buff.readLine()) != null) 
		{
			if (line.length() == 0)
				continue;
			if (is_name)
			{
				score_collection.add(new engine.Pair<String, Long>(decode(line), null));
			}
			else
			{
				try
				{
					score_collection.get(score_collection.size() - 1).second = Long.valueOf(decode(line));
				}
				catch (Exception exc_obj)
				{
					score_collection.remove(score_collection.size() - 1);
				}
			}
			is_name = !is_name;
		}
		buff.close();
	}
	
	
	private void run()
	{
			
		try 
		{
			loadScoreData();
			
			// Printns all scores retrieved from the file score.txt
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
					case postshaft:
						new PostShaftCinematic(data);
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
