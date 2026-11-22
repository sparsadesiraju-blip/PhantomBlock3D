package phantomblock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Floor {
	int floorNumber;
	int studentCount;
	private double floorArea;
	private double areaPerStudent;
	private double restrictedArea;
	private double usableArea;
	private int observedContourCount = 0;
	private boolean hasVideoObservation = false;
	private final List<FloorObservation> observationHistory = new ArrayList<>();
	public Floor(int floorNumber , double floorArea, double areaPerStudent, double restrictedArea) {
		this.floorNumber = floorNumber;
		this.floorArea = floorArea;
		this.areaPerStudent = areaPerStudent;
		this.studentCount = 0;
		this.restrictedArea = restrictedArea;
		this.usableArea = floorArea - restrictedArea;
	}
	public double getUsableArea() {
		return usableArea;
	}
	public double getRestrictedArea() {
		return restrictedArea;
	}
	private int yellowRounds = 0;
	private int orangeRounds = 0;
	private int redRounds = 0;
	private int yellowEvents = 0;
	private int orangeEvents = 0;
	private int redEvents = 0;
	private int floorCapacity = 10;
	private String previousStatus = "";
	private int congestionStartTime = -1;
	private int recoveryTime = 0;
	private int getSeverity(String status){
	    if (status.startsWith("RED")) {
	        return 4;
	    }
	    else if (status.startsWith("ORANGE")) {
	        return 3;
	    }
	    else if (status.startsWith("YELLOW")) {
	        return 2;
	    }
	    else if (status.startsWith("GREEN")) {
	        return 1;
	    }
	    else {
	        return 0; // BLUE
	    }
	}

	public void updateCongestionDuration(int currentTime){
		String currentStatus = getDensityStatus();
		if(! currentStatus.startsWith("GREEN") && !currentStatus.startsWith("BLUE") && congestionStartTime == -1) {
			congestionStartTime = currentTime;
		}
		if(currentStatus.startsWith("YELLOW")) {
			yellowRounds++;
			if(! previousStatus.startsWith("YELLOW")) {
				yellowEvents++;
			}
		}
		else if (currentStatus.startsWith("ORANGE")) {
			orangeRounds++;
			if ( !previousStatus.startsWith("ORANGE")) {
				orangeEvents++;
			}	
		}
		else if(currentStatus.startsWith("RED")) {
			redRounds++;
			 if(!previousStatus.startsWith("RED")) {
				redEvents++;
		}
	}
		if ((currentStatus.startsWith("GREEN") || currentStatus.startsWith("BLUE"))
		        && congestionStartTime != -1) {

		    recoveryTime = currentTime - congestionStartTime;

		    System.out.println(
		        "Congestion recovered in " + recoveryTime + " seconds."
		    );

		    congestionStartTime = -1;
		}
		previousStatus = currentStatus;
	}
	public int getYellowRounds() {
		return yellowRounds;
	}
	public int getOrangeRounds() {
		return orangeRounds;
	}
	public int getRedRounds() {
		return redRounds;
	}
	public int getYellowEvents() {
		return yellowEvents;
	}
	public int getOrangeEvents() {
		return orangeEvents;
	}
	public int getRedEvents() {
		return redEvents;
	}
	
	public Floor(int floorNumber) {
		this.floorNumber = floorNumber;
		this.studentCount = 0;
	}
	public void addStudent() {
		studentCount++;
	}
	public void removeStudent() {
		if(studentCount > 0) {
			studentCount --;
		}
	}
	
	public String getDensityStatus() {
		if(studentCount < 1) {
			return "BLUE - Isolated";
		}
		else if (studentCount <= 3) {
			return "GREEN - Comfortable";
		}
		else if(studentCount <= 5) {
			return "YELLOW - Getting busy";
		}
		else if(studentCount <= floorCapacity) {
			return "ORANGE - Crowded";
		}
		else {
			return "RED - Congested";
		}
	}
	public void showFloor() {
		System.out.println("Floor " + floorNumber + " | Students: " + studentCount +" | Floor Area: "+ floorArea + " m2" + " | Occupied Area: "+ getOccupiedArea() + " m2" + " | Occupancy : " + String.format("%.2f", getOccupancyPercentage()) + "%" + "| Status: " + getDensityStatus());
	}
	public double getOccupiedArea() {
		return studentCount * areaPerStudent;
	}
	public double getOccupancyPercentage() {
		    if (usableArea <= 0) {
		        return 0;
		    }
		return (getOccupiedArea() / usableArea) * 100;
	}
	public double getFloorArea() {
		return floorArea;
	}
	public double getAreaPerStudent() {
		return areaPerStudent;
	}
	public void updateVideoObservation(int contourCount) {
	    if (contourCount < 0) {
	        throw new IllegalArgumentException( "Contour count cannot be negative.");
	    }
	    observedContourCount = contourCount;
	    hasVideoObservation = true;
	}
	public int getObservedContourCount() {
	    return observedContourCount;
	}
	public boolean hasVideoObservation() {
	    return hasVideoObservation;
	}
	public void recordVideoObservation(
	        double timeSeconds, int contourCount) {

	    if (!Double.isFinite(timeSeconds) || timeSeconds < 0) {
	        throw new IllegalArgumentException(
	            "Observation time must be finite and non-negative."
	        );
	    }

	    if (contourCount < 0) {
	        throw new IllegalArgumentException(
	            "Contour count cannot be negative."
	        );
	    }

	    if (!observationHistory.isEmpty()) {
	        double previousTime =
	            observationHistory.get(
	                observationHistory.size() - 1
	            ).getTimeSeconds();

	        if (timeSeconds < previousTime) {
	            throw new IllegalArgumentException(
	                "Floor observations must be chronological."
	            );
	        }
	    }

	    updateVideoObservation(contourCount);

	    observationHistory.add(
	        new FloorObservation(timeSeconds, contourCount)
	    );
	}

	public List<FloorObservation> getObservationHistory() {
	    return Collections.unmodifiableList(
	        new ArrayList<>(observationHistory)
	    );
	}

	public int getObservationHistoryCount() {
	    return observationHistory.size();
	}
	public double getLatestObservationTimeSeconds() {
	    if (observationHistory.isEmpty()) {
	        return -1.0;
	    }
	    return observationHistory.get(
	        observationHistory.size() - 1
	    ).getTimeSeconds();
	}
	
	public void showAreaDetails() {
	    System.out.println("Floor " + floorNumber +
	            " | Total Area: " + floorArea + " m²" +
	            " | Restricted Area: " + restrictedArea + " m²" +
	            " | Usable Area: " + usableArea + " m²" +
	            " | Occupied Area: " + getOccupiedArea() + " m²" +
	            " | Occupancy: " +
	            String.format("%.2f", getOccupancyPercentage()) + "%");
	    }
	public int getRecoveryTime() {
	    return recoveryTime;
	}
	}

