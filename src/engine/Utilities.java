package engine;

import java.util.ArrayList;

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
}
