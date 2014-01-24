package engine;

public class XYZAxes extends XYAxes {
	protected float z;
	
	public XYZAxes(float x, float y, float z) {
		super(x,y);
		this.z = z;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}
