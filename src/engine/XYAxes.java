package engine;


/**
 * 
 * @author Thormod Myrvang
 *
 */
public class XYAxes 
{
	protected float x, y;
	
	public XYAxes (float x, float y)
	{
		setX(x);
		setY(y);
	}
	
	public void add ( XYAxes axel )
	{
		setX( x + axel.getX() );
		setY( y + axel.getY() );
	}
	
	public void div ( float value )
	{
		setX ( x / value );
		setY ( y / value );
	}
	
	public void reset()
	{
		setX (0);
		setY (0);
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void addX(float x)
	{
		this.x += x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void addY(float y) {
		this.y += y;
	}
}
