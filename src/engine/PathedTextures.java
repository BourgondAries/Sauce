package engine;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.ArrayList;

import org.jsfml.graphics.Texture;


/**
 * Manages unique textures. Textures requested that are
 * already in the Pathed Textures are to be returned instead
 * of re-loaded from the local filesystem (which is a slow process).
 * 
 * DONE: TODO: cleanup algorithm for removing no longer requested textures.
 * The problem with this is that 
 * 
 * How cleanup works: fully automated; PathedTextures only holds weak references.
 * This means that: if all strong references are gone, the object is deleted from the list.
 * Else, a new strong reference is returned, which delays the deletion of 
 * texture.
 * 
 * The texture will be deleted when all references are deleted.
 */
public class PathedTextures 
{
	private static ArrayList<WeakReference<Texture>> 	textures = new ArrayList<>();
	private static ArrayList<Path> 						paths	 = new ArrayList<>();
	
	/**
	 * If the image has already been loaded;
	 * returns a reference to the loaded texture instead.
	 * This allows an efficient use of multiple textures.
	 * @param path The path where the texture file is located.
	 * @return the texture that is to be used.
	 * @throws IOException when the specified file is not found.
	 */
	public static Texture getTexture ( Path path ) throws IOException
	{
		// Check if the image is already loaded, can't really binary search
		// unless using a hashing algorithm... It's likely fast enough for a
		// number of textures under 1000
		for (int i = 0; i < paths.size(); i++)
		{
			if (paths.get(i).equals(path))
			{
				Texture hard_reference = textures.get(i).get();
				if ( hard_reference == null )
				{
					textures.remove(i);
					paths.remove(i);
					break;
				}
				else
					return hard_reference;
			}
		}
		
		// The image is not loaded, it's a new texture,load from HDD:
		Texture tex = new Texture();
		tex.loadFromFile(path);
		textures.add(new WeakReference<>(tex));
		paths.add(path);
		return tex;
	}
	
}
