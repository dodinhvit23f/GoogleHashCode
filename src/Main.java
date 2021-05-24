
public class Main {
	public static void main (String [] args) {
		long startTime = System.currentTimeMillis();
		FileHandler.readFile("./a_example.in");
		PizzaHandler.HandlerPizza("a_example");
		System.out.println("Run in :"+(System.currentTimeMillis() - startTime)+" m/s");
	}
}
