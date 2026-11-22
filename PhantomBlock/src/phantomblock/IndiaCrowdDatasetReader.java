package phantomblock;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;

public class IndiaCrowdDatasetReader {
	public static void readDataset(String filePath) {
		 try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
		      String line;
		      int dataCount = 0;
		      double totalCrowd= 0;
		      int validCount =  0;
		      double minCrowd = Double.MAX_VALUE;
		      double maxCrowd = Double.MIN_VALUE;
		      LocalDate earliestDate = null;
		      LocalDate latestDate = null;
		      DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		      Map<String, Double> locationTotals = new HashMap<>();
		      Map<String, Integer> locationCounts = new HashMap<>();
		      br.readLine();
		      while ((line = br.readLine()) != null) {
		    	  dataCount++;
		          String[] values = line.split(",", -1);
		          if(values.length > 0) {
		        	  try {
		        		  LocalDate date = LocalDate.parse(values[0].trim(), formatter);
		        		  if(earliestDate == null || date.isBefore(earliestDate)) {
		        			  earliestDate = date;
		        		  }
		        		  if(latestDate == null || date.isAfter(latestDate)) {
		        			  latestDate = date;
		        		  }
		        	  } catch(java.time.format.DateTimeParseException e) {
		        		  System.out.println("Skipping invalid date: [" + values[0] + "]" );
		        		  System.out.println(e.getMessage());
		        	  }
		        	
		          }
		          if(values.length > 3) {
		        	  try {
		        		  double crowdCount = Double.parseDouble(values[4]);
		        		  totalCrowd += crowdCount;
		        		  validCount++;
		        		  String place = values[2];
		        		  locationTotals.put(place, locationTotals.getOrDefault(place, 0.0) + crowdCount);
		        		  locationCounts.put(place, locationCounts.getOrDefault(place, 0) + 1); 
		        		  if(crowdCount < minCrowd) {
				    		  minCrowd = crowdCount;
				    	  }
				    	  if(crowdCount > maxCrowd) {
				    		  maxCrowd = crowdCount;
				    	  }
		        	  }catch(NumberFormatException e){
		        		  System.out.println("Skipping invalid crowd count: +  values[4]");
		        		  
		        	  }
		        	  System.out.println("Place: " + values[2] + " |Crowd count: " + values[4]);
		          }
		        }
		      System.out.println("Total data records read: " + dataCount);
		      if(validCount > 0) {
		    	  double averageCrowd =  totalCrowd / validCount;
		    	  System.out.println("Valid records: " + validCount);
		    	  System.out.println("Average crowd count: " + averageCrowd);
		    	  System.out.println("Minimum crowd count: " + minCrowd);
		    	  System.out.println("Maximum crowd count: " + maxCrowd);
		    	  double range = maxCrowd - minCrowd;
		    	  System.out.println("Maximum count range: " +  range);
		    	  System.out.println("\n----LOCATION WISE AVERAGES-----");
		    	  for(String place: new TreeSet<>(locationTotals.keySet())) {
		    		  double total = locationTotals.get(place);
		    		  int count = locationCounts.get(place);
		    		  double average = total / count;
		    		  System.out.println(place + " | Records: " + count + "|Average: " + average);
		    	  }
		    	  exportLocationSummary("india_location_summary.csv",locationTotals,locationCounts);
		    		System.out.println("Location summary exported!");


		      }
		    } catch (IOException e) {
		        System.out.println("Error reading India crowd dataset.");
		        e.printStackTrace();
		    }
		 
		}
	
public static void main(String[] args) {
	String filePath = "crowd_flow_india_2_years_new.csv";
	readDataset(filePath);
}

public static void exportLocationSummary(
        String filePath,Map<String, Double> locationTotals,Map<String, Integer> locationCounts) {
    try (FileWriter writer = new FileWriter(filePath)) {
        writer.write("Place,Record_Count,Average_Crowd_Count\n");
        for (String place : new TreeSet<>(locationTotals.keySet())) {
            int count = locationCounts.get(place);
            double average = locationTotals.get(place) / count;
            writer.write(place + "," + count + "," + average + "\n");
        }
        

    } catch (IOException e) {
        System.out.println("Error exporting location summary.");
        e.printStackTrace();
    }
}


}

