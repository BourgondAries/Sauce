package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.*;
import org.jsfml.graphics.Text;
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
import engine.PathedFonts;
import engine.PathedTextures;

public class EnterScore 
{
	private Text m_name = new Text();
	private RectangleShape m_textbox = new RectangleShape();
	private final int CM_SPACE = 8;
	
	
	public EnterScore()
	{
		try 
		{
			m_name.setFont(PathedFonts.getFont(Paths.get("res/pixelmix.ttf")));
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		
		m_textbox.setSize(new Vector2f(Main.wnd.getSize().x / 2, Main.wnd.getSize().y / 10));
		m_textbox.setFillColor(new Color(0,0,0));
		m_textbox.setOutlineColor(new Color(167, 52, 52, 152));
		m_textbox.setOutlineThickness(7);
		m_textbox.setPosition(new Vector2f(Main.wnd.getSize().x / 2 - m_textbox.getSize().x / 2, Main.wnd.getSize().y / 3 - m_textbox.getSize().y / 2));
		
		m_name.setPosition(m_textbox.getPosition());
		m_name.move(m_textbox.getSize().x / 10, m_textbox.getSize().y / 4);
		
		run();
	}
	
	private void run()
	{
		do
		{
			handleEvents();
			runGameLogic();
			updateObjects();
			if (Main.game_state != Main.states.enterscore)
				return;
			drawFrame();
		}
		while ( true );
	}
	
	private void handleEvents()
	{
		for (Event event : Main.wnd.pollEvents())
		{
			switch (event.type)
			{
				case TEXT_ENTERED:
				{
					char c = event.asTextEvent().character;
					
					if ((int) c == CM_SPACE) 
					{
						if (m_name.getString().length() > 0)
							m_name.setString(m_name.getString().substring(0, m_name.getString().length() - 1));
					}
					else
					{
						m_name.setString(m_name.getString() + event.asTextEvent().character);
					}
					
				} break;
				default:
					break;
			}
		}
	}
	
	private void runGameLogic()
	{
		// If the appended character exceeds the size of the container, it shall forthwith omitted.
		{
			if (m_name.getString().length() > 2 && m_name.getGlobalBounds().width > m_textbox.getSize().x - m_textbox.getSize().x / 5)
				m_name.setString(m_name.getString().substring(0, m_name.getString().length() - 1));
		}
	}
	
	private void updateObjects()
	{
		
	}
	
	private void drawFrame()
	{
		Main.wnd.clear();
		Main.wnd.draw(m_textbox);
		Main.wnd.draw(m_name);
		Main.wnd.display();
	}
	
}