package engine;


/**
 * The states may affect the physical calculations.
 * For example; when DynamicObject is in water, the friction
 * coefficient may be high, thus resulting in a higher speed decrease.
 * 
 * Idea: Take states outside.
 * @author Thormod Myrvang
 *
 */
public enum Environment 
{
	inAir, onGround, inMagma;
	
	
}
