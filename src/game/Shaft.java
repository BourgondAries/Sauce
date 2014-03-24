package game;

public class Shaft
{
	Shaft()
	{
		run();
	}
	
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
		while ( Main.game_state == Main.states.shaft );
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
		
	}
	
}
