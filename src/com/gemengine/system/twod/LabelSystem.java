package com.gemengine.system.twod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gemengine.component.twod.CameraComponent;
import com.gemengine.component.Component;
import com.gemengine.component.twod.SpriteComponent;
import com.gemengine.component.base.PointComponent;
import com.gemengine.component.twod.LabelComponent;
import com.gemengine.entity.Entity;
import com.gemengine.listener.ComponentListener;
import com.gemengine.listener.ComponentUpdaterListener;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.helper.ListenerHelper;
import com.google.inject.Inject;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LabelSystem extends SystemBase implements ComponentUpdaterListener {
	private final ComponentSystem componentSystem;
	private final AssetSystem assetSystem;
	private final Map<String, BitmapFont> pathToFont;
	private final CameraSystem cameraSystem;
	private CameraComponent currentCamera;
	@SuppressWarnings("unchecked")
	@Getter
	private final Set<String> configuration = ListenerHelper.createConfiguration(LabelComponent.class);

	@Inject
	public LabelSystem(ComponentSystem componentSystem, AssetSystem assetSystem, CameraSystem cameraSystem) {
		super(true, 5);
		this.componentSystem = componentSystem;
		this.assetSystem = assetSystem;
		this.cameraSystem = cameraSystem;
		componentSystem.addComponentUpdater(this);
		pathToFont = new HashMap<String, BitmapFont>();
	}

	@Override
	public void onAfterEntities() {
		SpriteSystem.spriteBatch.end();
	}

	@Override
	public void onBeforeEntities() {
		SpriteSystem.spriteBatch.setTransformMatrix(new Matrix4());
		if (currentCamera != null) {
			SpriteSystem.spriteBatch.setProjectionMatrix(cameraSystem.getCamera(currentCamera).combined);
		}
		SpriteSystem.spriteBatch.begin();
	}

	@Override
	public void onNext(Entity ent) {
		LabelComponent textComponent = ent.getComponent(LabelComponent.class);
		PointComponent pointComponent = ent.getComponent(PointComponent.class);
		if (!textComponent.isEnable()) {
			return;
		}
		CameraComponent newCamera = cameraSystem.getWatchingCamera(ent);
		if (newCamera == null) {
			log.warn("No camera for sprite {}", ent);
			textComponent.setEnable(false);
			return;
		}
		if (currentCamera != newCamera) {
			currentCamera = newCamera;
			SpriteSystem.spriteBatch.setProjectionMatrix(cameraSystem.getCamera(currentCamera).combined);
		}
		String fontPath = textComponent.getFontPath();
		BitmapFont font = pathToFont.get(fontPath);
		if (font == null) {
			font = assetSystem.getAsset(fontPath);
			if (font == null) {
				font = new BitmapFont();
			}
			pathToFont.put(fontPath, font);
		}
		if (pointComponent != null) {
			SpriteSystem.spriteBatch.setTransformMatrix(pointComponent.getMatrix());
			font.getData().setScale(textComponent.getFontScale());
		}
		font.setColor(Color.valueOf(textComponent.getHexColor()));
		font.draw(SpriteSystem.spriteBatch, textComponent.getText(), textComponent.getOffsetX(),
				textComponent.getOffsetY(), textComponent.getWidth(), textComponent.getHAlign(), textComponent.isWrap());
		log.debug(textComponent);
	}
}
