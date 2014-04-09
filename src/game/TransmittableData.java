package game;

/**
 * Class holds data that is returned by core.
 * This class is made in order to keep the number of globals down.
 * 
 * Shouldn't have used globals in the first place... forgive me for I have sinned.
 * 
 * @author bourgondaries
 *
 */
public class TransmittableData 
{
	public TransmittableData()
	{
		score = 0L;
		difficulty = 0.;
	}
	
	public long		score;
	public double 	difficulty;
	public HUD 		hud = new HUD(Main.wnd);
	public Player 	ship = new Player();
}
