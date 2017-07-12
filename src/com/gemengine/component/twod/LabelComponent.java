package com.gemengine.component.twod;

import com.gemengine.component.base.DrawableComponent;
import com.gemengine.system.ComponentSystem;
import com.google.inject.Inject;

import lombok.Getter;

/**
 * Text component that can be rendered just with a camera.
 * 
 * @author Dragos
 *
 */
public class LabelComponent extends DrawableComponent {
	@Getter
	private String fontPath  = "";

	@Getter
	private String text = "";

	@Getter
	private float fontScale = 1;

	@Getter
	private int hAlign;

	@Getter
	private boolean wrap;
	
	@Getter
	private float offsetX;

	@Getter
	private float offsetY;

	@Inject
	public LabelComponent(ComponentSystem componentSystem) {
		super(componentSystem);
	}

	public LabelComponent setFontPath(String fontPath) {
		this.fontPath = fontPath;
		doNotify("fontPath");
		return this;
	}

	public LabelComponent setText(String text) {
		this.text = text;
		doNotify("text");
		return this;
	}

	public LabelComponent setFontScale(float fontScale) {
		this.fontScale = fontScale;
		doNotify("fontScale");
		return this;
	}

	public LabelComponent setHAlign(int hAlign) {
		this.hAlign = hAlign;
		doNotify("hAlign");
		return this;
	}

	public LabelComponent setWrap(boolean wrap) {
		this.wrap = wrap;
		doNotify("wrap");
		return this;
	}
	
	public LabelComponent setOffsetX(float offsetX) {
		this.offsetX = offsetX;
		doNotify("offsetX");
		return this;
	}

	public LabelComponent setOffsetY(float offsetY) {
		this.offsetY = offsetY;
		doNotify("offsetY");
		return this;
	}

	@Override
	public String toString() {
		return "TextComponent [fontPath=" + fontPath + ", text=" + text + ", fontScale=" + fontScale + "]";
	}
}
