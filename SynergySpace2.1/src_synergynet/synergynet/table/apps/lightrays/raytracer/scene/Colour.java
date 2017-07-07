package synergynet.table.apps.lightrays.raytracer.scene;

public class Colour {
	
	final static double nf = 1.0 / 255.0;

	public double r;
	public double g;
	public double b;
	
	public Colour() {
		set(0, 0, 0);
	}
	
	public Colour(double r, double g, double b) {
		set(r, g, b);
	}
	
	public Colour(int rgb) {
		set(((rgb >> 16) & 0xff) * nf, ((rgb >> 8)  & 0xff) * nf, (rgb & 0xff) * nf);		
	}
	
	public Colour(Colour c) {
		set(c.r, c.b, c.g);
	}

	public void set(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void set(Colour c) {
		set(c.r, c.g, c.b);
	}
		
	public final void attenuate(double a)
	{
		r *= a;
		g *= a;
		b *= a;
	}
	
	public final void attenuate(Colour c)
	{
		r *= c.r;
		g *= c.g;
		b *= c.b;
	}
	
	public final void combineWith(Colour c)
	{
		r += c.r;
		g += c.g;
		b += c.b;
	}
	
	public final int getRGB()
	{
		int _r = (int)Math.ceil(this.r * 255);
		int _g = (int)Math.ceil(this.g * 255);
		int _b = (int)Math.ceil(this.b * 255);
		
		return (255 << 24) |
		       (_r > 255? 255: _r) << 16 |
		       (_g > 255? 255: _g) << 8 |
		       (_b > 255? 255: _b);
	}
	
	public static Colour getAverageColour(Colour[] array) {
		double total_r = 0.0;
		double total_g = 0.0;
		double total_b = 0.0;
		
		for(int i = 0; i < array.length; i++) {
			total_r += array[i].r;
			total_g += array[i].g;
			total_b += array[i].b;
		}
		
		Colour c = new Colour(total_r/array.length, total_g/array.length, total_b/array.length);
		return c;
	}
	
	public String toString() {
		return "(r:" + r + " g:" + g + " b:" + b +")";
	}
}
