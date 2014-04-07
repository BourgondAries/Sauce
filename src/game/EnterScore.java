package game;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector3f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import ttl.Bool;
import engine.DynamicObject;
import engine.Formulas;
import engine.Layer;
import engine.LayerCollection;
import engine.PathedFonts;
import engine.PathedTextures;

public class EnterScore 
{
	private Layer
		m_layer = new Layer();
	private Text 
		m_description = new Text(),
		m_name = new Text(),
		m_score = new Text();
	private Sprite
		m_background = new Sprite();
	private RectangleShape m_textbox = new RectangleShape();
	private final int 
		CM_BACKSPACE = 8,
		CM_ENTER = 13;
	private long 
		m_score_value;
	
	
	public EnterScore(TransmittableData data)
	{
		m_score_value = data.score;
		try 
		{
			m_name.setFont(PathedFonts.getFont(Paths.get("res/pixelmix.ttf")));
			m_score.setFont(m_name.getFont());
			m_description.setFont(m_name.getFont());
			
			Texture tex = PathedTextures.getTexture(Paths.get("res/end/splodin_planet.tga"));
			m_background.setTexture(tex, true);
			m_background.setScale(((float)Main.wnd.getSize().x) / ((float)tex.getSize().x), ((float)Main.wnd.getSize().y) / ((float)tex.getSize().y));
		} 
		catch (IOException exc_obj) 
		{
			exc_obj.printStackTrace();
		}
		
		m_textbox.setSize(new Vector2f(Main.wnd.getSize().x / 2, Main.wnd.getSize().y / 10));
		m_textbox.setFillColor(new Color(127,127,127,127));
		m_textbox.setOutlineColor(new Color(167, 52, 52, 152));
		m_textbox.setOutlineThickness(7);
		m_textbox.setPosition(new Vector2f(Main.wnd.getSize().x / 2 - m_textbox.getSize().x / 2, Main.wnd.getSize().y / 3 - m_textbox.getSize().y / 2));
		
		m_name.setPosition(m_textbox.getPosition());
		m_name.move(m_textbox.getSize().x / 10, m_textbox.getSize().y / 4);
		
		m_score.setString("Your Score: " + data.score);
		m_description.setString("Huzzah, The Fun Has Been Doubled! Enter your alias!");
		
		m_description.setPosition(new Vector2f(Main.wnd.getSize().x / 2 - m_description.getGlobalBounds().width / 2, m_description.getGlobalBounds().height / 2));
		m_score.setPosition(new Vector2f(Main.wnd.getSize().x / 2 - m_score.getGlobalBounds().width / 2, 2*(m_description.getGlobalBounds().height + m_description.getGlobalBounds().top)));
		
		m_layer.add(m_background);
		m_layer.add(m_description);
		m_layer.add(m_score);
		m_layer.add(m_textbox);
		m_layer.add(m_name);
		
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
			{
				Main.score_collection.add(new engine.Pair<String, Long>(m_name.getString(), m_score_value));
				return;
			}
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
					System.out.println((int)c);
					if ((int) c == CM_BACKSPACE) 
					{
						if (m_name.getString().length() > 0)
							m_name.setString(m_name.getString().substring(0, m_name.getString().length() - 1));
					}
					else if ((int) c != CM_ENTER)
					{
						m_name.setString(m_name.getString() + event.asTextEvent().character);
					}
					else
					{
						Main.game_state = Main.states.menu;
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
		Main.wnd.draw(m_layer);
		Main.wnd.display();
	}
	
}