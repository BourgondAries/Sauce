package game;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;
import org.jsfml.audio.*;
import org.jsfml.window.event.Event;


public class Core
{
	public Core()
	{
		System.out.println("CORE\n");
		m_rs.setFillColor(new Color(200, 200, 200));
		m_rs.setSize(new Vector2f(10, 10));
		
		m_items.add(m_rs);
		
		run();
	}
	
	public void run()
	{
		while (Main.game_state == Main.states.core)
		{
			handleEvents();
			runGameLogic();
			render();
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
					System.out.println("Pressed a key!");
					m_rs.move(new Vector2f(0.f, -1.f));
				} break;
			}
		}
	}
	
	private void runGameLogic()
	{
		m_rs.move(new Vector2f(0.f, 1.f));
	}
	
	private void render()
	{
		Main.wnd.clear();
		
		for (Drawable x : m_items)
		{
			Main.wnd.draw(x);
		}
		
		Main.wnd.display();
	}
	
	private java.util.ArrayList<Drawable> m_items = new java.util.ArrayList<>();
	private final float m_gravity = -9.81f;
	
	private RectangleShape m_rs = new RectangleShape();
}
