package engine;

public class XYZRAxes extends XYZAxes {
	protected float rotation_cw;
	
	public XYZRAxes(float x, float y, float z, float rotation_cv) {
		super(x, y, z);
		setRotation(rotation_cw);
	}
	
	public void add(XYZRAxes axel) {
		setX(x+axel.getX());
		setY(y+axel.getY());
		setZ(z+axel.getZ());
		setRotation(rotation_cw+axel.getRotation());
	}
	
	public void div(float value) {
		setX(x/value);
		setY(y/value);
		setZ(z/value);
		setRotation(rotation_cw/value);
	}
	
	@Override
	public void reset() {
		setX(0);
		setY(0);
		setZ(0);
		setRotation(0);
	}

	public float getRotation() {
		return rotation_cw;
	}

	public void setRotation(float rotation_cw) {
		this.rotation_cw = (float) (rotation_cw % 360);
		if (this.rotation_cw < 0) this.rotation_cw += 360;
	}
	
	public void addRotation(float rotation_cw) {
		this.rotation_cw = (this.rotation_cw + rotation_cw) % 360;
		if (this.rotation_cw < 0) this.rotation_cw += 360;
	}
}
