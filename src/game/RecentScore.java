package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import engine.*;

public class RecentScore implements Drawable
{
	private Font m_font;
	private Layer m_layer = new Layer();
	private ArrayList<Text> m_objects = new ArrayList<>();
	private ArrayList<Long> m_object_times = new ArrayList<>();
	
	private static final long 
		CM_LIFETIME_IN_MILLIS = 2000,
		CM_FLOAT_SPEED = 1;
	
	public RecentScore()
	{
		try {
			m_font = PathedFonts.getFont(Paths.get("res/pixelmix.ttf"));
		} catch (IOException exc_obj) {
			exc_obj.printStackTrace();
		}
	}
	
	public void pushScore(long score_value, Vector2f position)
	{
		Text tx = new Text();
		tx.setString(String.valueOf(score_value));
		tx.setPosition(position);
		tx.setFont(m_font);
		m_objects.add(tx);
		m_layer.add(tx);
		m_object_times.add(System.currentTimeMillis());
	}
	
	public void update()
	{
		if (m_object_times.size() > 0)
		{
			if ( m_object_times.get(0) + CM_LIFETIME_IN_MILLIS < System.currentTimeMillis() )
			{
				m_object_times.remove(0);
				m_layer.remove(m_objects.get(0));
				m_objects.remove(0);
			}
			for (Text x : m_objects)
				x.move(0, -CM_FLOAT_SPEED);
		}
	}
	
	public void draw(RenderTarget target, RenderStates states) 
	{
		target.draw(m_layer);
	}
	
}
