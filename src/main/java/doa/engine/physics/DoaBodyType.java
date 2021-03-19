package doa.engine.physics;

/**
 * Physics body types.
 * 
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public enum DoaBodyType {

	/**
	 * Static body type. A static body does not react to any force, impulse, or
	 * collision and does not move. A static body can only be moved manually by the
	 * programmer.
	 */
	STATIC,

	/**
	 * Dynamic body type. A dynamic body reacts to forces, impulses, collisions, and
	 * any other world event. Dynamic bodies can also be moved manually, although
	 * suggested way is to let them be moved by world forces.
	 */
	DYNAMIC,

	/**
	 * Kinematic body type. A dynamic does not react to any force, impulse,
	 * collision, but can be moved using velocity/acceleration.
	 */
	KINEMATIC
}
