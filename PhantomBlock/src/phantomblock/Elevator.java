package phantomblock;

public class Elevator {
	private int elevatorId;
	private int capacity;
	private int currentFloor;
	private int currentPassengers = 0;
	private int maxPassengers;
	private int availableAt = 0;
	private int totalOperatingTime = 0;
	
	
	public Elevator(int elevatorId, int capacity) {
		this.elevatorId = elevatorId;
		this.capacity = capacity;
		this.currentFloor = 1;
		this.maxPassengers = 0;
		this.availableAt = 0;

	}
	public  int getElevatorId() {
		return elevatorId;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getMaxPassengers() {
		return maxPassengers;
	}
	public int getCurrentFloor() {
		return currentFloor;
	}
	public void moveToFloor(int destinationFloor) {
		currentFloor = destinationFloor;
	}
	public String getStatus() {
		return "Elevator " + elevatorId + " | Floor : " + currentFloor + " | Passengers : " + currentPassengers + "| Capacity : " + capacity;
	}
	public int getCurrentPassengers() {
		return currentPassengers;
	}
	public boolean addPassenger() {
		if(currentPassengers < capacity) {
			currentPassengers++;
			if(currentPassengers > maxPassengers) {
				maxPassengers = currentPassengers;
			}
			return true;
		}
		return false;
	}
	public int addPassengers(int number) {
		int added = 0;
		while(added < number && currentPassengers < capacity) {
			currentPassengers ++ ;
			added ++;
			if(currentPassengers > maxPassengers) {
				maxPassengers = currentPassengers;
			}
		}
		return added;
	}
	public void removePassenger() {
		if(currentPassengers > 0) {
			currentPassengers--;
		}
	}
		public boolean isAtFloor(int floor) {
			return currentFloor == floor;
	}
		public int getAvailable() {
			return availableAt;
		}
		public void setAvailable(int availableAt) {
			this.availableAt = availableAt;
		}
		
		public boolean isAvailable(int currentTime) {
			return availableAt <= currentTime;
		}
		public void addOperatingTime(int time) {
			totalOperatingTime += time;
			
		}
		public int getTotalOperatingTime(int time) {
			return totalOperatingTime;
		
		}
		
	
	


}
