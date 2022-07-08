package doa.engine.scene;

import static doa.engine.core.DoaGraphicsFunctions.popAll;
import static doa.engine.core.DoaGraphicsFunctions.pushAll;
import static doa.engine.core.DoaGraphicsFunctions.rotate;
import static doa.engine.core.DoaGraphicsFunctions.translate;
import static doa.engine.log.DoaLogger.LOGGER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import doa.engine.maths.DoaMath;
import doa.engine.scene.elements.DoaEssentialComponent;
import doa.engine.scene.elements.DoaTransform;
import doa.engine.scene.elements.physics.DoaCollider;
import doa.engine.scene.elements.physics.DoaRigidBody;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;

/**
 * Blueprint of all objects that are going to be processed by DoaEngine, all
 * objects that will be processed by DoaEngine <strong>must</strong> either be
 * this class, or its sub-classes.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public class DoaObject implements Serializable {

	private static final long serialVersionUID = -3293257583358544377L;

	public final DoaTransform transform = new DoaTransform();
	public DoaRigidBody rigidBody = null;

	DoaScene scene = null;
	private int zOrder = 0;

	protected final ArrayList<DoaComponent> components = new ArrayList<>();

	private final List<DoaScript> scripts = new ArrayList<>();
	private final List<DoaRenderer> renderers = new ArrayList<>();

	@NotNull
	public String name;

	private boolean dynamic = true;

	/**
	 * Constructor. Constructs an empty DoaObject.
	 */
	public DoaObject() { this("DoaObject"); }

	/**
	 * Constructor. Constructs an empty DoaObject.
	 * 
	 * @param name name of the DoaObject
	 */
	public DoaObject(String name) {
		this.name = name;
		components.add(transform);
	}

	/**
	 * Makes the DoaObject static. Static DoaObjects are in screen-space. They
	 * aren't moved by DoaCamera and aren't shaded by DoaEngine. Particularly useful
	 * when implementing UI.
	 * 
	 * @return this
	 */
	public DoaObject makeStatic() {
		dynamic = false;
		return this;
	}

	/**
	 * Makes the DoaObject dynamic. Dynamic DoaObjects are in world-space.
	 * 
	 * @return this
	 */
	public DoaObject makeDynamic() {
		dynamic = true;
		return this;
	}

	public boolean isDynamic() { return dynamic; }

	/**
	 * Returns the scene which this DoaObject is in. Returns null if this DoaObject
	 * is not inside a scene.
	 * 
	 * @return the scene which this DoaObject is in, or null if this DoaObject is
	 *         not inside one
	 */
	public final DoaScene getScene() { return scene; }

	/**
	 * Returns the zOrder of this DoaObject. A higher zOrder means the DoaObject is
	 * drawn over DoaObjects with lower zOrder values.
	 * 
	 * @return the zOrder of this DoaObject
	 */
	public final int getzOrder() { return zOrder; }

	/**
	 * Finds and returns the first occurrence of a DoaComponent. This method will
	 * log a warning message if a DoaComponent of type T cannot be found.
	 * 
	 * @param <T> type of the DoaComponent to find
	 * @param type class of the DoaComponent to find
	 * @return an optional containing either the found DoaComponent or null
	 */
	public final <T extends DoaComponent> Optional<T> getComponentByType(@NotNull Class<T> type) {
		var rv = components.stream().filter(type::isInstance).findFirst();
		if (rv.isEmpty()) {
			LOGGER.warning(new StringBuilder(128).append("No component of type ").append(type.getName()).append(" is found in object ").append(name).append(
			        ". Returned an Optional containing null!"));
			return Optional.of(null);
		}
		return Optional.of((T)rv.get());
	}

	/**
	 * Returns an unmodifiable view of the DoaComponents inside this DoaObject.
	 * 
	 * @return an immutable view of DoaComponents
	 */
	public final List<DoaComponent> getComponentsView() { return Collections.unmodifiableList(components); }

	@OverridingMethodsMustInvokeSuper
	public void setzOrder(final int zOrder) {
		if (scene != null) {
			scene.updatezOrder(this, zOrder);
		}
		this.zOrder = zOrder;
	}

	@OverridingMethodsMustInvokeSuper
	public void onAddToScene(final DoaScene scene) {
		if (rigidBody != null) {
			scene.registerBody(rigidBody);
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void onRemoveFromScene(final DoaScene scene) {
		if (rigidBody != null) {
			scene.deleteBody(rigidBody);
		}
	}

	/**
	 * Add the specified DoaComponent to this DoaObject. If the component is already
	 * added to another DoaObject, it is first removed from that DoaObject prior.
	 * 
	 * @param component the DoaComponent to add
	 */
	public final void addComponent(@NotNull final DoaComponent component) {
		if (components.contains(component)) { return; }
		var oldOwner = component.getOwner();
		if (oldOwner != null) {
			LOGGER.warning(new StringBuilder(128).append("Component").append(component.name).append(
			        " already has an owner. Components cannot be shared. Add failed."));
			return;
		}
		if (component instanceof DoaScript script) {
			scripts.add(script);
		} else if (component instanceof DoaRenderer renderer) {
			renderers.add(renderer);
		} else if (component instanceof DoaRigidBody rigidBody) {
			if (this.rigidBody != null) {
				LOGGER.warning(new StringBuilder(64).append("DoaObject").append(name).append(" already has a DoaRigidBody. Add failed."));
			}
			this.rigidBody = rigidBody;
		}
		components.add(component);
		component.owner = this;
		component.onAdd(this);
		LOGGER.fine(new StringBuilder(64).append("Component ").append(component.name).append(" is succesfully added to object ").append(name).append("."));
	}

	/**
	 * Removes the specified DoaComponent from this DoaObject. This method will log
	 * a warning message if component is not present in this DoaObject.
	 * 
	 * @param component the DoaComponent to remove
	 */
	public final void removeComponent(@NotNull final DoaComponent component) {
		if (component instanceof DoaEssentialComponent) { return; }
		if (components.remove(component)) {
			if (component instanceof DoaScript script) {
				scripts.remove(script);
			} else if (component instanceof DoaRenderer renderer) {
				renderers.remove(renderer);
			} else if (component instanceof DoaRigidBody rigidBody) {
				this.rigidBody = null;
			}
			component.owner = null;
			component.onRemove(this);
			LOGGER.fine(
			        new StringBuilder(128).append("Component ").append(component.name).append(" is succesfully removed from object ").append(name).append("."));
		} else {
			LOGGER.warning(new StringBuilder(128).append("Component ").append(component.name).append(" is not present in object ").append(name).append(
			        ". Couldn't remove anything."));
		}
	}

	final void tick() {
		if (rigidBody != null) {
			rigidBody.tick();
		}
		scripts.forEach(scr -> {
			if (scr.isEnabled()) {
				scr.tick();
			}
		});
	}

	final void render() {
		pushAll();
		translate(transform.position.x, transform.position.y);
		rotate(DoaMath.toRadians(transform.rotation));
		renderers.forEach(ren -> {
			if (ren.isEnabled()) {
				ren.render();
			}
		});

		if (rigidBody != null && rigidBody.enableDebugRender) {
			rigidBody.colliders.forEach(DoaCollider::debugRender);
		}
		scripts.stream().filter(scr -> scr.isEnabled() && scr.enableDebugRender).forEach(DoaScript::debugRender);
		renderers.stream().filter(ren -> ren.isEnabled() && ren.enableDebugRender).forEach(DoaRenderer::debugRender);
		// want to consider parallelStream's here? check that
		popAll();
	}
}
