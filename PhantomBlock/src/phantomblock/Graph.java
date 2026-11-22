package phantomblock;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Graph extends JPanel{
	private double[][] data;
	private int numberOfRounds;
	private int numberOfFloors;
	public Graph(double[][] data, int numberOfRounds, int numberOfFloors) {
		this.data = data;
		this.numberOfFloors = numberOfFloors;
		this.numberOfRounds = numberOfRounds;
		setPreferredSize(new Dimension(900, 600));
	}
	public static void showGraph(double[][] data, int numberOfRounds, int numberOfFloors) {
		JFrame frame = new JFrame("PhantomBlock - Occupancy Graph");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(new Graph(data,numberOfRounds, numberOfFloors));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		int left = 80;
		int bottom = 500;
		int top =- 60;
		int right = 850;
		double maxOccupancy = 0;
		for(int floor = 0; floor < numberOfFloors; floor++) {
			for(int round = 0 ; round < numberOfRounds; round++) {
				if(data[round][floor] > maxOccupancy) {
					maxOccupancy = data[round][floor];
				}
			}
		}
		maxOccupancy = Math.ceil(maxOccupancy + 0.5);
		if(maxOccupancy < 1) { 
			maxOccupancy = 1;
		}
		g2.setFont(new Font("Arial", Font.BOLD,20));
		g2.drawString("Congestion Duration: Simulation vs Dataset",150, 45);
		g2.drawLine(left,  top ,  left, bottom);
		g2.drawLine(left, bottom, right, bottom);
		g2.setFont(new Font("Arial", Font.PLAIN , 12));
		for(int i = 0; i <= 5; i++) {
			double value = maxOccupancy * 1 / 5.0;
			int y = bottom - (int)(value * (bottom - top  ) / maxOccupancy );
			g2.drawLine(left - 5, y, left, y);
			g2.drawString(String.format("%.1f%%", value), 35, y + 5);
		}
		for(int round = 0; round < numberOfRounds; round++) {
			int x = left + round *(right - left)/ Math.max(1,  numberOfRounds - 1);
			g2.drawString("R" + (round + 1), x- 8, bottom + 20);
		}
		for(int floor = 0; floor < numberOfFloors; floor++) {
			for(int round = 0 ; round < numberOfRounds - 1; round++) {
				double value1 = data[round][floor];
				double value2 = data[round + 1][floor];
				int x1 = left + round *(right - left)/ Math.max(1, numberOfRounds - 1);
				int x2 = left + (round + 1) *(right - left)/ Math.max(1, numberOfRounds - 1);
				int y1 = bottom - (int)(value1 * (bottom - top ) / maxOccupancy);
				int y2 = bottom - (int)(value2 * (bottom - top) / maxOccupancy);
				g2.drawLine(x1,  y1, x2, y2);
			}
		}
		int legendX = 700;
		int legendY = 80;
		for(int floor = 0; floor < numberOfFloors; floor++) {
			g2.drawString(("Floor ") + (floor + 1), legendX, legendY + floor*20);	
		}
		g2.drawString("Simulation Round", 400, 50);
		g2.drawString("Occupancy %", 10, 50);
	}
	public static void showCongestionDurationGraph(Floor[] floors, int numberOfFloors) {
	    JFrame frame = new JFrame("PhantomBlock - Congestion Duration");
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    JPanel panel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Graphics2D g2 = (Graphics2D) g;
	            int left = 100;
	            int bottom = 500;
	            int top = 80;
	            int right = 850;
	            g2.setFont(new Font("Arial", Font.BOLD, 20));
	            g2.drawString("Congestion Duration by Floor", 280, 40);
	            g2.drawLine(left, top, left, bottom);
	            g2.drawLine(left, bottom, right, bottom);
	            int maxDuration = 1;
	            for (int i = 0; i < numberOfFloors; i++) {
	                maxDuration = Math.max(maxDuration,floors[i].getYellowRounds());
	                maxDuration = Math.max(maxDuration,floors[i].getOrangeRounds());
	                maxDuration = Math.max(maxDuration,floors[i].getRedRounds());
	            }
	            g2.setFont(new Font("Arial", Font.PLAIN, 12));
	            for (int i = 0; i <= maxDuration; i++) {
	                int y = bottom -(int) (i * (bottom - top) / (double) maxDuration);
	                g2.drawLine(left - 5, y, left, y);
	                g2.drawString(String.valueOf(i),left - 30, y + 5);
	            }
	            g2.drawString("Rounds", 35, 70);
	            int groupWidth = (right - left) / numberOfFloors;
	            int barWidth = 15;
	            for (int floor = 0;floor < numberOfFloors; floor++) {
	                int x = left +floor * groupWidth +groupWidth / 2;
	                int yellow = floors[floor].getYellowRounds();
	                int orange = floors[floor].getOrangeRounds();
	                int red = floors[floor].getRedRounds();
	                int yellowHeight =(int) (yellow *(bottom - top) /(double) maxDuration);
	                g2.setColor(Color.YELLOW);
	                g2.fillRect(x - 25,bottom - yellowHeight,barWidth,yellowHeight);
	                int orangeHeight =
	                        (int) (orange *
	                        (bottom - top) /
	                        (double) maxDuration);
	                g2.setColor(Color.ORANGE);
	                g2.fillRect( x - 5,bottom - orangeHeight,barWidth,orangeHeight);
	                int redHeight =(int) (red *(bottom - top) /(double) maxDuration);
	                g2.setColor(Color.RED);
	                g2.fillRect(
	                        x + 15,
	                        bottom - redHeight,barWidth,redHeight);
	                g2.setColor(Color.BLACK);
	                }
	            g2.setColor(Color.YELLOW);
	            g2.fillRect(700, 90, 15, 15);
	            g2.setColor(Color.BLACK);
	            g2.drawString("Yellow", 720, 103);
	            g2.setColor(Color.ORANGE);
	            g2.fillRect(700, 120, 15, 15);
	            g2.setColor(Color.BLACK);
	            g2.drawString("Orange", 720, 133);
	            g2.setColor(Color.RED);
	            g2.fillRect(700, 150, 15, 15);
	            g2.setColor(Color.BLACK);
	            g2.drawString("Red", 720, 163);
	        }
	    };
	    panel.setPreferredSize(new Dimension(950, 600));
	    frame.add(panel);
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
	public static void showComparisonGraph(
	        double[] phantomDurations,double[] datasetDurations) {
		int floors = Math.max(phantomDurations.length,datasetDurations.length);
	    JFrame frame = new JFrame( "PhantomBlock vs Dataset - Congestion Duration" );
	    frame.setSize(800, 600);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    JPanel panel = new JPanel() {
	        private static final long serialVersionUID = 1L;
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Graphics2D g2 = (Graphics2D) g;
	            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            int width = getWidth();
	            int height = getHeight();
	            g2.setColor(new Color(22, 18, 40));
	            g2.fillRect(0, 0, width, height);
	            g2.setColor(Color.WHITE);
	            g2.setFont(new Font("Serif", Font.BOLD, 24) );
	            g2.drawString("Congestion Duration Comparison", 150, 45 );
	            int left = 80;
	            int bottom = height - 80;
	            int top = 90;
	            int right = width - 50;
	            int groupWidth = (right - left) / floors;
	            g2.setColor(new Color(180, 170, 200));
	            g2.drawLine(left, top, left, bottom );
	            g2.drawLine( left, bottom, right, bottom );
	            g2.drawString("Duration (minutes)", left + 10, bottom + 75);
	            double maxDuration = 0;
	            	for (int i = 0; i < phantomDurations.length; i++) {
	            	    maxDuration = Math.max(
	            	        maxDuration, phantomDurations[i]
	            	    );
	            	}

	            	for (int i = 0; i < datasetDurations.length; i++) {
	            	    maxDuration = Math.max(
	            	        maxDuration, datasetDurations[i]
	            	    );
	            	}
	            if (maxDuration == 0) {
	                maxDuration = 1;
	            }
	            for (int i = 0; i < floors; i++) {
	            int x = left + i * groupWidth + groupWidth / 4;
	            int barWidth = groupWidth / 5;
	            int phantomHeight = 0;
	            if (i < phantomDurations.length) {
	                phantomHeight = (int) (
	                    phantomDurations[i] / maxDuration
	                    * (bottom - top)
	                );
	            }
	            g2.setColor(new Color(130, 90, 180));
	            g2.fillRect(
	                x, bottom - phantomHeight,
	                barWidth, phantomHeight
	            );
	            if (i < phantomDurations.length) {g2.setColor(Color.WHITE);
	                g2.drawString(String.format("%.2f", phantomDurations[i]),x - 5,bottom - phantomHeight - 5
	                );
	            }
	            int datasetHeight = 0;
	            if (i < datasetDurations.length) {
	                datasetHeight = (int) (
	                    datasetDurations[i] / maxDuration
	                    * (bottom - top)
	                );
	            }
	            g2.setColor(new Color(80, 150, 190));
	            g2.fillRect(
	                x + barWidth + 5,
	                bottom - datasetHeight,
	                barWidth, datasetHeight );
	            if (i < datasetDurations.length) {
	                g2.setColor(Color.WHITE);
	                g2.drawString(
	                    String.format("%.2f", datasetDurations[i]),
	                    x + barWidth,
	                    bottom - datasetHeight - 5
	                );
	            }
	            g2.setColor(Color.WHITE);
	            if (i < phantomDurations.length) {
	                g2.drawString(
	                    "PB F" + (i + 1),
	                    x - 5,
	                    bottom + 20
	                );
	            }
	            if (i < datasetDurations.length) {
	                g2.drawString(
	                    "ICMTD " + (i + 1),
	                    x + barWidth,
	                    bottom + 40
	                );
	            }
	        }
	            g2.setColor(new Color(130, 90, 180));
		        g2.fillRect(width - 240, 55, 15, 15);
		        g2.setColor(Color.WHITE);
		        g2.drawString("PhantomBlock (min)", width - 215, 68);
		        g2.setColor(new Color(80, 150, 190));
		        g2.fillRect(width - 240, 80, 15, 15);
		        g2.setColor(Color.WHITE);
		        g2.drawString("ICMTD (min)", width - 215, 93);
	        }
	    };
	    frame.add(panel);
	    frame.setVisible(true);
	}
