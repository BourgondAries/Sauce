package game;

/**
 * Class holds data that is returned by core.
 * @author bourgondaries
 *
 */
public class TransmittableData 
{
	public TransmittableData()
	{
		score = 0;
		difficulty = 0.;
	}
	
	public long		score;
	public double 	difficulty;
	public HUD 		hud = new HUD(Main.wnd);
	public Player 	ship = new Player();
}
