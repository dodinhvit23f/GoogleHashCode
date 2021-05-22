import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

public class FileHandler {
	
	public static void readFile(String filepath) {

		File file = new File(filepath);
		
		if (file.exists() && file.isFile() && file.canRead()) {
			
			Scanner scan = null;
			try {
				scan = new Scanner(file);

				String line;
				int numberofPizaa = 0;

				if (scan.hasNextLine()) {
					line = scan.nextLine();

					String[] arrayElement = line.split(" ");
					boolean firstLine = true;
					int n_team = 1;
					
					for (String element : arrayElement) {

						if (firstLine) {
							numberofPizaa = Integer.parseInt(element);
							firstLine = false;
							continue;
						}
						
						Team team = new Team();
						team.setNumber(Integer.parseInt(element));
						if (n_team == 1) {
							team.setNumberOfPerson(2);
							PizzaHandler.listTeam.add(team);
						}
						
						if (n_team == 2) {
							team.setNumberOfPerson(3);
							PizzaHandler.listTeam.add(team);
						}
						
						if (n_team == 3) {
							team.setNumberOfPerson(4);
							PizzaHandler.listTeam.add(team);
						}
						
						n_team = n_team + 1;
					}
				}

				if (numberofPizaa == 0) {
					throw new Exception("None pizza number");
				}

				while (scan.hasNextLine()) {
					line = scan.nextLine();
					
					String[] arrayElement = line.split(" ");
					
					int length = arrayElement.length;
					String[] ingredients = new String[length - 1];
					
					Pizza pizza = new Pizza();
					pizza.setNumberOfIngredient(Integer.parseInt(arrayElement[0]));
					
					for (int i = 1 ; i < length; i++) {
						ingredients[i -1 ] = arrayElement[i];
					}
					
					pizza.setIngredient(ingredients);
					
					if(PizzaHandler.listPizza.isEmpty()) {
						PizzaHandler.listPizza.add(pizza);
						pizza.incrase();
						continue;
					}
					
					boolean hadIt = false;
					
					for ( Pizza pizzaStored : PizzaHandler.listPizza) {
						if( pizzaStored.equals(pizza) ) {
							hadIt = true;
							pizzaStored.incrase();
						}
					}
					
					if(!hadIt) {
						PizzaHandler.listPizza.add(pizza);
						pizza.incrase();
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (NumberFormatException fe) {

			} catch (Exception fe) {

			} finally {
				if (scan != null) {
					scan.close();
				}
			}

		}	
	}
	
	
}
