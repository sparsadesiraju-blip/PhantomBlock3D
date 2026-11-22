package phantomblock;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DatasetReader {
	public static void main(String[] args) {
		double[][] graphData = new double[1000][2];
		int row = 0;
		String fileName = "PhantomBlock_ICMTD_Day1-3_5min.csv";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			reader.readLine();
			while((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				int day = Integer.parseInt(data[0]);
				double time = Double.parseDouble(data[1]);
				double floor1 = Double.parseDouble(data[2]);
				double floor2 = Double.parseDouble(data[3]);
				graphData[row][0]= floor1;
				graphData[row][1]= floor2;
				row++;
				
				System.out.println("Day " + day + "| Time: " + time + "min" + " | Floor 1: " + floor1 + " people" + " | Floor 2: " + floor2 + " people");
				
			}
				
			reader.close();
			double[][] finalData = new double[row][2];
			for(int i = 0; i < row ; i++) {
				finalData[i][0] = graphData[i][0];
				finalData[i][1] = graphData[i][1];
				}
			double floor1Duration =
			        calculateCongestionDuration(finalData, row, 0, 10);

			double floor2Duration =
			        calculateCongestionDuration(finalData, row, 1, 10);

			System.out.println("Floor 1 total congestion: "
			        + floor1Duration + " minutes");

			System.out.println("Floor 2 total congestion: "
			        + floor2Duration + " minutes");
			Graph.showGraph(finalData, row, 2);
		}
		
		catch(IOException e) {
			System.out.println("Error reading dataset.");
			e.printStackTrace();
		}
	}
	public static double calculateCongestionDuration(
	        double[][] data, int rows, int floorIndex, double limit) {
	      int start = -1;
	      double totalDurationMinutes =  0.0;
	      for (int i = 0; i < rows; i++) {
	        if (data[i][floorIndex] > limit) {
	            if (start == -1) {
	                start = i;
	            }
	            }
	         else {
	                if (start != -1)
	                {
	                    int durationRows = i - start;
	                    double durationMinutes = durationRows * 5.0;
	                    totalDurationMinutes += durationMinutes;
	                    System.out.println(
	                        "Floor " + (floorIndex + 1) +
	                        " congested from row " + start +
	                        " to " + (i - 1) +
	                        " | Duration: " + durationMinutes + " minutes");
	                    start = -1;
	                }
	         }
	     }
	     if (start != -1) {

	         int durationRows = rows - start;
	         double durationMinutes = durationRows * 5.0;
	         totalDurationMinutes += durationMinutes;
	         System.out.println(
	             "Floor " + (floorIndex + 1) +
	             " congested until end of dataset" +
	             " | Duration: " +
	             durationMinutes + " minutes"
	         );
	     }
	     return totalDurationMinutes;
	 }
	public static double[] getDatasetDurations(String fileName) {
	    double[][] graphData = new double[1000][2];
	    int row = 0;

	    try (BufferedReader reader =
	             new BufferedReader(new FileReader(fileName))) {

	        reader.readLine(); // Skip header

	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] data = line.split(",");

	            if (data.length < 4) {
	                continue;
	            }

	            graphData[row][0] = Double.parseDouble(data[2].trim());
	            graphData[row][1] = Double.parseDouble(data[3].trim());
	            row++;
	        }

	    } catch (IOException | NumberFormatException e) {
	        System.out.println("Error reading dataset durations.");
	        e.printStackTrace();
	        return new double[0];
	    }

	    double[][] finalData = new double[row][2];

	    for (int i = 0; i < row; i++) {
	        finalData[i][0] = graphData[i][0];
	        finalData[i][1] = graphData[i][1];
	    }

	    return new double[] {
	        calculateCongestionDuration(finalData, row, 0, 10),
	        calculateCongestionDuration(finalData, row, 1, 10)
	    };
	}
	

}
