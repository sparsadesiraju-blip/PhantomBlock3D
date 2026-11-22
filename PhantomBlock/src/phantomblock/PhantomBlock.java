package phantomblock;
import java.io.FileWriter;  //used to write text data .. used it for saving results of frames in a csv.
import java.io.IOException; // to handle errors in csv or file uploading
import java.util.Random; // to assign random numbers such as classes, floors or students 
public class PhantomBlock {
    public static void main(String[] args) {
      Building building = new Building(7, 2);
     System.out.println("===== ELEVATOR TEST ======");
       for(Elevator elevator : building.getElevators()) { //to call elevators and assign them to students
    	  System.out.println(elevator.getStatus());
      }
      Elevator testelevator = building.findAvailableElevator(1, 2); //finding available elevators
      if(testelevator != null) {
    	  System.out.println("Student boarded Elevator " + testelevator.getElevatorId());
    	  System.out.println(testelevator.getStatus()); //test code to see if elevators are working
      }
      building.getElevators()[0].addPassenger();
     System.out.println(building.getElevators()[0].getStatus()); // addition and removal of passengers
      building.getElevators()[0].removePassenger();
      System.out.println(testelevator.getStatus());
      System.out.println(building.getElevators()[0].getStatus());
      
      
      building.getElevators()[0].moveToFloor(5);
      System.out.println(building.getElevators()[0].getStatus());
       Random random = new Random();
       int numberOfStudents = 50;  //data for simulation
       int numberOfFloors = 7;
       int numberOfRounds = 5;
       System.out.println("NUMBER OF ROUNDS = "+ numberOfRounds); //time for each simulation
       int secondsPerRound = 100;
       int simulationTime = 0;
       int secondsPerFloor = 1;
       int secondsPerPassenger = 1;
        Student[] students = new Student[numberOfStudents]; //finding out where students are 
        for (int i = 0; i < numberOfStudents; i++) {
            int currentFloor =
                    random.nextInt(numberOfFloors) + 1;
            int destinationFloor =
            		random.nextInt(numberOfFloors) + 1;
            students[i] =
                    new Student(i + 1, currentFloor, destinationFloor);
            int classRoomIndex = (currentFloor - 1) * 10 + (int)(Math.random() * 10);
            students[i].setAssignedClassroom(building.getClassrooms()[classRoomIndex]);
            students[i].setLocation(building.getRandomLocation(currentFloor));
            String destinationLocation = building.getRandomLocation(destinationFloor);
            students[i].setDestinationLocation(destinationLocation);
            String activity = building.getRandomActivity();
            students[i].setActivity(activity);
            if(activity.equals("Class")) {
            	int  room = (int)(Math.random() * 10) + 1;
            	destinationLocation = "Classroom "+(destinationFloor * 100 + room);
            }
            else if(activity.equals("Lab")) {
            	int  room = (int)(Math.random() * 2) + 1;
            	destinationLocation = "Lab"+(destinationFloor * 100 + room);
            }
            else if(activity.equals("Canteen")) {
            	int canteen = (int)(Math.random() * 2) + 1;
            	destinationLocation = "Canteen "+ canteen;
            }
            else{
            	destinationLocation = "Toilet - Floor"+ destinationFloor;
            }
            students[i].setDestinationLocation(destinationLocation);
            
            students[i].showDetails();
            building.floors[currentFloor - 1].addStudent();
            int eventStartTime = simulationTime;
            int eventDuration;
            if(currentFloor != destinationFloor) {
            	eventDuration = Math.abs(currentFloor - destinationFloor)* secondsPerFloor + secondsPerPassenger;
            }
            else {
            	eventDuration =  secondsPerPassenger;
            }
            
            MovementEvent event = new MovementEvent(students[i] ,activity, students[i].getLocation(), students[i].getDestinationLocation(),eventStartTime, eventDuration);
            students[i].setCurrentEvent(event);
            System.out.println(
            	    "Movement Event | Student: " + event.getStudent().getId()
            	    + " | Reason: " + event.getReason()
            	    + " | From: " + event.getOrigin()
            	    + " | To: " + event.getDestination()
            	    + " | Start: " + event.getStartTime()
            	    + " | Duration: " + event.getDuration()
            	    + " | End: " + event.getEndTime()
            	);
        }
        
        for(Elevator elevator : building.getElevators()) {
        	System.out.println("Elevator " + elevator.getElevatorId() + " peak passengers " + elevator.getMaxPassengers());
        	
        }
        int overallMaxStudents = 0; // to calculate the past rounds and congestion history
        int overallMaxFloor = 0;
        int overallMaxElevatorPassengers = 0;
        double[][] occupancyHistory = new double[numberOfRounds][numberOfFloors];
        for(int round = 1;round <= numberOfRounds; round++) {
        	System.out.println();
        	System.out.println("------ROUND " + round + "--------");
        	simulationTime += secondsPerRound;	
        	System.out.println("Simulation Time: " + simulationTime + " seconds");
        	
        for (int i = 0; i < numberOfStudents; i++) {
            int oldFloor = students[i].currentFloor;  // locating and de locating students
            if(students[i].getMovementEndTime() > simulationTime) {
            	System.out.println("Student " + students[i].getId() + " is still moving");
            	continue; 
            }
            System.out.println("Student " + students[i].getId() + " | Simulation Time: " + simulationTime + " | Class End: "+ students[i].getAssignedClassroom().getEndTime());
            if(! students[i].canStartMoving(simulationTime)) {
            	System.out.println("Student " + students[i].getId() + " is still in class");
            	continue;
            }
            else {
            	System.out.println("Student " + students[i].getId() + " are moving");
            }
            int newFloor ;
            do {
            	newFloor = random.nextInt(numberOfFloors) + 1;
            }
            while(newFloor == students[i].currentFloor);
            
            System.out.println("Student " + (i + 1) + " : Floor " + oldFloor + " -> Floor " + newFloor);
            students[i].destinationFloor = newFloor;
            Elevator elevator = building.findAvailableElevator(oldFloor, simulationTime);
           
            if(elevator != null) {
            	int groupSize = random.nextInt(4) + 1;
            	int boarded = elevator.addPassengers(groupSize);
            
            	if(elevator.getMaxPassengers() > overallMaxElevatorPassengers) {
            		overallMaxElevatorPassengers = elevator.getMaxPassengers();
            	}
            	int distance = Math.abs(newFloor - oldFloor);
            	int travelTime = distance * secondsPerFloor;
            	int boardingTime = boarded * secondsPerPassenger;
            	int exitTime = boarded * secondsPerPassenger;
            	int totalElevatorTime = travelTime + boardingTime + exitTime;
            	elevator.addOperatingTime(totalElevatorTime);
            	int movementEndTime =  simulationTime + totalElevatorTime;
                students[i].setMovementEndTime(movementEndTime);
            
            	System.out.println("Elevator trip time : " + totalElevatorTime + " seconds");
            	//simulationTime += totalElevatorTime;
            	System.out.println("Current Simulation Time: " + simulationTime + " seconds");
            	elevator.moveToFloor(oldFloor);
            	elevator.moveToFloor(newFloor);
            	System.out.println(elevator.getStatus());
            	System.out.println("Elevator " + elevator.getElevatorId() + " moved from Floor " + oldFloor + " To Floor " + newFloor);
            	if(elevator.getMaxPassengers() > overallMaxElevatorPassengers) {
            		overallMaxElevatorPassengers = elevator.getMaxPassengers();
            	}
            	for(int p = 0; p < boarded ; p++) {
            		elevator.removePassenger();
            	}
            }
            building.floors[oldFloor - 1].removeStudent();
            students[i].currentFloor = newFloor;
            students[i].move();
            building.floors[newFloor - 1].addStudent();
            building.corridor[oldFloor - 1].removeStudent();
            building.corridor[newFloor - 1].addStudent();
       for(int j = 0; j < numberOfFloors ; j++)   {
    	   occupancyHistory[round - 1][j] = building.floors[j].getOccupancyPercentage();
       }
       System.out.println();
       System.out.println("========CONGESTION HISTORY==========");
       for(int rounds = 0; rounds < numberOfRounds; rounds++) {
    	System.out.println("Round " + (rounds + 1)+ " | ")  ;
    	for(int floor = 0; floor< numberOfFloors; floor++) {
    		System.out.println("Floor "+ (floor + 1) + ": " + String.format(".%2f",  occupancyHistory[rounds][floor]) + "% | ") ;
    	}
       }
       for(int j = 0; j< numberOfFloors; j++) {
    	 building.floors[j].showAreaDetails();  
       }
       System.out.println();
    }
        for(int i = 0; i < numberOfFloors ; i++) {
    		building.floors[i].updateCongestionDuration(simulationTime);
    	}

        int maxStudents = 0;
        int maxFloor = 0;
        for(int i = 0; i< numberOfFloors; i++) {
        	if(building.floors[i].studentCount > maxStudents) {
        		maxStudents = building.floors[i].studentCount;
        		maxFloor = i + 1;
        	}
        	
        	if(maxStudents > overallMaxStudents) {
        		overallMaxStudents = maxStudents;
        		overallMaxFloor = maxFloor;
        }
        
        building.showBuilding();
        System.out.println("Most crowded floor: " + maxFloor);
        System.out.println("Maximum Student : " + maxStudents);
        building.showCorridors();
        building.showCanteens();
        building.showToilets();
        building.showOffices();
        building.showClassSchedule(60);
		
}
        }
        System.out.println();
        System.out.println("========== FINAL SIMULATION SUMMARY ============");
        System.out.println("Overall Maximum Students on a Floor : " + overallMaxStudents);
        System.out.println("Floor with Maximum Congestion : Floor " + overallMaxFloor);
        System.out.println("Overall Maximum Passengers in the Elevator :" + overallMaxElevatorPassengers);
        System.out.println("Overall Busiest Floor : Floor " + overallMaxFloor + " With " + overallMaxStudents + " students");
        System.out.println("=================================================");
        for(int i = 0;i < numberOfFloors; i++) {
        	building.floors[i].updateCongestionDuration(simulationTime);
        }
        System.out.println();
        System.out.println("====== SIMULATION SUMMARY ======");
        System.out.println("Highest congestion : " + overallMaxStudents + " students");
        System.out.println("Occured on Floor : " + overallMaxFloor);
        System.out.println();
        System.out.println("-------CONGESTION DURATION-------");
        for(int i = 0; i < numberOfFloors; i++) {
        	System.out.println(
        		    "Floor " + (i + 1)
        		    + " | Yellow: " + building.floors[i].getYellowRounds() + " rounds"
        		    + " (" + building.floors[i].getYellowEvents() + " events)"
        		    + " | Orange: " + building.floors[i].getOrangeRounds() + " rounds"
        		    + " (" + building.floors[i].getOrangeEvents() + " events)"
        		    + " | Red: " + building.floors[i].getRedRounds() + " rounds"
        		    + " (" + building.floors[i].getRedEvents() + " events)"
        		);
    }
        try {
            FileWriter writer = new FileWriter("phantomblock_durations.csv");

            writer.append("Floor,TotalCongestionDuration\n");

            for (int i = 0; i < numberOfFloors; i++) {
                writer.append(String.valueOf(i + 1));
                writer.append(",");
                writer.append(String.valueOf(building.floors[i].getRecoveryTime()));
                writer.append("\n");
            }

            writer.close();
            System.out.println("Congestion durations exported successfully!");

        } catch (IOException e) {
            System.out.println("Error exporting congestion durations.");
            e.printStackTrace();
        }
    
    for(int round = 0; round < numberOfRounds ;  round++) {
    	//printing history
    }
    try {
    	FileWriter writer = new FileWriter("congestion_history.csv"); //adding new data
    	writer.append("Round,Floor,OccupancyPercentage\n");
    	for(int round = 0 ; round < numberOfRounds ; round++) {
    		for(int floor = 0; floor < numberOfFloors; floor++) {
    			writer.append(String.valueOf(round + 1));
    			writer.append(" , ");
    			writer.append(String.valueOf(floor + 1));
    			writer.append(" ,");
    			writer.append(String.valueOf(occupancyHistory[round][floor]));
    			writer.append("\n");
    		}
    	}
    	writer.flush(); //forcing any data in the buffer to be written out immediately
    	writer.close();
    	System.out.println("CSV file created successfully !");
    	
    }
    catch (IOException e) {
    	System.out.println("Error creating CSV file.");
    	e.printStackTrace();
    }
    Graph.showGraph(occupancyHistory, numberOfRounds, numberOfFloors);
    Graph.showCongestionDurationGraph(building.floors, numberOfFloors);
    	
    }
}

