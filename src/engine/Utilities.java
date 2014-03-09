package engine;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;

public class Utilities
{
	public static <T> T getLastOf ( ArrayList<T> array_list )
	{
		return array_list.get(array_list.size() - 1);
	}
	
	public static <T> T getFirstOf ( ArrayList<T> array_list )
	{
		return array_list.get(0);
	}
	
	public static float getDistance(Vector2f lhs, Vector2f rhs)
	{
		return (float) Math.sqrt((lhs.x - rhs.x)*(lhs.x - rhs.x) + (lhs.y - rhs.y)*(lhs.y - rhs.y));
	}
}
