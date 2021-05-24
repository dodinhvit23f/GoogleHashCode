import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PizzaHandler {

	public static List<Pizza> listPizza = new ArrayList<Pizza>();
	public static List<Team> listTeam = new ArrayList<Team>();
	public static float average;
	// public static float alpha = 2;
	public static int amountOfPizza = -1;
	public static int sroce = 0;
	public static HashMap<String, Integer> mapPizza = new HashMap<String, Integer>();
	public static List<String> output = new ArrayList<String>();
	public static int totalTeam = 0;

	/**
	 * Đặt lại thông số: tổng số pizza, số pizza trung bình
	 */
	public static void setParamter() {
		if (amountOfPizza == -1) {
			amountOfPizza = 0;
			for (Pizza pizza : listPizza) {
				amountOfPizza = amountOfPizza + pizza.getNumber();
			}
		}
		average = (float) (amountOfPizza / listPizza.size());
	}

	public static void HandlerPizza(String file_name) {

		int index = 0;
		// kiểm tra xem list có bị rỗng hay không
		boolean notProcess = true;
		while (!listTeam.isEmpty() || !listPizza.isEmpty()) {
			// bắt đầu lấy Team từ team có số người nhỏ nhất
			Team team = listTeam.get(index);
			// nếu số team trở về 0 thì chuyển sang team khác
			if (team.getNumber() == 0) {
				listTeam.remove(index);
				mapPizza = new HashMap<String, Integer>();
				notProcess = true;
				continue;
			}

			if (!listPizza.isEmpty()) {
				for (int i = 0; i < listPizza.size(); i++) {
					if (listPizza.get(i).getNumber() == 0) {
						listPizza.remove(i);
						notProcess = true;
					}
				}
			}

			// xử lý các team để tạo một hash map lưu lại tổng số nguyên liệu khi kết hợp
			if (notProcess) {
				setMapChoice(team.getNumberOfPerson());
				notProcess = false;
			}

			setParamter();

			choicePizzas();

			amountOfPizza = amountOfPizza - team.getNumberOfPerson();

			team.decrease();

			totalTeam = totalTeam + 1;

			if (team.getNumberOfPerson() > amountOfPizza) {
				break;
			}

		}
		
		saveFile(file_name);
	}

	/**
	 * Dựa vào kết quả của setMapChoice để lấy ra số nguyên liệu nhiều nhất.
	 */
	public static void choicePizzas() {
		// ngưỡng max
		int threshold = 0;
		List<String> maxIngredientKey = new ArrayList<String>();
		Set<String> keys = mapPizza.keySet();

		// tìm ra giá trị max
		for (String k : keys) {
			if ((int) mapPizza.get(k) > threshold) {
				threshold = mapPizza.get(k);
			}
		}
		// lấy list key chứa giá trị max
		for (String k : keys) {
			if ((int) mapPizza.get(k) == threshold) {
				maxIngredientKey.add(k);
			}
		}
		String selectedKey = maxIngredientKey.get(0);

		// chọn key phù hợp

		for (String key : maxIngredientKey) {

			for (String k : keys) {
				if (k.equals(key)) {
					continue;
				}

				for (int i = 0; i < key.length(); i++) {

					if (k.indexOf(new Character(key.charAt(i))) != -1) {

						int value = mapPizza.get(key);

						if (value < threshold) {
							threshold = value;
							selectedKey = key;
						}
					}
				}
			}

		}
		sroce = sroce + mapPizza.get(selectedKey)*mapPizza.get(selectedKey);
		StringBuilder string = new StringBuilder();

		string.append(String.format("%d ", selectedKey.length()));
		for (int index = 0; index < selectedKey.length(); index = index + 1) {
			Pizza pizza = listPizza.get(Integer.parseInt(new Character(selectedKey.charAt(index)).toString()));

			string.append(String.format("%d ", pizza.getPosition().get(0)));
			pizza.decrase();
		}

		string.append("\n");
		output.add(string.toString());

		// xóa pizza

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

	public static void saveFile(String file_name) {
		System.out.println("Your sroce is: "+sroce);
		File file = new File(String.format("./%s.out", file_name));

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileWriter fileWriter = null;
		

		try {

			fileWriter = new FileWriter(file);
			fileWriter.write(String.format("%d \n", totalTeam));
			for (String string : output) {
				fileWriter.write(string);
			}
			
		} catch (IOException e) {

		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
