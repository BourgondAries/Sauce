package engine;

public class Formulas {
	private Formulas() {}
	
	public static float slow_top_then_return(float x) {
		return x*(1-x);
	}
	
	public static float slow_stop(float x) {
		return x*(2-x);
	}
	
	public static float very_slow_stop(float x) {
		return (float) (1-Math.pow(1-x,4));
	}
}
