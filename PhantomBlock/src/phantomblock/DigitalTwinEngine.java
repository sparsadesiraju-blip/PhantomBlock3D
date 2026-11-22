package phantomblock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class DigitalTwinEngine {
    private final List<DigitalTwinState> history =new ArrayList<>();
    private DigitalTwinState currentState;
    private final Building building;
    private final SpatialMapping spatialMapping;
    public DigitalTwinEngine(Building building) {
        if (building == null) {
            throw new IllegalArgumentException(
                "Building cannot be null.");
        }
        if (building.getFloors() == null|| building.getFloors().length == 0) {
            throw new IllegalArgumentException("Building must contain at least one floor." );
        }
        this.building = building;
        this.spatialMapping = new SpatialMapping(building);
    }
    public void updateState(DigitalTwinState newState) {

        if (newState == null) {
            throw new IllegalArgumentException("Digital twin state cannot be null.");
        }
        if (currentState != null
                && newState.getTimeSeconds()< currentState.getTimeSeconds()) {
            throw new IllegalArgumentException("Observations must be added in chronological order.");
        }
        spatialMapping.updateBuildingObservations(newState);
        history.add(newState);
        currentState = newState;
    }
    public DigitalTwinState getCurrentState() {
        return currentState;
    }
    public int getBuildingFloorCount() {
        return building.getFloors().length;
    }
    public SpatialMapping getSpatialMapping() {
        return spatialMapping;
    }
    public List<DigitalTwinState> getHistory() {
        return Collections.unmodifiableList(new ArrayList<>(history));
    }
    public int getObservationCount() {
        return history.size();
    }
    public void printCurrentState() {
        if (currentState == null) {
            System.out.println("The digital twin has no observations yet.");
            return;
        }
        System.out.println("\n=== PHANTOMBLOCK DIGITAL TWIN ===");
        System.out.printf("Observation time: %.3f seconds%n",currentState.getTimeSeconds());
        System.out.println("Total detected contours: "+ currentState.getTotalContours());
        System.out.println("Highest-activity region: Area "+ currentState.getHighestActivityRegion());
        int[] counts = currentState.getRegionalCounts();
        for (int i = 0; i < counts.length; i++) {
            System.out.println("Area " + (i + 1)+ ": " + counts[i]+ " contours | "+ currentState.getActivityLevel(i));
        }
        System.out.println("Observations stored: " + history.size() );
    }
}

