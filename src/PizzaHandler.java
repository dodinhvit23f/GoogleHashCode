import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PizzaHandler {

	public static List<Pizza> listPizza = new ArrayList<Pizza>();
	public static List<Team> listTeam = new ArrayList<Team>();

	// public static float alpha = 2;

	public static int amountOfPizza = -1;
	public static int sroce = 0;
	public static int totalTeam = 0;
	public static int maxSize = 20;
	public static int numberOfImprove = 0;

	public static float weight = 0.4f;

	public static float average = 0;

	public static Random rd = new Random();
	// memory out
	public static HashMap<String, Integer> mapPizzaMax = new HashMap<String, Integer>();

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

		for (int i = 0; i < listPizza.size(); i++) {
			average += listPizza.get(i).getNumberOfIngredient() * listPizza.get(i).getNumber();
		}
		average = average / listPizza.size();

	}

	public static long factorial(int start, int n) {

		if (n == 0) {
			return 1;
		}

		long results = 1;

		for (int i = start; i <= n; i++) {
			results = results * i;
		}

		return results;
	}

	/**
	 * Quyết định xem bắt đầu từ 4 hay 3.
	 */
	public static int getTeamIndexToStart(int numberOfPerson) {
		setParamter();
		int numberOfTeam = 0;

		int total = amountOfPizza;

		while ((total / numberOfPerson) != 0) {
			numberOfTeam = numberOfTeam + (total / numberOfPerson) * numberOfPerson;
			total = total % numberOfPerson;

			numberOfPerson = numberOfPerson - 1;
			if (numberOfPerson == 1) {
				break;
			}
		}

		return numberOfTeam;
	}

	public static void HandlerPizza(String file_name) {

		int index = 1;

		if (getTeamIndexToStart(4) >= getTeamIndexToStart(3)) {
			index = 2;
		}

		while (!listTeam.isEmpty() && !listPizza.isEmpty()) {
			// bắt đầu lấy Team từ team có số người nhỏ nhất
			Team team = listTeam.get(index);
			// nếu số team trở về 0 thì chuyển sang team khác
			if (team.getNumber() == 0) {

				listTeam.remove(index);

				if (index != 0) {
					index = index - 1;
				}

				continue;
			}

			if (team.getNumberOfPerson() > listPizza.size()) {

				if (index != 0) {
					index = index - 1;
					continue;
				}

				if (index == 0) {
					break;
				}
			}

			// xử lý các team để tạo một hash map lưu lại tổng số nguyên liệu khi kết hợp

			setMapChoice(team.getNumberOfPerson());

			numberOfImprove = numberOfImprove + 1;

			if (numberOfImprove < 10) {
				continue;
			}

			removeLargeDuplicate();

			choicePizzas(file_name, team);

			amountOfPizza = amountOfPizza - team.getNumberOfPerson();

			if (!listPizza.isEmpty()) {
				for (int i = 0; i < listPizza.size();) {

					if (listPizza.get(i).getNumber() == 0 && listPizza.get(i).getPosition().size() == 0) {
						listPizza.remove(i);

						continue;
					}

					i = i + 1;
				}
			}

			mapPizzaMax.clear();
			// System.out.println("Your current sroce is: " + sroce);
		}
		listPizza.clear();
		listTeam.clear();
		mapPizzaMax.clear();

		saveFile(file_name, String.format("%d \n", totalTeam), true);
		System.out.println(file_name);
		System.out.println("Your sroce is: " + sroce);
	}

	/**
	 * Dựa vào kết quả của setMapChoice để lấy ra số nguyên liệu nhiều nhất.
	 */
	public static void choicePizzas(String file_name, Team team) {

		// ngưỡng max
		int threshold = Integer.MAX_VALUE;

		ArrayList<String> keys = new ArrayList<String>(mapPizzaMax.keySet());
		// lây toan bộ cac key
		// tìm ra số nguyên liệu trùng nhỏ nhất và thêm vào danh sách

		String selectedKey = keys.get(0);

		StringBuilder string = new StringBuilder();

		String[] classNo = selectedKey.split(" ");

		string.append(String.format("%d ", classNo.length));

		List<String> listIngredient = new ArrayList<String>();
		for (int i = 0; i < classNo.length; i++) {
			for (String ingredient : listPizza.get(Integer.parseInt(classNo[i])).getIngredient()) {
				listIngredient.add(ingredient);
			}
		}
		// System.out.println("");
		threshold = totalDulicateIngredient(listIngredient);

		for (int i = 0; i < classNo.length; i++) {
			Pizza pizza = listPizza.get(Integer.parseInt(classNo[i]));
			// System.out.print(String.format("%s ",
			// listPizza.get(Integer.parseInt(classNo[i]))));
			string.append(String.format("%d ", pizza.getPosition().get(0)));
			pizza.decrase();

		}

		sroce = sroce + mapPizzaMax.get(selectedKey) * mapPizzaMax.get(selectedKey);

		totalTeam = totalTeam + 1;
		team.decrease();

		string.append("\n");
		saveFile(file_name, string.toString(), false);

		System.out.println(String.format(" %d - %d ", mapPizzaMax.get(selectedKey), threshold));

	}

	public static void setMapChoice(int numberOfPerson) {

		/*
		 * for (int i = rd.nextInt(listPizza.size() - numberOfPerson) ; i <
		 * listPizza.size() - 1; i++) { loopProcess(listPizza.size() - 1, i,
		 * numberOfPerson - 1, "", ingredients);
		 * 
		 * if (mapPizzaMax.size() > maxSize) { return; }
		 * 
		 * }
		 */
		int position = 0;
		int oldPosition = 0;

		List<String> listRandom = new ArrayList<String>();

		for (int i = 0; i < maxSize; i++) {
			position = rd.nextInt(listPizza.size());
			ArrayList<String> ingredients = new ArrayList<String>();

			if (position > 0 && position < listPizza.size() - 2) {
				loopProcess(listPizza.size() - 1, position - 1, numberOfPerson - 1, "", ingredients, listRandom);
				loopProcess(listPizza.size() - 1, position + 1, numberOfPerson + 1, "", ingredients, listRandom);

			}

			loopProcess(listPizza.size() - 1, position, numberOfPerson - 1, "", ingredients, listRandom);

			oldPosition = position;

			position = rd.nextInt(listPizza.size());
			while (position != oldPosition) {
				position = rd.nextInt(listPizza.size());
			}

			ingredients.clear();
		}

	}

	public static void loopProcess(int lim, int start, int numberOfPerson, String code, List<String> ingredients,
			List<String> listRandom) {

		if (code.equals("")) {
			code = code + String.valueOf(start);
		} else {
			code = code + " " + String.valueOf(start);
		}

		if (numberOfPerson == 0) {

			int total = totalIngredient(ingredients);

			if (mapPizzaMax.isEmpty()) {

				mapPizzaMax.put(code, total);

				return;
			}

			if (total <= new ArrayList<Integer>(mapPizzaMax.values()).get(0)) {

				mapPizzaMax.put(code, total);
			}

			String[] classNoNew = code.split(" ");

			for (String string : classNoNew) {
				listRandom.add(string);
			}

			for (int index = 0; index <= maxSize; index++) {

				Set<String> keySet = new HashSet<String>(mapPizzaMax.keySet());

				for (String key : keySet) {

					String[] classNoOld = key.split(" ");
					int value = mapPizzaMax.get(key);

					int count = 0;

					for (int i = 0; i < classNoOld.length; i++) {
						for (int j = 0; j < classNoNew.length; j++) {
							if (classNoOld[i].equals(classNoNew[j])) {
								count++;
								break;
							}
						}
					}

					if (count == classNoOld.length) {
						return;
					}

					for (String string : classNoOld) {

						boolean exist = false;

						for (String pizza : listRandom) {
							if (pizza.equals(string)) {
								exist = true;
								break;
							}
						}

						if (!exist) {
							listRandom.add(string);
						}
					}

					if (randomBetweenTwoPizza(classNoNew, listRandom, (value > total) ? value : total,
							(value > total) ? key : code)) {
						break;
					}
				}
			}
			return;
		}

		if (start != -1) {
			String[] array_ingredient = listPizza.get(start).getIngredient();
			for (String ingredinet : array_ingredient) {
				ingredients.add(ingredinet);
			}
		}

		String[] classNo = code.split(" ");

		int i = rd.nextInt(listPizza.size());
		
		int loopTime = 1;
		while (true) {
			boolean exist = false;

			for (String s : classNo) {
				if (String.valueOf(i).equals(s)) {
					exist = true;
					break;
				}
			}
			
			if(loopTime == 50) {
				break;
			}
			
			loopTime = loopTime + 1;

			if (!exist) {
				Pizza pizza = listPizza.get(i);
				if (pizza.getNumberOfIngredient() > average) {
					average = (long) (average + pizza.getNumberOfIngredient() * weight);
					break;
				}

				if (pizza.getNumberOfIngredient() <= average) {
					average = (long) (average - pizza.getNumberOfIngredient() * (weight / 2));
				}

			}

			i = rd.nextInt(listPizza.size());
		}

		for (String ingredinet : listPizza.get(i).getIngredient()) {
			ingredients.add(ingredinet);
		}

		loopProcess(lim, i, numberOfPerson - 1, code, ingredients, listRandom);
	}

	public static boolean randomBetweenTwoPizza(String[] classNoNew, List<String> listRandom, int value, String key) {
		// số pizza cần đưa cho team
		int number = classNoNew.length;
		// mã code
		String newCode = null;
		// danh sách các nguyên liệu

		int lim = listRandom.size();
		String[] classNoOld = key.split(" ");
		List<String> ingredients = null;

		for (int index = 0; index < maxSize; index++) {

			ingredients = new ArrayList<String>();
			newCode = randomNewKey(number, lim, listRandom, ingredients);

			int count = 0;

			for (int i = 0; i < classNoOld.length; i++) {
				for (int j = 0; j < classNoNew.length; j++) {
					if (classNoOld[i].equals(classNoNew[j])) {
						count++;
						break;
					}
				}
			}

			if (count != classNoOld.length) {
				break;
			}

		}

		int duplicate = totalDulicateIngredient(ingredients);
		int total = totalIngredient(ingredients);

		// nếu tổng lớn hơn thì ....
		if (value < total) {

			mapPizzaMax.clear();

			mapPizzaMax.put(newCode, total);
			return true;
		}

		if (value == total) {
			String[] oldNo = key.split(" ");
			List<String> oldIngredients = new ArrayList<String>();

			for (String no : oldNo) {
				for (String ingredinet : listPizza.get(Integer.parseInt(no)).getIngredient()) {
					oldIngredients.add(ingredinet);
				}
			}

			int oldDuplicate = totalDulicateIngredient(oldIngredients);

			if (oldDuplicate > duplicate) {

				mapPizzaMax.put(newCode, total);

			}

			return true;
		}

		return false;
	}

	public static String randomNewKey(int number, int lim, List<String> listRandom, List<String> ingredients) {
		String newCode = "";

		while (number != 0) {
			// chọn pizaa ngẫu nhiên

			int index = rd.nextInt(lim);

			// kiểm tra trùng lặp
			String[] classNo = newCode.split(" ");
			boolean exist = false;
			// kiểm tra với mỗi phần tử của với phần tử vừa random
			for (String s : classNo) {
				if (listRandom.get(index).equals(s)) {
					exist = true;
					break;
				}
			}

			if (exist) {
				continue;
			}

			// thêm nguyên liệu vào danh sách
			String[] array_ingredient = listPizza.get(Integer.parseInt(listRandom.get(index))).getIngredient();

			for (String ingredinet : array_ingredient) {
				ingredients.add(ingredinet);
			}

			if (newCode.equals("")) {
				newCode = newCode + listRandom.get(index);
			} else {
				newCode = newCode + " " + listRandom.get(index);
			}

			number = number - 1;
		}

		return newCode;
	}

	public static int totalIngredient(List<String> ingredients) {

		int totalIngredient = 0;

		for (int index = 0; index < ingredients.size(); index++) {

			if (ingredients.get(index) == null) {
				continue;
			}

			for (int checkIndex = index + 1; checkIndex < ingredients.size(); checkIndex++) {

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

	public static int totalDulicateIngredient(List<String> ingredients) {

		int totalDuplicate = 0;

		for (int index = 0; index < ingredients.size(); index++) {

			if (ingredients.get(index) == null) {
				continue;
			}

			for (int checkIndex = index + 1; checkIndex < ingredients.size(); checkIndex++) {

				if (ingredients.get(checkIndex) == null) {
					continue;
				}

				if (ingredients.get(checkIndex).equals(ingredients.get(index))) {
					ingredients.set(checkIndex, null);
					totalDuplicate = totalDuplicate + 1;
				}

			}

		}

		return totalDuplicate;
	}

	public static void saveFile(String file_name, String output, boolean end) {

		File file = new File(String.format("./%s.out", file_name));

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (end) {

			FileReader fileReader = null;
			StringBuilder stringBuilder = new StringBuilder();
			try {
				fileReader = new FileReader(file);

				int i;
				while ((i = fileReader.read()) != -1) {
					stringBuilder.append((char) i);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fileReader != null) {
					try {
						fileReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			FileWriter fileWriter = null;

			try {

				fileWriter = new FileWriter(file);

				fileWriter.write(output);
				fileWriter.write(stringBuilder.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return;
		}

		FileWriter fileWriter = null;

		try {

			fileWriter = new FileWriter(file, true);

			fileWriter.write(output);

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

	public static void removeLargeDuplicate() {
		List<String> keys = new ArrayList<String>(mapPizzaMax.keySet());

		int minThreshold = Integer.MAX_VALUE;
		String lastkey = "";

		for (String key : keys) {

			String[] classNo = key.split(" ");

			List<String> listIngredient = new ArrayList<String>();
			for (int i = 0; i < classNo.length; i++) {
				for (String ingredient : listPizza.get(Integer.parseInt(classNo[i])).getIngredient()) {
					listIngredient.add(ingredient);
				}
			}

			// System.out.println("remove lagreDuplicate");

			int duplicate = totalDulicateIngredient(listIngredient);

			if (minThreshold > duplicate) {

				if (!lastkey.equals("")) {
					mapPizzaMax.remove(lastkey);
				}

				minThreshold = duplicate;
				lastkey = key;

				continue;
			}

			mapPizzaMax.remove(key);

		}

	}
}
