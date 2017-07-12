package com.gemengine.system.twod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.gemengine.component.twod.CameraComponent;
import com.gemengine.component.Component;
import com.gemengine.component.twod.SpriteComponent;
import com.gemengine.component.base.PointComponent;
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
public class SpriteSystem extends SystemBase implements ComponentListener, ComponentUpdaterListener {
	private final ComponentSystem componentSystem;
	private final AssetSystem assetSystem;
	public static final SpriteBatch spriteBatch = new SpriteBatch();
	private final Map<Integer, Sprite> idToSprite;
	private final CameraSystem cameraSystem;
	private CameraComponent currentCamera;
	@SuppressWarnings("unchecked")
	@Getter
	private final Set<String> configuration = ListenerHelper.createConfiguration(SpriteComponent.class);

	@Inject
	public SpriteSystem(ComponentSystem componentSystem, AssetSystem assetSystem, CameraSystem cameraSystem) {
		super(true, 5);
		this.componentSystem = componentSystem;
		this.assetSystem = assetSystem;
		this.cameraSystem = cameraSystem;
		componentSystem.addComponentListener(this);
		componentSystem.addComponentUpdater(this);
		idToSprite = new HashMap<Integer, Sprite>();
	}

	@Override
	public void onAfterEntities() {
		spriteBatch.end();
	}

	@Override
	public void onBeforeEntities() {
		spriteBatch.setTransformMatrix(new Matrix4());
		if (currentCamera != null) {
			spriteBatch.setProjectionMatrix(cameraSystem.getCamera(currentCamera).combined);
		}
		spriteBatch.begin();
	}

	@Override
	public <T extends Component> void onChange(ComponentChangeType arg0, T arg1) {
	}

	@Override
	public void onNext(Entity ent) {
		SpriteComponent spriteComponent = ent.getComponent(SpriteComponent.class);
		if (!spriteComponent.isEnable()) {
			return;
		}
		Sprite sprite = getSprite(spriteComponent);
		if (sprite.getTexture() == null) {
			String texturePath = spriteComponent.getTexturePath();
			log.warn("Sprite System cannot load texture {}", texturePath);
			spriteComponent.setEnable(false);
			return;
		}
		CameraComponent newCamera = cameraSystem.getWatchingCamera(ent);
		if (newCamera == null) {
			log.warn("No camera for sprite {}", ent);
			spriteComponent.setEnable(false);
			return;
		}
		if (currentCamera != newCamera) {
			currentCamera = newCamera;
			spriteBatch.setProjectionMatrix(cameraSystem.getCamera(currentCamera).combined);
		}
		sprite.draw(spriteBatch);
	}

	@Override
	public <T extends Component> void onNotify(String event, T component) {
		SpriteComponent spriteComponent = (SpriteComponent) component;
		switch (event) {
		case "texturePath":
			setTexture(spriteComponent);
			break;
		case "point":
			setPoint(spriteComponent, spriteComponent.getOwner().getComponent(PointComponent.class));
			break;
		default:
			setSize(spriteComponent, spriteComponent.getWidth(), spriteComponent.getHeight());
			break;
		}
		component.setEnable(true);
	}

	@Override
	public <T extends Component> void onTypeChange(Class<T> arg0) {

	}

	public void setPoint(SpriteComponent component, PointComponent point) {
		Sprite spr = getSprite(component);
		spr.setPosition(point.getPosition().x - spr.getWidth() / 2, point.getPosition().y - spr.getHeight() / 2);
		spr.setScale(point.getScale().x, point.getScale().y);
		spr.setRotation(point.getRotation().z);
	}

	public void setSize(SpriteComponent component, float width, float height) {
		String texturePath = component.getTexturePath();
		Sprite spr = getSprite(component);
		component.texture(spr);
		component.setEnable(true);
		spr.setOrigin(width / 2, height / 2);
		spr.setSize(width, height);
		PointComponent point = componentSystem.getOwner(component.getId()).getComponent(PointComponent.class);
		if (point != null) {
			setPoint(component, point);
		}
	}

	public void setTexture(SpriteComponent component) {
		String texturePath = component.getTexturePath();
		Sprite sprite = getSprite(component);
		component.texture(sprite);
		setSize(component, component.getWidth(), component.getHeight());
	}

	private Sprite getSprite(SpriteComponent component) {
		int textureId = component.getId();
		Sprite sprite = idToSprite.get(textureId);
		if (sprite == null) {
			sprite = new Sprite();
			idToSprite.put(textureId, sprite);
		}
		return sprite;
	}
}
