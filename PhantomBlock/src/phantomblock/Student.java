package phantomblock;

public class Student {   //student info
	int id;
	int currentFloor;
	int destinationFloor;
	String currentLocation;
	String destinationLocation;
	String activity;
	Classroom assignedClassroom;
	MovementEvent currentEvent;
	private int movementEndTime; 
	public Student(int id, int currentFloor, int destinationFloor) {  //constructor
		this.id = id;
		this.currentFloor = currentFloor;
		this.destinationFloor = destinationFloor;
		
	}
   public void showDetails() {
	   System.out.println("👻 Student " + id + " | Current Floor : " + currentFloor + " | Location: " + currentLocation + " | Destination " + destinationFloor + " | Activity: "+ activity);
   }
   public void move() {
	   currentFloor = destinationFloor;
   }
   public int getId() {
	   return id;
   }
   public void setLocation(String location) {
	   this.currentLocation = location;
   }
   public String getLocation() {
	   return currentLocation;
   }
   public void setDestinationLocation(String location) {
	   this.destinationLocation = location;
   }
   public String getDestinationLocation() {
	   return destinationLocation;
   }
   public void setActivity(String activity) {
	   this.activity = activity;
   }
   public String getActivity() {
	   return activity;
   }
   public void setAssignedClassroom(Classroom classroom) {
	   this.assignedClassroom = classroom;
   }
   public Classroom getAssignedClassroom() {
	   return assignedClassroom;
   }
   public void setCurrentEvent(MovementEvent event) {
	   this.currentEvent = event;
   }
   public MovementEvent getCurrentEvent() {
		return currentEvent;
	}
   public boolean isClassOver(int currentTime) {
	   if(assignedClassroom == null) {
		   return true;
	   }
	   return currentTime >= assignedClassroom.getEndTime();
   }
   public boolean canStartMoving(int currentTime) {
	   if(assignedClassroom == null) {
		   return true;
	   }
	   return currentTime >= assignedClassroom.getEndTime();
   }
   public void setMovementEndTime(int time) {
	   this.movementEndTime = time;
   }
   public int getMovementEndTime() {
	   return movementEndTime;
   }
		   
   
	   
   
}

