package engine;

public class XYZAxes extends XYAxes {
	protected float z;
	
	public XYZAxes(float x, float y, float z) {
		super(x,y);
		setZ(z);
	}
	
	public void add(XYZAxes axel) {
		setX(x+axel.getX());
		setY(y+axel.getY());
		setZ(z+axel.getZ());
	}
	
	public void div(float value) {
		setX(x/value);
		setY(y/value);
		setZ(z/value);
	}
	
	@Override
	public void reset() {
		setX(0);
		setY(0);
		setZ(0);
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	public void addZ(float z) {
		this.z += z;
	}
}
