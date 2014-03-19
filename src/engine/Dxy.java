package engine;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dxy
{
	private String m_dxy_data;
	
	public Dxy ( )
	{}


	public Dxy ( Dxy dxy )
	{
	    m_dxy_data = dxy.m_dxy_data;
	}

	public Dxy ( String dxy_data )
	{
	    m_dxy_data = dxy_data ;
	}


	public void setData ( String dxy_data )
	{
	    m_dxy_data = dxy_data;
	}

	public String getData ( )
	{
	    return m_dxy_data;
	}

	void uncomment ( )
	{
//	    ArrayList<String> matches = new ArrayList<String>();
//		String regex = "(.*?)((//.*?)(\n)|(/\\*.*?\\*/)|(\"([^\"]|\"\")*\")|([^\\w]))";
//        
//        Matcher m = Pattern.compile(regex).matcher(m_dxy_data);
//        while ( m.find ( ) )
//        {
//        	if ( m.group(1) != null)
//        		matches.add(m.group(1));
//            if ( m.group(4) != null)
//            	matches.add(m.group(4));
//            if ( m.group(6) != null)
//            	matches.add(m.group(6));
//            if ( m.group(8) != null)
//            	matches.add(m.group(8));
//
//        }
//		return matches;
	}


	public ArrayList<String> tokenize ( )
	{
		ArrayList<String> matches = new ArrayList<String>();
		String regex = "(\")?(?:(1)([^\"]|\"\")*\"|\\w+)";
        
        Matcher m = Pattern.compile("(" + regex + ")").matcher(m_dxy_data);
        while(m.find())
        {
            matches.add(m.group(1));
        }
		return matches;
	}


	public String toString ( )
	{
	    return m_dxy_data;
	}

}
