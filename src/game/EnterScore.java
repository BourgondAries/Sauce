package game;

import java.io.*;
import java.nio.file.Paths;

import org.jsfml.audio.Music;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import engine.Layer;
import engine.PathedFonts;
import engine.PathedTextures;

public class EnterScore 
{
	private Layer
		m_layer = new Layer();
	private Text 
		m_description = new Text(),
		m_name = new Text(),
		m_score = new Text(),
		m_max_score = null;
	private Sprite
		m_background = new Sprite();
	private RectangleShape m_textbox = new RectangleShape();
	private final int 
		CM_BACKSPACE = 8,
		CM_ENTER = 13;
	private long 
		m_score_value;
	Boolean[]
		flipflop = new Boolean[3];
	
	private Music
		m_music = new Music();
	
	public EnterScore(TransmittableData data) throws IOException
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
		
		
		if (Main.getMaxScore() < m_score_value)
		{
			m_max_score = new Text();
			m_music.openFromFile(Paths.get("sfx/xXxTurnDown4(Hwut)20DankScopeFazeClanTryoutsxXx.flac"));
			m_max_score.setString("You're the new #1 [[MLG 420 erryday]] 1337 #swag #NoScOpE pro!");
			m_max_score.setFont(PathedFonts.getFont(Paths.get("res/pixelmix.ttf")));
			m_max_score.setPosition(Main.wnd.getSize().x / 2 - m_max_score.getGlobalBounds().width / 2, Main.wnd.getSize().y / 2);
			
			
			Text tx = new Text();
			tx.setString(m_max_score.getString());
			tx.setFont(m_max_score.getFont());
			tx.setPosition(m_max_score.getPosition());
			tx.move(new Vector2f(-3.f, -3.f));
			tx.setColor(Color.BLACK);
			m_layer.add(tx);
			
			m_layer.add(m_max_score);
			
			for (int i = 0; i < flipflop.length; ++i)
				flipflop[i] = new Boolean(false);
		}
		else
		{
			m_music.openFromFile(Paths.get("sfx/core_background.ogg"));
		}
		m_music.setLoop(true);
		m_music.play();
		
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
				m_music.stop();
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
		if (m_max_score != null)
		{
			Color c = m_max_score.getColor();
			if (c.r >= 250 )
				flipflop[0] = false;
			if (c.g >= 250)
				flipflop[1] = false;
			if (c.b >= 250)
				flipflop[2] = false;
			
			if (c.r <= 127 )
				flipflop[0] = true;
			if (c.g <= 127)
				flipflop[1] = true;
			if (c.b <= 127)
				flipflop[2] = true;
			
			m_max_score.setColor
			(
				new Color
				(
					(flipflop[0]) ? m_max_score.getColor().r + 1 : m_max_score.getColor().r - (int)(1 + Math.random() * 5)
					, (flipflop[1]) ? m_max_score.getColor().g + 2 : m_max_score.getColor().g - (int)(2 + Math.random() * 5)
					, (flipflop[2]) ? m_max_score.getColor().b + 3 : m_max_score.getColor().b - (int)(3 + Math.random() * 5)
				)
			);
		}
	}
	
	private void drawFrame()
	{
		Main.wnd.clear();
		Main.wnd.draw(m_layer);
		Main.wnd.display();
	}
	
}