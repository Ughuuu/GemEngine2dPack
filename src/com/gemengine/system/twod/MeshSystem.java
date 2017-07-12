package com.gemengine.system.twod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.gemengine.component.twod.CameraComponent;
import com.gemengine.component.Component;
import com.gemengine.component.twod.MeshComponent;
import com.gemengine.component.twod.SpriteComponent;
import com.gemengine.component.base.PointComponent;
import com.gemengine.entity.Entity;
import com.gemengine.listener.ComponentListener;
import com.gemengine.listener.ComponentUpdaterListener;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.base.ConstructorSystem;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.helper.ListenerHelper;
import com.google.inject.Inject;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MeshSystem extends ConstructorSystem<Mesh, MeshComponent> {
	private final AssetSystem assetSystem;

	@Inject
	public MeshSystem(AssetSystem assetSystem, ComponentSystem componentSystem, EntitySystem entitySystem) {
		super(componentSystem, entitySystem, true, 16, MeshComponent.class);
		this.assetSystem = assetSystem;
	}

	@Override
	protected Mesh create(MeshComponent component) {
		//Mesh mesh = new Mesh();
		return null;
	}

	@Override
	protected void onEvent(String event, MeshComponent notifier, Mesh label) {
		switch (event) {
		}
	}
}