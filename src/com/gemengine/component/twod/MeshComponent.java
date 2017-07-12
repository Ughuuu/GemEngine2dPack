package com.gemengine.component.twod;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.twod.MeshSystem;
import com.google.inject.Inject;

import lombok.Getter;

public class MeshComponent extends DrawableComponent {
	BoundingBox box = new BoundingBox();
	private final MeshSystem meshSystem;

	@Getter
	private short[] indices;

	@Getter
	private float[] vertices;

	@Getter
	private MeshType meshType = MeshType.Triangles;

	@Inject
	public MeshComponent(ComponentSystem componentSystem, MeshSystem meshSystem) {
		super(componentSystem);
		this.meshSystem = meshSystem;
	}

	public MeshComponent setIndices(short[] indices) {
		this.indices = indices;
		doNotify("indices");
		return this;
	}

	public MeshComponent setVertices(float[] vertices) {
		this.vertices = vertices;
		doNotify("vertices");
		return this;
	}

	public MeshComponent setMeshType(MeshType meshType) {
		this.meshType = meshType;
		doNotify("meshType");
		return this;
	}

	@Override
	public float getWidth() {
		Mesh mesh = meshSystem.get(this);
		if (mesh == null) {
			return 0;
		}
		mesh.calculateBoundingBox(box);
		return box.getWidth();
	}

	@Override
	public float getHeight() {
		Mesh mesh = meshSystem.get(this);
		if (mesh == null) {
			return 0;
		}
		mesh.calculateBoundingBox(box);
		return box.getHeight();
	}

	public static enum MeshType {
		Points(GL20.GL_POINTS), Lines(GL20.GL_LINES), LineLoop(GL20.GL_LINE_LOOP), LineStrip(
				GL20.GL_LINE_STRIP), Triangles(
						GL20.GL_TRIANGLES), TriangleStrip(GL20.GL_TRIANGLE_STRIP), TriangleFan(GL20.GL_TRIANGLE_FAN);

		@Getter
		private int type;

		MeshType(int type) {
			this.type = type;
		}
	}
}