public static void showIndiaCrowdGraph(String filePath) {
    Map<String, Double> averages = new java.util.TreeMap<>();
    try (BufferedReader br =new BufferedReader(new FileReader(filePath))) {
        String line;
        br.readLine(); // Skip CSV header
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",", -1);
            if (values.length >= 3) {
                String place = values[0].trim();
                double average = Double.parseDouble(values[2].trim());
                averages.put(place, average);
            }
        }
    } catch (IOException | NumberFormatException e) {
        System.out.println("Could not read India summary CSV.");
        e.printStackTrace();
        return;
    }
    JFrame frame = new JFrame("India Crowd Counts by Location");
    frame.setSize(900, 600);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    JPanel panel = new JPanel() {
        private static final long serialVersionUID = 1L;
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int left = 220;
            int top = 60;
            int barHeight = 25;
            int gap = 35;
            int maxBarWidth = 550;
            double maxValue = 0;
            for (double value : averages.values()) {
                maxValue = Math.max(maxValue, value);
            }
            g2.drawString("Average Recorded Crowd Count", left, 30);
            int row = 0;
            for (Map.Entry<String, Double> entry : averages.entrySet()) {
                int y = top + row * gap;
                int barWidth = maxValue > 0
                    ? (int) (entry.getValue() / maxValue * maxBarWidth): 0;
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(entry.getKey(), 15, y + barHeight - 5);
                g2.setColor(new Color(80, 150, 190));
                g2.fillRect(left, y, barWidth, barHeight);
                g2.setColor(Color.BLACK);
                g2.drawString(String.format("%.2f", entry.getValue()),left + barWidth + 8,y + barHeight - 5);
                row++;
            }
        }
    };
    panel.setBackground(Color.WHITE);
    frame.add(panel);
    frame.setVisible(true);
}
}
