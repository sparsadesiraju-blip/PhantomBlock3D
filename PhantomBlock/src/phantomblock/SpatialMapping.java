package phantomblock;

public class SpatialMapping {
    private static final int TOTAL_VIDEO_AREAS = 9;
    private final Building building;
    private final int[] mappedFloorByArea;
    private final String[] mappedSpaceByArea;
    public SpatialMapping(Building building) {
        if (building == null) {
            throw new IllegalArgumentException( "Building cannot be null.");
        }
        if (building.getFloors() == null
                || building.getFloors().length == 0) {
            throw new IllegalArgumentException("Building must contain at least one floor." );
        }
        this.building = building;
        this.mappedFloorByArea = new int[TOTAL_VIDEO_AREAS];
        this.mappedSpaceByArea =
            new String[TOTAL_VIDEO_AREAS];
    }
    public void mapAreaToSpace(int areaNumber,int floorNumber,String spaceName) {
        if (areaNumber < 1|| areaNumber > TOTAL_VIDEO_AREAS) {
            throw new IllegalArgumentException("Area number must be between 1 and 9.");
        }
        if (floorNumber < 1|| floorNumber > building.getFloors().length) {
            throw new IllegalArgumentException("Invalid building floor number.");
        }
        if (spaceName == null || spaceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Space name cannot be empty.");
        }
        int index = areaNumber - 1;
        mappedFloorByArea[index] = floorNumber;
        mappedSpaceByArea[index] = spaceName.trim();
    }
    public boolean isMapped(int areaNumber) {
        validateAreaNumber(areaNumber);
        return mappedFloorByArea[areaNumber - 1] != 0;
    }
    public int getMappedFloor(int areaNumber) {
        validateAreaNumber(areaNumber);
        return mappedFloorByArea[areaNumber - 1];
    }
    public String getMappedSpace(int areaNumber) {
        validateAreaNumber(areaNumber);
        return mappedSpaceByArea[areaNumber - 1];
    }
    public int getMappedAreaCount() {
        int count = 0;
        for (int floor : mappedFloorByArea) {
            if (floor != 0) {
                count++;
            }
        }
        return count;
    }
    public void clearMapping(int areaNumber) {
        validateAreaNumber(areaNumber);
        int index = areaNumber - 1;
        mappedFloorByArea[index] = 0;
        mappedSpaceByArea[index] = null;
    }
    private void validateAreaNumber(int areaNumber) {
        if (areaNumber < 1|| areaNumber > TOTAL_VIDEO_AREAS) {
            throw new IllegalArgumentException( "Area number must be between 1 and 9.");
        }
    }

public void updateBuildingObservations(DigitalTwinState state) {

    if (state == null) {
        throw new IllegalArgumentException(
            "Digital twin state cannot be null."
        );
    }
    int floorCount = building.getFloors().length;
    int[] countsByFloor = new int[floorCount];
    boolean[] hasMappedArea = new boolean[floorCount];
    int[] regionalCounts = state.getRegionalCounts();
    for (int i = 0; i < TOTAL_VIDEO_AREAS; i++) {
        int floorNumber = mappedFloorByArea[i];
        if (floorNumber == 0) {
            continue;
        }
        int floorIndex = floorNumber - 1;
        countsByFloor[floorIndex] += regionalCounts[i];
        hasMappedArea[floorIndex] = true;
    }
    for (int i = 0; i < floorCount; i++) {
    	if (hasMappedArea[i]) {
    	    building.getFloors()[i].recordVideoObservation(
    	        state.getTimeSeconds(),
    	        countsByFloor[i]
    	    );
    	}

    }
}

}

