package game;

//Class-specific import
import org.jsfml.graphics.*;
import org.jsfml.window.event.Event;
import engine.MusicManager;

public class Menu
{
	public Menu()
	{
		run();
	}
	
	public void run()
	{
		MusicManager mu = new MusicManager();
		
		

		boolean x = false;
		while (true)
		{
			for (Event event : Main.wnd.pollEvents())
			{
				switch (event.type)
				{
					case KEY_PRESSED:
					{
						x = !x;
						if (x)
							mu.fade("res/Rayman_2_music_sample.ogg");
						else
							mu.fade("res/Tromboon-sample.ogg");
					} break;
					case CLOSED:
					{
						Main.game_state = Main.states.scoreboard;
						Main.wnd.close();
						return;
					}
					default:
						break;
				}
			}
			
			Main.wnd.clear(Color.RED);
			Main.wnd.display();
		}
	}
}
