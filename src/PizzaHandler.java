import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PizzaHandler {

	public static List<Pizza> listPizza = new ArrayList<Pizza>();
	public static List<Team> listTeam = new ArrayList<Team>();
	public static float average;
	//public static float alpha = 2;
	public static int amountOfPizza = -1;
	public static int sroce = 0;
	public static HashMap<String, Integer> mapPizza = new HashMap<String, Integer>();

	public static void setMediumNumber() {
		if (amountOfPizza == -1) {
			amountOfPizza = 0;
			for (Pizza pizza : listPizza) {
				amountOfPizza = amountOfPizza + pizza.getNumber();
			}
		}
		average = (float) (amountOfPizza / listPizza.size());
	}

	public static void HandlerPizza() {

		int index = 0;
		// kiểm tra xem list có bị rỗng hay không
		boolean notProcess = true;
		while (!listTeam.isEmpty() || !listPizza.isEmpty()) {

			Team team = listTeam.get(index);

			if (team.getNumber() == 0) {
				listTeam.remove(index);
				mapPizza = new HashMap<String, Integer>();
				notProcess = true;
				continue;
			}
	
			if (notProcess) {
				setMapChoice(team.getNumberOfPerson());
				notProcess = false;
			}
			
			setMediumNumber();

			if (team.getNumberOfPerson() > amountOfPizza) {
				break;
			}

			amountOfPizza = amountOfPizza - team.getNumberOfPerson();
			team.decrease();

		}

	}
	
	public static void choicePizzas() {
		int max = 0;
		List<String> maxIngredientKey = new ArrayList<String>();
		Set<String> keys = mapPizza.keySet();
		
		// tìm ra giá trị max
		for (String k : keys) {
			if(  (int) mapPizza.get(k) > max) {
				max = mapPizza.get(k);
			}
		}
		// lấy key chứa giá trị max
		for (String k : keys) {
			if(  (int) mapPizza.get(k) == max) {
				maxIngredientKey.add(k);
			}
		}
	}

	public static void setMapChoice(int numberOfPerson) {
		ArrayList<String> ingredients = new ArrayList<String>();

		loopProcess(listPizza.size() - 1, 0, numberOfPerson - 1, "", ingredients);

	}

	public static void loopProcess(int lim, int start, int numberOfPerson, String code, List<String> ingredients) {
		code = code + String.valueOf(start);

		if (numberOfPerson == 0) {

			if (mapPizza.get(code) == null) {
				mapPizza.put(code, totalIngredient(ingredients));
			}

			return;
		}

		if (start == 0) {
			String[] array_ingredient = listPizza.get(start).getIngredient();
			for (String ingredinet : array_ingredient) {
				ingredients.add(ingredinet);
			}

		}

		for (int i = start + 1; i <= lim; i++) {
			List<String> new_ingredient = new ArrayList<String>(ingredients);
			String[] array_ingredient = listPizza.get(i).getIngredient();

			for (String ingredinet : array_ingredient) {
				new_ingredient.add(ingredinet);
			}

			loopProcess(lim, i, numberOfPerson - 1, code, new_ingredient);
		}

	}

	public static int totalIngredient(List<String> ingredients) {
		int totalIngredient = 0;
		int length = ingredients.size();
		ingredients.toArray();

		for (int index = 0; index < length; index++) {

			if (ingredients.get(index) == null) {
				continue;
			}

			for (int checkIndex = index + 1; checkIndex < length; checkIndex++) {

				if (ingredients.get(checkIndex) == null) {
					continue;
				}

				if (ingredients.get(checkIndex).equals(ingredients.get(index))) {
					ingredients.set(checkIndex, null);
				}

			}

			totalIngredient = totalIngredient + 1;

		}

		return totalIngredient;
	}
}
