package phantomblock;

public class Building { //building details 
	private double length;
	private double width;
	private double area;
	int numberOfFloors;
	private double floorArea;
	private double areaPerStudent;
	private double buildingArea;
	double restrictedArea;
	Floor[] floors;  
	Elevator[] elevators;
	Elevator facultyElevator;
	Corridor[] corridor;
	Canteen[] canteen;
	Toilet[] toilet;
	Classroom[] classroom;
	Lab[] labs;
	Office[] offices;
	int numberOfElevators;
	public Building(int numberOfFloors, int numberOfElevators) {
		this.numberOfFloors = numberOfFloors;
		this.length = 50;
		this.width = 30;
		this.area = length  * width;
		this.floorArea = this.area;
		this.areaPerStudent = 2.0;
		this.buildingArea = numberOfFloors * floorArea;
		this.restrictedArea = 200.0;
		floors = new Floor[numberOfFloors];  //floor constructor 
		for( int i = 0; i <numberOfFloors ; i++) {
			floors[i] = new Floor( i + 1, floorArea, areaPerStudent, restrictedArea);
		}
		this.numberOfElevators = numberOfElevators;
		elevators = new Elevator[numberOfElevators];
		for(int i = 0; i < numberOfElevators; i++) {
			elevators[i] = new Elevator( i + 1 , 10);	
		}
		facultyElevator = new Elevator(3, 6);
		corridor = new Corridor[numberOfFloors];
		for(int i = 0; i < numberOfFloors ; i++) {
			corridor[i] = new Corridor( i + 1 , 30);
		}
		canteen = new Canteen[2];    //canteen constructor
		canteen[0] = new  Canteen(1, 50); 
		canteen[1] = new Canteen(3, 50);
		
		toilet = new Toilet[numberOfFloors * 4]; //constructor for toilets
		int index = 0;
		for(int i = 0; i < numberOfFloors; i++) {
			toilet[index++] = new Toilet( i+1, "Boys", 10);
			toilet[index++] = new Toilet( i+1, "Boys", 10);
			toilet[index++] = new Toilet( i+1, "Girls", 10);
			toilet[index++] = new Toilet( i+1, "Girls", 10);
			
		}
		classroom = new Classroom[numberOfFloors * 10]; //classroom constructor
		int classIndex = 0;
		for(int i = 0; i < numberOfFloors; i++) {
			for(int j = 1; j <= 10; j++) {
				int roomNumber = (i+1)*100 + j;
				int period = j % 4;
				int startTime;
				int endTime;
				if(period == 0) {   //start time and end time based on class period duration 
					startTime = 0;
					endTime = 50;
				}
				else if(period == 1) {
					startTime = 50;
					endTime = 100;
				}
				else if(period == 2) {
					startTime = 140;
					endTime = 200;
				}
				else {
					startTime = 210; //cycle split in 4 ..will continue after lunch
					endTime = 260;
				}
			classroom[classIndex++] = new Classroom(i+1, roomNumber, 40, startTime, endTime);
				
			}
		}
		labs = new Lab[numberOfFloors * 2];  //lab constructor
		int labIndex = 0;
		for(int i = 0; i < numberOfFloors; i++) {
			for(int j = 1; j <= 2; j++) {
				int labNumber = (i+1)*100 + j;
				labs[labIndex++] = new Lab(i+1, labNumber, 40);
				
			}
		}
		offices = new Office[5];       //office constructor
		offices[0] = new Office(2, "Faculty Staffroom", 20);
		offices[1] = new Office(7, "Faculty Staffroom", 20);
		offices[2] = new Office(1, "Staff Office", 20);
		offices[3] = new Office(2, "Staff Office", 20);
		offices[4] = new Office(7, "Main Office", 30);
		
		
	}
	public Building(int numberOfFloors, double floorArea , double areaPerStudent, double restrictedArea) { //building constructor
		this.floorArea = floorArea;
		this.areaPerStudent = areaPerStudent;
		this.buildingArea = numberOfFloors * floorArea;
		floors = new Floor[numberOfFloors];
		for(int i = 0; i < numberOfFloors; i++) {
			floors[i] = new Floor(i+1, floorArea, areaPerStudent, restrictedArea);
		}
	}
	public void showBuilding() {
		System.out.println("🏣 Phantom Block has " + numberOfFloors + " floors");
		System.out.println("Building Length : " + length + " m");
		System.out.println("Building Width : " + width + " m");
		System.out.println("Building Area : " + area + " m2");
		System.out.println("Classrooms: "+ classroom.length);
		System.out.println("Labs: "+ labs.length);
		System.out.println("Corridors: "+ corridor.length);
		System.out.println("Canteens: "+ canteen.length);
		System.out.println("Toilet Units: "+ toilet.length);
		System.out.println("Faculty Elevator: Available");
		
		
		for(int i = 0; i < numberOfFloors; i++ ) {
			floors[i].showFloor();
		}
	}
	public void showCorridors() {
		System.out.println("------CORRIDORS-------");
		for(int i = 0; i < corridor.length ; i++) {
		System.out.println("Corridor "+ (i + 1) + " | Students: "+ corridor[i].getCurrentStudents() + "/" + corridor[i].getCapacity() + " | Status: " + corridor[i].getStatus());
		}
	}
	public void showCanteens() {
		System.out.println("------CANTEENS-------");
		for(int i = 0; i < canteen.length ; i++) {
		System.out.println("Canteen "+ (i + 1) + "|Floor:  "+ canteen[i].getFloor() +  "| Students: "+ canteen[i].getCurrentStudents() + "/" + canteen[i].getCapacity() + " | Status: " + canteen[i].getStatus());
		}
	}
	public void showToilets() {
		System.out.println("------TOILETS-------");
		for(int i = 0; i < toilet.length ; i++) {
		System.out.println( "|Floor:  "+ toilet[i].getFloor() + " |  " + toilet[i].getType() + "| Students: "+ toilet[i].getCurrentStudents() + "/" + toilet[i].getCapacity() + " | Status: " + toilet[i].getStatus());
		}
	}
	public void showOffices() {
		System.out.println("------OFFICES-------");
		for(int i = 0; i < offices.length ; i++) {
		System.out.println( offices[i].getType() + "|Floor:  "+ offices[i].getFloor() +  "| People: "+ offices[i].getCurrentPeople() + "/" + offices[i].getCapacity() + " | Status: " + offices[i].getStatus());
		}
	}
	public void showClassSchedule(int currentTime) {
		System.out.println("-------CLASS SCHEDULE---------");
		for(int i = 0; i < classroom.length; i++) {
			if(classroom[i].isClassRunning(currentTime)) {
				System.out.println("Room "+ classroom[i].getRoomNumber() + " | Floor: " + classroom[i].getFloor() + " | CLASS RUNNING" );
			}
			else if(classroom[i].isClassOver(currentTime)) {
				System.out.println("Room " + classroom[i].getRoomNumber()+ " | Floor: "+ classroom[i].getFloor() + " | CLASS OVER ");
			}
		}
	}
	public Elevator getFacultyElevator() {
		return facultyElevator;	
	}
	
