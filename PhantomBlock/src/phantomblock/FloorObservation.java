package phantomblock;

public class FloorObservation {
    private final double timeSeconds;
    private final int contourCount;
    public FloorObservation(double timeSeconds, int contourCount) {
        if (!Double.isFinite(timeSeconds) || timeSeconds < 0) {
            throw new IllegalArgumentException( "Observation time must be finite and non-negative.");
        }
        if (contourCount < 0) {
            throw new IllegalArgumentException("Contour count cannot be negative.");
        }
        this.timeSeconds = timeSeconds;
        this.contourCount = contourCount;
    }
    public double getTimeSeconds() {
        return timeSeconds;
    }
    public int getContourCount() {
        return contourCount;
    }
}

