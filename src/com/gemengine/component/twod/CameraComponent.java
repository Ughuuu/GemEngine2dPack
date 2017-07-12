package com.gemengine.component.twod;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gemengine.component.Component;
import com.gemengine.component.base.NotifyComponent;
import com.gemengine.component.base.PointComponent;
import com.gemengine.system.ComponentSystem;
import com.google.inject.Inject;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Camera component which contains a camera. If you want to move the camera,
 * move the Point component. The camera is used to draw different drawable
 * components(or other components that use it indirectly, such as scene).
 * 
 * Ex: If you want to draw a sprite, you must add it as a child to a camera.
 * 
 * @author Dragos
 *
 */
@Log4j2
public class CameraComponent extends NotifyComponent {
	public enum ViewportType {
		Stretch, Fit, Fill, Screen, Extend;
	}

	@Getter
	private boolean resizeable = true;
	@Getter
	private float fov = -1;
	@Getter
	private int width = 0;
	@Getter
	private int height = 0;
	@Getter
	private ViewportType viewportType = ViewportType.Screen;

	@Inject
	public CameraComponent(ComponentSystem componentSystem) {
		super(componentSystem);
	}

	public CameraComponent makeOrthographicCamera() {
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		this.fov = -1;
		doNotify("create");
		return this;
	}

	public CameraComponent makeOrthographicCamera(int width, int height) {
		this.width = width;
		this.height = height;
		this.fov = -1;
		doNotify("create");
		return this;
	}

	public CameraComponent makePerspectiveCamera() {
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		this.fov = 90f;
		doNotify("create");
		return this;
	}

	public CameraComponent makePerspectiveCamera(int fov, int width, int height) {
		this.width = width;
		this.height = height;
		this.fov = fov;
		doNotify("create");
		return this;
	}

	@Override
	public <T extends Component> void onNotify(String arg0, T arg1) {
		if (!(arg1 instanceof PointComponent)) {
			return;
		}
		doNotify("point");
	}

	public CameraComponent setHeight(int height) {
		this.height = height;
		doNotify("size");
		return this;
	}

	public CameraComponent setResizeable(boolean resizeable) {
		this.resizeable = resizeable;
		doNotify("resizeable");
		return this;
	}

	public CameraComponent setSize(int width, int height) {
		this.width = width;
		this.height = height;
		doNotify("size");
		return this;
	}

	public CameraComponent setViewportType(ViewportType type) {
		this.viewportType = type;
		doNotify("viewportType");
		return this;
	}

	public CameraComponent setWidth(int width) {
		this.width = width;
		doNotify("size");
		return this;
	}

	@Override
	public String toString() {
		return "CameraComponent [fov=" + fov + ", width=" + width + ", height=" + height + ", viewportType="
				+ viewportType + "]";
	}
}
