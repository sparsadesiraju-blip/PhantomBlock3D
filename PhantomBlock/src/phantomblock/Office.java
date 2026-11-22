package phantomblock;

public class Office {
	private int floor;
	private String type;
	private int capacity;
	private int currentPeople = 0;
	public Office(int floor,String type, int capacity) {
		this.floor = floor;
		this.type = type;
		this.capacity = capacity;
	}
	public void addPerson() {
		if(currentPeople < capacity) {
			currentPeople++;
		}
	}
	public void removePerson() {
		if(currentPeople > 0) {
			currentPeople--;
		}
	}
	public int getFloor() {
		return floor;
	}
	public int getCapacity() {
		return capacity;
	}
	public String getType(){
		return type;
	}
	public int getCurrentPeople() {
		return currentPeople;
	}
	public String getStatus() {
		double density = (double) currentPeople / capacity;
		if(density >= 1.0) {
			return "RED - Congested ";
		}
		else if(density >= 0.75) {
			return "ORANGE - Crowded ";
		}
		else if(density >= 0.5) {
			return "YELLOW - Getting Busy ";
		}
		else if(density >= 0.2) {
			return "GREEN - Normal";
		}
		else {
			return "BLUE - Quiet";
		}
		
	}


}
