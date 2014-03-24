package engine;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.ArrayList;

import org.jsfml.graphics.Font;


/**
 * Manages unique fonts. Fonts requested that are
 * already in the Pathed Fonts are to be returned instead
 * of re-loaded from the local filesystem (which is a slow process).
 * 
 * DONE: TODO: cleanup algorithm for removing no longer requested fonts.
 * The problem with this is that 
 * 
 * How cleanup works: fully automated; PathedFonts only holds weak references.
 * This means that: if all strong references are gone, the object is deleted from the list.
 * Else, a new strong reference is returned, which delays the deletion of 
 * font.
 * 
 * The font will be deleted when all references are deleted.
 * 
 * Man... "font" sounds so weird. Replaced all "texture" with "font". What a strange word.
 */
public class PathedFonts
{
	private static ArrayList<WeakReference<Font>> 	fonts = new ArrayList<>();
	private static ArrayList<Path> 					paths = new ArrayList<>();
	
	/**
	 * If the font has already been loaded;
	 * returns a reference to the loaded font instead.
	 * This allows an efficient use of multiple fonts.
	 * @param path The path where the font file is located.
	 * @return the font that is to be used.
	 * @throws IOException when the specified file is not found.
	 */
	public static Font getFont ( Path path ) throws IOException
	{
		// Check if the image is already loaded, can't really binary search
		// unless using a hashing algorithm... It's likely fast enough for a
		// number of fonts under 1000
		for (int i = 0; i < paths.size(); i++)
		{
			if (paths.get(i).equals(path))
			{
				Font hard_reference = fonts.get(i).get();
				if ( hard_reference == null )
				{
					fonts.remove(i);
					paths.remove(i);
					break;
				}
				else
					return hard_reference;
			}
		}
		
		// The image is not loaded, it's a new font,load from HDD:
		Font tex = new Font();
		tex.loadFromFile(path);
		fonts.add(new WeakReference<>(tex));
		paths.add(path);
		return tex;
	}
	
}
