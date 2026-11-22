package phantomblock;

public class Classroom {
	private int floor;
	private int capacity;
	private int roomNumber;
	private int currentStudents = 0;
	private int startTime;
	private int endTime;
	public Classroom(int floor,int roomNumber, int capacity, int startTime, int endTime) {
		this.floor = floor;
		this.roomNumber = roomNumber;
		this.capacity = capacity;
		this.startTime = startTime;
		this.endTime = endTime;
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
	public int getRoomNumber() {
		return roomNumber;
	}
	public int getCurrentStudents() {
		return currentStudents;
	}
	public int getStartTime() {
		return startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public boolean isClassOver(int currentTime) {
		return currentTime >= endTime;
	}
	public boolean isClassRunning(int currentTime) {
		return currentTime >= startTime && currentTime < endTime;
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

}
