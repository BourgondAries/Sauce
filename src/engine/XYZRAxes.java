package engine;

public class XYZRAxes extends XYZAxes {
	protected float rotation_cv;
	
	public XYZRAxes(float x, float y, float z, float rotation_cv) {
		super(x, y, z);
		this.rotation_cv = rotation_cv;
	}

	public float getRotation() {
		return rotation_cv;
	}

	public void setRotation(float rotation_cv) {
		this.rotation_cv = rotation_cv;
	}
}
