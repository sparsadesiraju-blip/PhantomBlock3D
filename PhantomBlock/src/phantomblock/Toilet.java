package phantomblock;

public class Toilet {
	private int floor;
	private String type;
	private int capacity;
	private int currentStudents = 0;
	public Toilet(int floor,String type,  int capacity) {
		this.floor = floor;
		this.type = type;
		this.capacity = capacity;
	}
	public void addStudent() {
		if(currentStudents < capacity) {
			currentStudents++;
		}
	}
	public void removeStudent() {
		if(currentStudents > 0) {
			currentStudents--;
		}
	}
	public int getFloor() {
		return floor;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getCurrentStudents() {
		return currentStudents;
	}
	public String getStatus() {
		double density = (double) currentStudents / capacity;
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
	public String getType() {
		return type;
	}

}
