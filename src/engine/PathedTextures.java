package engine;


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

public class PathedTextures {
	private static ArrayList<Texture> textures;
	private static ArrayList<Path> pathes;
	
	public static Sprite addImage(Path path) throws IOException {
		// Check if the image is already loaded
		for (int i = 0; i < pathes.size(); i++) {
			if (pathes.get(i) == path) {
				return new Sprite(textures.get(i));
			}
		}
		
		// It's a new texture, add to memory
		Texture tex = new Texture();
		tex.loadFromFile(path);
		textures.add(tex);
		pathes.add(path);
		return new Sprite(textures.get(textures.size()-1));
	}
	
}
