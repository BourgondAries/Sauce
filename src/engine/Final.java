package engine;


/**
 * Allows for final return types as in:
 * 
 * Final<Integer> const_number;
 * Integer x;
 * 
 * public ClassName () 
 * {
 * 		x = new Integer(33);
 * 		const_number = new Final<Integer>(x);
 * }
 * 
 * public Final<Integer> getNumber
 * {
 * 		return const_number;
 * }
 * 
 * This allows one to return an object that can not be modified.
 * 
 * @author Kevin R. Stravers
 *
 * @param <T> The type type make final.
 * @param reference is the reference to the data to be finalized.
 */
public class Final <T>
{
	/**
	 * This constructor takes a reference to T
	 * and initializes the final parameter with this
	 * reference.
	 * @param reference is the reference to initialize with.
	 */
	public Final ( T reference )
	{
		data = reference;
	}
	
	public final T 
		data;
}
