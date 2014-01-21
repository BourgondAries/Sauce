package engine;

import game.Main;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

// Kunne du gjøre variabelnavnene litt mer selv-forklarende? aner ikke hva m_rs er, ellers liker jeg det jeg ser :)

public class Player implements Transformable, Drawable
{
	private float dY = 0.f; //Og er ikke dette litt smør på flesk? :P
	
	public Player()
	{
		m_rs.setSize(new Vector2f(30, 50));
		m_rs.setFillColor(new Color(230, 200, 150));
		m_rs.setPosition(Main.wnd.getSize().x / 2.f, Main.wnd.getSize().y / 2.f);
	}
	
	public void logic()
	{
		if (m_rs.getPosition().y < 600.f - m_rs.getSize().y)
		{
			dY += 0.0981f;
			m_rs.move(0, dY);
		}
		else
		{
			dY = 0.f;
			m_rs.setPosition(m_rs.getPosition().x, 600.f - m_rs.getSize().y);
		}
	}
	
	public void jump()
	{
		if (dY == 0.f)
		{
			dY = -10.f;
			m_rs.move(0, dY);
		}
	}
		
	
	public void draw(RenderTarget t, RenderStates s)
	{
		t.draw(m_rs);
	}
	
	public void rotate(float deg)
	{
		
	}
	
	public Vector2f getPosition()
	{
		return m_rs.getPosition();
	}
	
	public Vector2f getScale()
	{
		return m_rs.getScale();
	}
	
	public void setOrigin(Vector2f o)
	{
		m_rs.setOrigin(o);
	}
	
	public void setOrigin(float x, float y)
	{
		m_rs.setOrigin(x, y);
	}
	
	
	private RectangleShape m_rs = new RectangleShape();


	public Transform getInverseTransform() 
	{
		return m_rs.getInverseTransform();
	}

	public Vector2f getOrigin() 
	{
		return m_rs.getOrigin();
	}

	public float getRotation()
	{
		return m_rs.getRotation();
	}

	public Transform getTransform()
	{	
		return m_rs.getTransform();
	}

	public void move(Vector2f coord)
	{
		m_rs.move(coord);
	}

	public void move(float x, float y)
	{
		m_rs.move(x, y);
	}

	public void scale(Vector2f scale)
	{
		m_rs.scale(scale);
	}

	public void scale(float x, float y)
	{
		m_rs.scale(x, y);
	}

	public void setPosition(Vector2f pos)
	{
		m_rs.setPosition(pos);
	}

	public void setPosition(float x, float y)
	{
		m_rs.setPosition(x, y);
	}

	public void setRotation(float r)
	{
		m_rs.setRotation(r);
	}

	public void setScale(Vector2f scale)
	{
		m_rs.setScale(scale);
	}

	public void setScale(float x, float y)
	{
		m_rs.setScale(x, y);
	}
}