	public Floor[] getFloors() {
		return floors;
	}
	public Floor getFloor(int floorNumber) {
	    if (floorNumber < 1 || floorNumber > floors.length) {
	        throw new IllegalArgumentException("Invalid floor number.");
	    }
	    return floors[floorNumber - 1];
	}
	public double getBuildingArea() {
		return buildingArea;
	}
	public void showBuildingArea() {
		System.out.println("Total Building Area:" + buildingArea + "m2");
	}
	public double getLength() {
		return length;
	}
	public double getWidth() {
		return width;
	}
	public double getArea() {
		return area;
	}
    public Elevator [] getElevators() {
    	return elevators;
    }
    public Classroom[] getClassrooms() {
    	return classroom;
    }
    public Elevator findAvailableElevator(int floor, int currentTime) {
    int bestScore = Integer.MAX_VALUE;
    Elevator bestElevator = null;
    for(Elevator elevator : elevators) {
    	System.out.println("DEBUG ELEVATOR " + elevator.getElevatorId() +" | Passengers: " + elevator.getCurrentPassengers() + " | Capacity: "+ elevator.getCapacity()+ " | Available " + elevator.getAvailable() + " | Current Floor : "+ elevator.getCurrentFloor());
    	
    	if(elevator.getCurrentPassengers() < elevator.getCapacity() && elevator.getAvailable() <= 0){
    		int distance = Math.abs(elevator.getCurrentFloor() - floor);
    		int passengerLoad = elevator.getCurrentPassengers();
    		int score = distance + passengerLoad;
    	if(score < bestScore) {
    		bestScore = score;
    		bestElevator = elevator;
    	}
    	}
    	}
    
    return bestElevator;
}
public String getRandomLocation(int floor) {
	int choice = (int)(Math.random() * 4);
	if(choice == 0) {
		int room =  (int)(Math.random() * 10) + 1;
		return "Classroom "+ (floor * 100 + room);
	}
	else if(choice == 1) {
		int lab =  (int)(Math.random() * 2) + 1;
		return "Lab "+ (floor * 100 + lab);
	}
	else if(choice == 2) {
		return "Corridor - Floor "+ floor;
	}
	else {
		return "Toilet - Floor "+ floor;
	}
    	
    }
public String getRandomActivity() {
	int choice = (int)(Math.random() * 4);
	if(choice == 0) {
		return "Class ";
	}
	else if(choice == 1) {
		return "Lab ";
	}
	else if(choice == 2) {
		return "Canteen";
	}
	else {
		return "Toilet ";
	}
    	
    }
}

