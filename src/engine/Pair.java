package engine;


/**
 * The pair class represents a 2-tuple.
 * The idea is taken from C++'s standard
 * library, in which the pair is: "std::pair".
 * 
 * @author Kevin R. Stravers
 *
 * @param <T> The first parameter type.
 * @param <S> The second parameter type.
 */
public class Pair <T, S>
{
	/**
	 * Pair default constructor.
	 * Initializes both members to null.
	 */
	public Pair ()
	{}
	
	/**
	 * Pair constructor that initializes both operands.
	 * @param a_first The "first" variable.
	 * @param a_second The "second" variable.
	 */
	public Pair ( T a_first, S a_second )
	{
		first = a_first;
		second = a_second;
	}
	
	public T first = null;
	public S second = null;
}
