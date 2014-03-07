package game;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Vector3f;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.audio.*;
import org.jsfml.window.event.*;

import engine.*;


public class Core 
{
	private Layer m_layer;
	private Player m_player;
	
	private BottomOfTheWorld m_bedrock;
	private InfiniteBox m_magma;
	
	private Random m_rng = new Random();
	
	static ArrayList<DynamicObject> physical_objects;
	
	public Core() throws IOException
	{
		m_player = new Player();
		m_player.setSize(new Vector2f(30, 30));
		m_player.setTexture(PathedTextures.addImage(Paths.get("res/drop2.png")));
		m_player.setOrigin(5, 5);
		m_player.setPosition(Main.wnd.getSize().x / 2, Main.wnd.getSize().y);
		m_player.setMass(10.f);
		
		m_bedrock = new BottomOfTheWorld ( );
		physical_objects = new ArrayList<DynamicObject>();
		
		System.out.println("CORE\n");
		
		m_bedrock.generateTiles();
		
		m_layer = new Layer();
		m_layer.add(m_player);
		m_layer.add(m_bedrock);
		
		m_player.setPosition(new Vector2f(0.f, 0.f));
//		m_layer.add(m_magma);
		
		run();
	}
	
	public void run()
	{
		while (Main.game_state == Main.states.core)
		{
			handleEvents();
			runGameLogic();
			updateObjects();
			drawFrame();
		}
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
						case UP:
							m_player.jump();
							break;
						case RETURN:
							m_bedrock.eraseRandomTileAtTheTop();
							break;
						case ESCAPE:
							Main.game_state = Main.states.menu;
							return;
						default:
							break;
					
					}
				} break;
			default:
				break;
			}
		}
		
		if (Keyboard.isKeyPressed(Keyboard.Key.LEFT)) 
		{
			m_player.fetchSpeed().x -= 7.f;
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) 
		{
			m_player.fetchSpeed().x += 7.f;
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) 
		{
			m_player.jump();
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.RETURN)) 
		{
			m_bedrock.eraseRandomTileAtTheTop();
		} 
		else if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) 
		{
			Main.game_state = Main.states.menu;
		}
		System.out.println(m_player);
		System.out.println(m_bedrock);
	}
	
	private void runGameLogic()
	{
		// Effect of gravity on the player
		m_player.fetchImpulse().y += 1.f;
	}

	private void updateObjects() 
	{
		m_player.update();
		m_player.fetchSpeed().x = 0.f;
		
		setViewToPlayer();
	}
	
		private void setViewToPlayer()
		{
			View v = Main.view;
			v = new View(m_player.getPosition(), Main.wnd.getDefaultView().getSize());
			v.move(m_rng.nextInt() % 2 - 1, m_rng.nextInt() % 2 - 1);
			Main.wnd.setView(v);
		}
	
	private void drawFrame ( )
	{
		Main.wnd.clear();
		Main.wnd.draw(m_layer);
		Main.wnd.draw(m_bedrock);
		Main.wnd.display();
	}
}
