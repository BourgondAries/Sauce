package engine;


import javax.vecmath.Vector3f;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;


/**
 * An object that can experience newtonian forces (impulses).
 * These forces are used to calculate speed.
 * 
 * @author Thormod Myrvang og Kevin R. Stravers
 *
 */
public class DynamicObject extends RectangleShape
{
	private static final float CM_FULL_CIRCLE_DEGREES = 360.f;
	
	// Setup movement
	protected Float 		m_mass = 1.f; 			/// Mass of the Dynamic Object, used in physics calculations.
	protected Vector3f 		m_speed, m_impulse; 	/// Speed and impulse (delta-speed / 1 frame), z is the rotation, counter clockwise.
	protected Environment 	m_world_state;			/// State that the object is in, affects physics.

	/**
	 * Standard constructor; initializes the data members and
	 * makes sure the constant references are also initialized.
	 */
	public DynamicObject () 
	{
		m_speed = new Vector3f(0.f ,0.f ,0.f);
		m_impulse = new Vector3f(0.f ,0.f ,0.f);
		m_world_state = Environment.inAir;
	}
	
	
	public static enum Environment
	{
		inAir, onGround, inMagma;
	}
	
	public void inAir()
	{
		m_world_state = Environment.inAir;
	}
	
	public void inMagma() 
	{
		m_world_state = Environment.inMagma;
	}
	
	public void onGround() 
	{
		m_world_state = Environment.onGround;
	}
	
	public boolean isInAir() 
	{
		return m_world_state == Environment.inAir;
	}
	
	public boolean isInMagma() 
	{
		return m_world_state == Environment.inMagma;
	}
	
	public boolean isOnGround ( ) 
	{
		return m_world_state == Environment.onGround;
	}
	
	public void setOriginToMiddle()
	{
		super.setOrigin(super.getSize().x / 2, super.getSize().y / 2);
	}
	
	/**
	 * This method returns a POD; thus a copy; of the mass of the object.
	 * @return the mass of this object.
	 */
	public float getMass ( ) 
	{
		return new Float( m_mass );
	}
	
	/**
	 * This method returns a POD; thus a copy; of the mass of the object.
	 * @return the mass of this object.
	 */
	public float fetchMass ( ) 
	{
		return m_mass;
	}
	
	/**
	 * We can set the mass to any positive value.
	 * The mass of an object is important in calculating its acceleration.
	 * We have F = m * a, however, We are working with impulses which take the
	 * form N / s. The increment in speed is given by: impulse / mass.
	 * @param mass The mass to assign to the DynamicObject.
	 * @throws IllegalArgumentException when the mass is negative or 0.
	 */
	public void setMass ( final float mass ) throws IllegalArgumentException
	{
		if (mass <= 0.f) 
			throw new IllegalArgumentException("DynamicObject.setMass(" + mass + "): Mass cannot be less than or equal to zero");
		m_mass = mass;
	}
	
	/**
	 * The speed of the object is computed after update();
	 * when all impulses have been nullified.
	 * The speed is given in pixels per iteration.
	 * @return the speed of the object.
	 */
	public Vector3f getSpeed ( )
	{
		return new Vector3f( m_speed );
	}
	
	/**
	 * The speed of the object is computed after update();
	 * when all impulses have been nullified.
	 * The speed is given in pixels per iteration.
	 * @return the speed of the object.
	 */
	public Vector3f fetchSpeed ( )
	{
		return m_speed;
	}
	
	/**
	 * Speed can be set to any other speed.
	 * This method can be used in conjunction with getSpeed and a
	 * Vector3f variable. Example:
	 * 
	 * DynamicObject b = new DynamicObject();
	 * Vector3f i = new Vector3f(1.f, 5.f, -4.f);
	 * i.add(b.getSpeed().data);
	 * b.setSpeed(i);
	 * 
	 * This algorithm adds a speed to the existing speed.
	 * 
	 * @param speed the speed to replace by.
	 */
	public void setSpeed ( final Vector3f speed )
	{
		m_speed.x = speed.x;
		m_speed.y = speed.y;
		m_speed.z = speed.z;
	}
	
	/**
	 * The impulse is the force that is applied in the frame.
	 * It will be converted to speed after the current speed
	 * has been applied to the position of the object.
	 * @return The a copy of the current impulse applied to the object.
	 */
	public Vector3f getImpulse ( ) 
	{
		return new Vector3f( m_impulse );
	}
	
	/**
	 * The impulse is the force that is applied in the frame.
	 * It will be converted to speed after the current speed
	 * has been applied to the position of the object.
	 * @return The current impulse applied to the object.
	 */
	public Vector3f fetchImpulse ( ) 
	{
		return m_impulse;
	}
	
	/**
	 * The impulse is set to a new impulse by using this method.
	 * @param impulse the impulse replace by.
	 */
	public void setImpulse ( final Vector3f impulse )
	{
		m_impulse.x = impulse.x;
		m_impulse.y = impulse.y;
		m_impulse.z = impulse.z;
	}
	
	/**
	 * Update moves the object according to its speed vectors.
	 * It then rotates the object by the rotation vector.
	 * 
	 * After this, impulses are added to the speed, and then
	 * the impulses are nullified.
	 */
	public void update ( ) 
	{
		// Update position
		super.move(m_speed.x, m_speed.y);
		super.rotate(m_speed.z);
		clampRotation();
		
		// Update speed
		m_speed.x += m_impulse.x / getMass();
		m_speed.y += m_impulse.y / getMass();
		m_speed.z += m_impulse.z / getMass();
		
		// Reset impulses
		m_impulse.set(0.f, 0.f, 0.f);
	}
	
	/**
	 * Method that checks whether one dynamic object is within a certain box-collision
	 * vincinity of another one. The pixel_limits are taken from the 4
	 * different vertices of the rectangleshape, and go in both x-and-y coords.
	 * @param compared_to
	 * @param pixel_limits
	 * @return whether the compared_to object is within another's space.
	 */
	public boolean isBoxNear ( DynamicObject compared_to, int pixel_limits )
	{
		FloatRect box = getGlobalBounds();
		int px = pixel_limits;
		box = new FloatRect ( box.left - px, box.top - px, box.width + 2*px, box.height + 2*px );
		box = box.intersection(compared_to.getGlobalBounds());
		
		return (box != null);
	}
	
	/**
	 * This function makes a dynamicobject accelerate towards another with
	 * a set "force". This force is multiplied by the direction and then divided
	 * by the entire distance given by the pythagorean theorem.
	 * @param other
	 * @param force
	 */
	public void accelerateTowards ( DynamicObject other, float force )
	{
		Vector2f origin = new Vector2f( getPosition().x + getSize().x / 2.f, getPosition().y + getSize().y / 2.f );
		Vector2f other_origin = new Vector2f( other.getPosition().x + other.getSize().x / 2.f, other.getPosition().y + other.getSize().y / 2.f );
		Vector2f direction = Vector2f.sub(origin, other_origin);
		float distance = Utilities.getDistance(origin,  other_origin);
		direction = new Vector2f(force * direction.x / distance, force * direction.y / distance);
		fetchImpulse().x -= direction.x;
		fetchImpulse().y -= direction.y;
	}
	
	private void clampRotation ()
	{
		super.setRotation(super.getRotation() % CM_FULL_CIRCLE_DEGREES);
		if ( super.getRotation() < 0.f )
			super.setRotation( super.getRotation() + CM_FULL_CIRCLE_DEGREES );
	}
	
}
