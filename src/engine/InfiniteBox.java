package engine;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import game.Main;

public class InfiniteBox extends StaticObject {
	private float stopline;
	private boolean fill_top_or_left;
	private boolean horizontal;
	private boolean too_small_for_render;
	private RenderCam render_cam;
	
	public InfiniteBox(float x_or_y, float z, boolean is_roof_or_left_wall, boolean is_horizontal, RenderCam render_cam) {
		super(new XYZRAxes(0,0,z,0),new XYAxes(Main.wnd.getSize().x,Main.wnd.getSize().y));
		fill_top_or_left = is_roof_or_left_wall;
		stopline = x_or_y;
		horizontal = is_horizontal;
		this.render_cam = render_cam;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		updateBox();
		if (too_small_for_render) return;
		super.draw(target, states);
	}
	
	private void updateBox() {
		too_small_for_render = false;
		float[] view = render_cam.getRenderView(getZ());
		if (horizontal) {
			setX(view[0]);
			setWidth(view[2]);
			if (fill_top_or_left) {
				setY(view[1]);
				if (stopline<=view[1]) {
					too_small_for_render = true;
				} else if (stopline>=view[1]+view[3]) {
					setHeight(view[3]);
				} else {
					setHeight(stopline-getY());
				}
			} else {
				if (stopline<=view[1]) {
					setY(view[1]);
				} else if (stopline>=view[1]+view[3]) {
					too_small_for_render = true;
				} else {
					setY(stopline);
				}
				setHeight(view[3]-(getY()-view[1]));
			}
		} else {
			setY(view[1]);
			setHeight(view[3]);
			if (fill_top_or_left) {
				setX(view[0]);
				if (stopline<=view[0]) {
					too_small_for_render = true;
				} else if (stopline>=view[0]+view[2]) {
					setWidth(view[2]);
				} else {
					setWidth(stopline-getX());
				}
			} else {
				if (stopline<=view[0]) {
					setX(view[0]);
				} else if (stopline>=view[1]+view[3]) {
					too_small_for_render = true;
				} else {
					setX(stopline);
				}
				setWidth(view[2]-(getX()-view[0]));
			}
		}
	}
}
