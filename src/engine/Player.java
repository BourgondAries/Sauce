package engine;

import game.Main;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

// Kunne du gjøre variabelnavnene litt mer selv-forklarende? aner ikke hva m_rectangle_shape er, ellers liker jeg det jeg ser :)
// Done! :D Morsom side-effekt!

public class Player implements Transformable, Drawable
{
	private boolean m_was_in_lava_past_iteration = false; // Last in the air, true = magma er på jordoverflaten >.<
	private XYAxes m_change_in_position = new XYAxes(0.f, 0.f);
	
	// CM: C stands for "Constant", M stands for "Class Member"
	public static final float CM_HEIGHT = 50.f, CM_WIDTH = 30.f, CM_JUMPFORCE = -50.f;
	
	public Player()
	{
		m_rectangle_shape.setSize(new Vector2f(CM_WIDTH, CM_HEIGHT));
		m_rectangle_shape.setFillColor(new Color(230, 200, 150));
		m_rectangle_shape.setPosition(Main.wnd.getSize().x / 2.f, Main.wnd.getSize().y / 2.f);
	}
	
	public XYAxes getQueryMovement()
	{
		return m_change_in_position;
	}
	
	public String toString()
	{
		return "Player position: (" + m_rectangle_shape.getPosition().x + ", " + m_rectangle_shape.getPosition().y + ")"; 
	}

	
	public void logic()
	{
		m_rectangle_shape.move(m_change_in_position.getX(), m_change_in_position.getY());
//		if (Main.START_OF_MAGMA < m_rectangle_shape.getPosition().y && m_rectangle_shape.getPosition().y < Main.BOTTOM_OF_THE_WORLD - m_rectangle_shape.getSize().y)
//		{
//			if (m_was_in_lava_past_iteration == false)
//			{
//				dY = 0.f;
//				m_was_in_lava_past_iteration = true;
//			}
//			dY += 0.04f;
//			m_rectangle_shape.move(0, dY);
////			System.out.println("Magma");
//		}
//		else if (m_rectangle_shape.getPosition().y < Main.BOTTOM_OF_THE_WORLD - m_rectangle_shape.getSize().y)
//		{
//			if (m_was_in_lava_past_iteration == true)
//			{
//				m_was_in_lava_past_iteration = false;
//			}
//			dY += 0.0981f;
//			m_rectangle_shape.move(0, dY);
////			System.out.println("air");
//		}
//		else
//		{
//			dY = 0.f;
//			m_rectangle_shape.setPosition(m_rectangle_shape.getPosition().x, Main.BOTTOM_OF_THE_WORLD - m_rectangle_shape.getSize().y);
//		}
	}
	
	public void jump()
	{
		if (m_change_in_position.getY() == 0.f)
		{
			m_change_in_position.setY(CM_JUMPFORCE);
			m_rectangle_shape.move(0, m_change_in_position.getY());
		}
	}
		
	
	public void draw(RenderTarget t, RenderStates s)
	{
		t.draw(m_rectangle_shape);
	}
	
	public void rotate(float deg)
	{
		
	}
	
	public Vector2f getPosition()
	{
		return m_rectangle_shape.getPosition();
	}
	
	public Vector2f getScale()
	{
		return m_rectangle_shape.getScale();
	}
	
	public void setOrigin(Vector2f o)
	{
		m_rectangle_shape.setOrigin(o);
	}
	
	public void setOrigin(float x, float y)
	{
		m_rectangle_shape.setOrigin(x, y);
	}
	
	
	private RectangleShape m_rectangle_shape = new RectangleShape();


	public Transform getInverseTransform() 
	{
		return m_rectangle_shape.getInverseTransform();
	}

	public Vector2f getOrigin() 
	{
		return m_rectangle_shape.getOrigin();
	}

	public float getRotation()
	{
		return m_rectangle_shape.getRotation();
	}

	public Transform getTransform()
	{	
		return m_rectangle_shape.getTransform();
	}

	public void move(Vector2f coord)
	{
		m_rectangle_shape.move(coord);
	}

	public void move(float x, float y)
	{
		m_rectangle_shape.move(x, y);
	}

	public void scale(Vector2f scale)
	{
		m_rectangle_shape.scale(scale);
	}

	public void scale(float x, float y)
	{
		m_rectangle_shape.scale(x, y);
	}

	public void setPosition(Vector2f pos)
	{
		m_rectangle_shape.setPosition(pos);
	}

	public void setPosition(float x, float y)
	{
		m_rectangle_shape.setPosition(x, y);
	}

	public void setRotation(float r)
	{
		m_rectangle_shape.setRotation(r);
	}

	public void setScale(Vector2f scale)
	{
		m_rectangle_shape.setScale(scale);
	}

	public void setScale(float x, float y)
	{
		m_rectangle_shape.setScale(x, y);
	}
}
