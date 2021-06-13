import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

public class Main {
	
public static int factorial(int n) {
		
		if(n == 1) {
			return 1;
		}
		
		return n * factorial(n - 1);
	}
	
	public static void main (String [] args) {		
		long startTime = System.currentTimeMillis();
		Runtime runtime = Runtime.getRuntime();
		
		//String file_name = "./a_example";
		//String file_name = "./b_little_bit_of_everything";
		//String file_name = "./c_many_ingredients";
		//String file_name = "./d_many_pizzas";
		String file_name = "./e_many_teams";
		
		FileWriter fileReader;
		try {
			fileReader = new FileWriter(file_name+".out");
			fileReader.write("");
			fileReader.close();
		} catch (IOException e) {
					
			e.printStackTrace();
		}
		
		
		FileHandler.readFile(file_name  +".in");
		PizzaHandler.HandlerPizza(file_name);
		
		
		
		//FileHandler.readFile("./a_example.in");
		//PizzaHandler.HandlerPizza("a_example.in");
		
		System.out.println("Run in :"+(System.currentTimeMillis() - startTime)+" m/s");
		

		NumberFormat format = NumberFormat.getInstance();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		
		System.out.println("free memory: " + format.format(freeMemory / 1024));
		System.out.println("allocated memory: " + format.format(allocatedMemory / 1024));
		System.out.println("max memory: " + format.format(maxMemory / 1024));
		System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
	}
}
