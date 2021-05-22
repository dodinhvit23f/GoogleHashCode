
public class Team {
	// số team
	private int number;
	// số người trong team
	private int numberOfPerson;
	
	
	/**
	 * số nhóm còn lại
	 * @return int
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * số nhóm người một nhóm
	 * @return int
	 */
	public int getNumberOfPerson() {
		return numberOfPerson;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setNumberOfPerson(int numberOfPerson) {
		this.numberOfPerson = numberOfPerson;
	}
	
	public void decrease() {
		this.number = this.number - 1;
	}
}
