package engine;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.ArrayList;

import org.jsfml.audio.SoundBuffer;


/**
 * Stores unique sounds in the memory
 */

public class PathedSounds
{
	private static ArrayList<WeakReference<SoundBuffer>> 	sounds = new ArrayList<>();
	private static ArrayList<Path> 						paths	 = new ArrayList<>();
	
	/**
	 * If the sound has already been loaded;
	 * returns a reference to the loaded sound instead.
	 * This allows an efficient use of multiple sounds.
	 * @param path The path where the sound file is located.
	 * @return the sound that is to be used.
	 * @throws IOException when the specified file is not found.
	 */
	public static SoundBuffer getSound ( Path path ) throws IOException
	{
		// Check if the sound is already loaded
		for (int i = 0; i < paths.size(); i++)
		{
			if (paths.get(i).equals(path))
			{
				SoundBuffer hard_reference = sounds.get(i).get();
				if ( hard_reference == null )
				{
					sounds.remove(i);
					paths.remove(i);
					break;
				}
				else
					return hard_reference;
			}
		}
		
		// The image is not loaded, it's a new texture,load from HDD:
		SoundBuffer sb = new SoundBuffer();
		sb.loadFromFile(path);
		sounds.add(new WeakReference<>(sb));
		paths.add(path);
		return sb;
	}
	
}
