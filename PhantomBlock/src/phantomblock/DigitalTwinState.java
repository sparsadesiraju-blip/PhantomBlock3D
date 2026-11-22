package phantomblock;
import java.util.Arrays;
public class DigitalTwinState {
    private final double timeSeconds;
    private final int[] regionalCounts;
    private final int totalContours;
    public DigitalTwinState(double timeSeconds,int[] regionalCounts,int totalContours) {
        if (regionalCounts == null || regionalCounts.length != 9) {
            throw new IllegalArgumentException( "Exactly nine regional counts are required.");
        }
        this.timeSeconds = timeSeconds;
        this.regionalCounts = Arrays.copyOf(regionalCounts, 9);
        this.totalContours = totalContours;
    }
    public double getTimeSeconds() {
        return timeSeconds;
    }
    public int[] getRegionalCounts() {
        return Arrays.copyOf(regionalCounts, 9);
    }
    public int getTotalContours() {
        return totalContours;
    }
    public int getHighestActivityRegion() {
        int highestIndex = 0;
        for (int i = 1; i < regionalCounts.length; i++) {
            if (regionalCounts[i] > regionalCounts[highestIndex]) {
                highestIndex = i;
            }
        }
        return highestIndex + 1;
    }
    public String getActivityLevel(int regionIndex) {
        if (regionIndex < 0 || regionIndex >= 9) {
            throw new IndexOutOfBoundsException("Region index must be between 0 and 8.");
        }
        int count = regionalCounts[regionIndex];
        if (count <= 2) return "LOW";
        if (count <= 4) return "MODERATE";
        if (count <= 6) return "HIGH";
        return "VERY HIGH";
    }
}

