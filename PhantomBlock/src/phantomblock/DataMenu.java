package phantomblock;
import javax.swing.*; // to design graphical panels
import javax.swing.border.EmptyBorder; //type of designs
import java.awt.*; //abstract window toolkit to create gui.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class DataMenu {
    static class PhantomPanel extends JPanel { //to draw our own background
    	private static final long serialVersionUID = 1L; //for serialization of data 
        public PhantomPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create(); //colour, text ... other stuff
            g2.setColor(new Color(22, 18, 40));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(45, 38, 65));
            for (int x = 0; x < getWidth(); x += 35) {
                g2.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y < getHeight(); y += 35) {
                g2.drawLine(0, y, getWidth(), y); //height of the box
            }
            g2.setColor(new Color(130, 110, 170));
            int[][] stars = {
                {40, 45}, {100, 90}, {180, 35},
                {270, 70}, {350, 40}, {450, 100},
                {500, 50}, {70, 230}, {420, 250},
                {250, 290}, {480, 190}
            };
            for (int[] star : stars) {
                g2.fillOval(star[0], star[1], 3, 3); //for background
            }
            g2.dispose();
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("PhantomBlock");   //for frames
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        PhantomPanel mainPanel = new PhantomPanel();
        mainPanel.setBorder(
            new EmptyBorder(45, 60, 45, 60)
        );
        JLabel title = new JLabel("✦ PHANTOMBLOCK ✦");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(new Color(230, 220, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle =
            new JLabel("Crowd Analysis & Visualization");
        subtitle.setFont(
            new Font("SansSerif", Font.PLAIN, 15)
        );
        subtitle.setForeground(
            new Color(175, 165, 195)
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(subtitle);
        mainPanel.add(
            Box.createVerticalStrut(35)
        );
        JLabel label =
            new JLabel("Choose results to visualize");
        label.setFont(
            new Font("SansSerif", Font.BOLD, 16)
        );
        label.setForeground(
            new Color(215, 205, 235)
        );
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(label);
        mainPanel.add(
            Box.createVerticalStrut(12)
        );
        String[] options = {
            "👻 PhantomBlock Results",
            "📊 Dataset Results",
            "🇮🇳 India Crowd Dataset",
            "⚖️ Comparision"
        };
        JComboBox<String> choiceBox =
            new JComboBox<>(options);
        choiceBox.setFont(
            new Font("SansSerif", Font.PLAIN, 15)
        );
        choiceBox.setBackground(
            new Color(48, 38, 70)
        );
        choiceBox.setForeground(
            new Color(235, 230, 245)
        );
        choiceBox.setBorder(
            BorderFactory.createLineBorder(
                new Color(130, 105, 170), 2
            )
        );
        choiceBox.setPreferredSize(
            new Dimension(330, 45)
        );
        choiceBox.setMaximumSize(
            new Dimension(330, 45)
        );
        choiceBox.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );
        mainPanel.add(choiceBox);
        mainPanel.add(
            Box.createVerticalStrut(28)
        );
        JButton displayButton =
            new JButton("DISPLAY");

        displayButton.setFont(
            new Font("SansSerif", Font.BOLD, 15)
        );
        displayButton.setForeground(Color.WHITE);
        displayButton.setBackground(
            new Color(105, 75, 145)
        );
        displayButton.setFocusPainted(false);
        displayButton.setBorder(
            BorderFactory.createLineBorder(
                new Color(155, 125, 195), 2
            )
        );
        displayButton.setPreferredSize(
            new Dimension(140, 45)
        );
        displayButton.setMaximumSize(
            new Dimension(140, 45)
        );
        displayButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );
        mainPanel.add(displayButton);
        displayButton.addActionListener(e -> {
            String choice =
                (String) choiceBox.getSelectedItem();
            if (choice.equals(
                    "👻 PhantomBlock Results")) {
                System.out.println(
                    "Loading PhantomBlock simulation data..."
                );
                PhantomBlock.main(
                    new String[0]
                );
            }
            else if (choice.equals(
                    "📊 Dataset Results")) {
                System.out.println(
                    "Loading dataset..."
                );
                DatasetReader.main(new String[0] );
            }
            else if (choice.equals("🇮🇳 India Crowd Dataset")) {
            	System.out.println("Opening India crowd graph...");
                Graph.showIndiaCrowdGraph("india_location_summary.csv");
            }
            else if(choice.equals("⚖️ Comparision")){
            	System.out.println("Loading comparision graph ....");
            	double[] phantomDurations =
            	        readPhantomDurations("phantomblock_durations.csv");

            	for (int i = 0; i < phantomDurations.length; i++) {
            	    phantomDurations[i] /= 60.0;
            	}
            	double[] datasetDurations =
            	        DatasetReader.getDatasetDurations(
            	            "PhantomBlock_ICMTD_Day1-3_5min.csv"
            	        );
            	System.out.println("PhantomBlock: "
            	        + java.util.Arrays.toString(phantomDurations));

            	System.out.println("Dataset: "
            	        + java.util.Arrays.toString(datasetDurations));
            	System.out.println("PhantomBlock: "
            	        + java.util.Arrays.toString(phantomDurations));

            	System.out.println("ICMTD: "
            	        + java.util.Arrays.toString(datasetDurations));

            	Graph.showComparisonGraph(phantomDurations, datasetDurations);
            	Graph.showComparisonGraph(phantomDurations, datasetDurations);
            	}
            
        });
        frame.add(mainPanel);
        frame.setVisible(true);
    }

public static double[] readPhantomDurations(String filePath) {
    List<Double> durations = new ArrayList<>();

    try (BufferedReader reader =
             new BufferedReader(new FileReader(filePath))) {

        reader.readLine(); // Skip CSV header

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");

            if (values.length >= 2) {
                durations.add(Double.parseDouble(values[1].trim()));
            }
        }

    } catch (IOException | NumberFormatException e) {
        System.out.println("Error reading PhantomBlock durations.");
        e.printStackTrace();
    }

    double[] result = new double[durations.size()];

    for (int i = 0; i < durations.size(); i++) {
        result[i] = durations.get(i);
    }

    return result;
}

}