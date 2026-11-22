
package phantomblock;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;
public class VideoObservationReader {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\D.spandana\\phantomblock_video_observations.csv";
        int rowsRead = 0;
        double totalContours = 0;
        List<Double> timeValues = new ArrayList<>();
        List<Integer> totalValues = new ArrayList<>();
        List<int[]> regionalValues = new ArrayList<>();
long[] regionalTotals = new long[9];
int peakTotal = -1;
int peakFrame = -1;
double peakTime = 0;
int increases = 0;
int decreases = 0;
int previousTotal = -1;
        try (BufferedReader br =
                 new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine(); // Skip header
            if (line == null) {
                System.out.println("CSV file is empty.");
                return;
            }
            System.out.println("Reading video observations...");
            System.out.println("--------------------------------");
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                if (values.length != 12) {
                    System.out.println(
                        "Skipping invalid row: " + line
                    );
                    continue;
                }
                try {
                    int frame = Integer.parseInt(values[0]);
                    double timeSeconds = Double.parseDouble(values[1]);
                    int[] areaCounts = new int[9];
                    for (int i = 0; i < 9; i++) {
                        areaCounts[i] =
                            Integer.parseInt(values[i + 2]);
                    }
                    int total = Integer.parseInt(values[11]);
                    timeValues.add(timeSeconds);
                    totalValues.add(total);
                    rowsRead++;
                    totalContours += total;
                    rowsRead++;
                    totalContours += total;
for (int i = 0; i < 9; i++) {
    regionalTotals[i] += areaCounts[i];
}
regionalValues.add(areaCounts);
if (total > peakTotal) {
    peakTotal = total;
    peakFrame = frame;
    peakTime = timeSeconds;
    
}
if (previousTotal != -1) {
    if (total > previousTotal) {
        increases++;
    } else if (total < previousTotal) {
        decreases++;
    }
}
previousTotal = total;
                    rowsRead++;
                    totalContours += total;
                    if (rowsRead <= 5) {
                        System.out.println(
                            "Frame: " + frame
                            + " | Time: " + timeSeconds + " s"
                            + " | Regions: "
                            + java.util.Arrays.toString(areaCounts)
                            + " | Total contours: " + total
                        );
                    }
                } catch (NumberFormatException e) {
                    System.out.println(
                        "Skipping row with invalid numbers."
                    );
                }
            }

double[] times = new double[timeValues.size()];
int[] totals = new int[totalValues.size()];
for (int i = 0; i < timeValues.size(); i++) {
    times[i] = timeValues.get(i);
    totals[i] = totalValues.get(i);
}

VideoObservationGraph.showGraph(times, totals);
int[][] regionalData = new int[regionalValues.size()][9];
for (int i = 0; i < regionalValues.size(); i++) {
    regionalData[i] = regionalValues.get(i);
}
RegionalObservationGraph.showGraph(times, regionalData);
exportRegionalResults(
	    "phantomblock_regional_results.csv",
	    times,
	    regionalData
	);
            System.out.println("--------------------------------");
            System.out.println("Rows successfully read: " + rowsRead);
System.out.println("\n===== VIDEO CONGESTION ANALYSIS =====");
if (rowsRead > 0) {
    System.out.println("Peak total contours: " + peakTotal);
    System.out.println("Peak frame: " + peakFrame);
    System.out.printf("Peak time: %.3f seconds%n", peakTime);
    System.out.println("\nRegional hotspot ranking:");
    for (int i = 0; i < 9; i++) {
        System.out.println(
            "Area " + (i + 1)
            + ": " + regionalTotals[i]
            + " accumulated contours"
        );
    }
    System.out.println("\nTemporal changes:");
    System.out.println("Increases: " + increases);
    System.out.println("Decreases: " + decreases);
    System.out.println(
        "Unchanged observations: "
        + (rowsRead - 1 - increases - decreases)
    );
}
            if (rowsRead > 0) {
                System.out.printf(
                    "Average contours per observation: %.2f%n",
                    totalContours / rowsRead
                );
            }
        }
        catch (IOException e) {
            System.out.println(
                "Could not read CSV: " + e.getMessage()
            );
            System.out.println(
                "Check that the CSV is in the Java working directory."
            );
        }
        
    }
    public static void exportRegionalResults(
            String fileName,
            double[] times,
            int[][] regions) {
        try (PrintWriter writer =
                 new PrintWriter(new FileWriter(fileName))) {
            writer.print("TimeSeconds");
            for (int i = 1; i <= 9; i++) {
                writer.print(",Area" + i);
            }
            writer.println();
            for (int row = 0; row < times.length; row++) {
                writer.print(times[row]);

                for (int region = 0; region < 9; region++) {
                    writer.print("," + regions[row][region]);
                }
                writer.println();
            }
            System.out.println(
                "Regional results exported to: " + fileName
            );
        } catch (IOException e) {
            System.out.println(
                "Could not export regional results: "
                + e.getMessage()
            );
        }
    }

}

