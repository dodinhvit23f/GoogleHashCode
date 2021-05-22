import java.util.ArrayList;
import java.util.List;

public class Pizza {
	// Số nguyên liệu trong bánh
	private int numberOfIngredient;
	// tên các nguyên liệu
	private String[] ingredients;
	// số lượng bánh
	private int number;
	// vi tri cua banh
	private List<Integer> position = new ArrayList<Integer>();

	public int getNumberOfIngredient() {
		return numberOfIngredient;
	}

	public String[] getIngredient() {
		return ingredients;
	}

	public int getNumber() {
		return number;
	}

	public void setNumberOfIngredient(int numberOfIngredient) {
		this.numberOfIngredient = numberOfIngredient;
	}

	public void setIngredient(String[] ingredients) {
		this.ingredients = ingredients;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<Integer> getPosition() {
		return position;
	}

	public void setPosition(List<Integer> position) {
		this.position = position;
	}

	public void incrase() {
		this.number = number + 1;
	}

	public void AddPosition(int number) {
		this.position.add(number);
	}

	public void RemovePosition(int number) {
		if (this.position.isEmpty()) {
			return;
		}
		
		this.position.remove(0);
	}

	public int checkSameIngredient(Pizza pizza) {
		int numberOfIngredientSame = 0;

		for (String this_ingredient : this.ingredients) {
			for (String that_ingredient : pizza.getIngredient()) {

				if (this_ingredient.equals(that_ingredient)) {
					numberOfIngredientSame = numberOfIngredientSame + 1;
					break;
				}

			}
		}

		return numberOfIngredientSame;
	}

	public boolean equals(Pizza pizza) {
		if (this.numberOfIngredient != pizza.getNumberOfIngredient()) {
			return false;
		}

		if (this.numberOfIngredient != checkSameIngredient(pizza)) {
			return false;
		}

		return true;
	}

}
