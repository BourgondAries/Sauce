package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector3f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import ttl.Bool;
import engine.DynamicObject;
import engine.Layer;
import engine.LayerCollection;
import engine.PathedTextures;

public class EnterScore 
{
		
	public EnterScore()
	{
		
		run();
	}
	
	private void run()
	{
		do
		{
			handleEvents();
			runGameLogic();
			updateObjects();
			if (Main.game_state != Main.states.shaft)
				return;
			drawFrame();
		}
		while ( Main.game_state == Main.states.shaft );
	}
	
	private void handleEvents()
	{
		for (Event event : Main.wnd.pollEvents())
		{
			switch (event.type)
			{
				case KEY_PRESSED:
				{
					KeyEvent keyev = event.asKeyEvent();
					switch (keyev.key)
					{
						case ESCAPE:
							Main.game_state = Main.states.menu;
							return;
						default:
							break;
					
					}
				} break;
				case KEY_RELEASED:
				{
					KeyEvent keyev = event.asKeyEvent();
					switch (keyev.key)
					{
						default:
							break;
					}
				} break;
			default:
				break;
			}
		}
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