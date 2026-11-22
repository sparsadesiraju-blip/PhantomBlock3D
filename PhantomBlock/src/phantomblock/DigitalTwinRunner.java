
package phantomblock;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DigitalTwinRunner {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\D.spandana\\phantomblock_video_observations.csv";
        Building building = new Building(7, 2);
        DigitalTwinEngine engine = new DigitalTwinEngine(building);
        System.out.println("Engine mapping areas: "+ engine.getSpatialMapping().getMappedAreaCount());
        SpatialMapping mapping = new SpatialMapping(building);
        System.out.println("Mapped video areas: "+ mapping.getMappedAreaCount() + "/9");
        System.out.println("Connected building floors: "+ engine.getBuildingFloorCount() );
        
        try (BufferedReader reader =
                 new BufferedReader(new FileReader(fileName))) {

            String line = reader.readLine(); 

            if (line == null) {
                System.out.println("CSV file is empty.");
                return;
            }
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.split(",", -1);

                if (values.length != 12) {
                    System.out.println("Skipping invalid CSV row.");
                    continue;
                }
                try {
                    double timeSeconds =
                        Double.parseDouble(values[1]);

                    int[] regionalCounts = new int[9];

                    for (int i = 0; i < 9; i++) {
                        regionalCounts[i] =
                            Integer.parseInt(values[i + 2]);
                    }
                    int totalContours =
                        Integer.parseInt(values[11]);

                    DigitalTwinState state =
                        new DigitalTwinState(
                            timeSeconds,
                            regionalCounts,
                            totalContours
                        );

                    engine.updateState(state);

                } catch (NumberFormatException e) {
                    System.out.println(
                        "Skipping row with invalid numbers."
                    );
                }
            }

            engine.printCurrentState();

        } catch (IOException e) {
            System.out.println(
                "Could not read CSV: " + e.getMessage()
            );
        }
      
    }
    


}

