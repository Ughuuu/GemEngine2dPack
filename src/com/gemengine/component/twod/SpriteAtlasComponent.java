package com.gemengine.component.twod;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.google.inject.Inject;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpriteAtlasComponent extends SpriteComponent {
	@Getter
	private String name = "";
	@Getter
	private int frame = 0;

	@Inject
	public SpriteAtlasComponent(ComponentSystem componentSystem, AssetSystem assetSystem) {
		super(componentSystem, assetSystem);
	}

	public SpriteAtlasComponent setName(String name) {
		this.name = name;
		doNotify("texturePath");
		return this;
	}
	
	public SpriteAtlasComponent setFrame(int frame){
		this.frame = frame;
		doNotify("texturePath");
		return this;		
	}

	@Override
	public void texture(Sprite sprite) {
		TextureAtlas textureAtlas = assetSystem.getAsset(texturePath);
		if (textureAtlas == null) {
			return;
		}
		TextureRegion region = textureAtlas.findRegion(name, frame);
		sprite.setRegion(region);
		this.width = region.getRegionWidth();
		this.height = region.getRegionHeight();
	}

	@Override
	public String toString() {
		return "SpriteAtlasComponent [name=" + name + ", frame=" + frame + "]";
	}
}
