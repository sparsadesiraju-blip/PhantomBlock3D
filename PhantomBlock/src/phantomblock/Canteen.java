package phantomblock;

public class Canteen {
		private int floor;
		private int capacity;
		private int currentStudents = 0;
		public Canteen(int floor, int capacity) {
			this.floor = floor;
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

	}


