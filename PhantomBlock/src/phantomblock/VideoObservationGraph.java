
package phantomblock;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class VideoObservationGraph extends JPanel {
    private final double[] times;
    private final int[] totals;
    List<Double> timeValues = new ArrayList<>();
    List<Integer> totalValues = new ArrayList<>();
    public VideoObservationGraph(double[] times, int[] totals) {
        this.times = times;
        this.totals = totals;
        
        setPreferredSize(new Dimension(850, 520));
        setBackground(Color.WHITE);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int left = 80;
        int right = getWidth() - 40;
        int top = 50;
        int bottom = getHeight() - 80;
        int plotWidth = right - left;
        int plotHeight = bottom - top;
        int maxValue = 1;
        double maxTime = 1;
        for (int value : totals) {
            maxValue = Math.max(maxValue, value);
        }
        for (double time : times) {
            maxTime = Math.max(maxTime, time);
        }
        // Title
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("PhantomBlock - Video Contour Activity", left, 28);
        // Grid and Y-axis labels
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (int i = 0; i <= 5; i++) {
            int y = bottom - (i * plotHeight / 5);
            int value = i * maxValue / 5;
            g2.setColor(new Color(225, 225, 225));
            g2.drawLine(left, y, right, y);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(String.valueOf(value), left - 35, y + 5);
        }
        // Axes
        g2.setColor(Color.BLACK);
        g2.drawLine(left, top, left, bottom);
        g2.drawLine(left, bottom, right, bottom);
        g2.drawString("Total detected contours", 10, top - 10);
        g2.drawString("Time (seconds)", right - 90, bottom + 45);
        if (times.length < 2 || totals.length != times.length) {
            g2.drawString(
                "At least two matching observations are needed.",
                left + 20, top + 30
            );
            g2.dispose();
            return;
        }
        // Plot the time series
        g2.setColor(new Color(110, 70, 190));
        g2.setStroke(new BasicStroke(2.5f));
        int previousX = 0;
        int previousY = 0;
        for (int i = 0; i < times.length; i++) {
            int x = left + (int) (times[i] / maxTime * plotWidth);
            int y = bottom - (int) ((double) totals[i] / maxValue
                    * plotHeight);
            if (i > 0) {
                g2.drawLine(previousX, previousY, x, y);
            }
            g2.fillOval(x - 4, y - 4, 8, 8);
            previousX = x;
            previousY = y;
        }
        g2.dispose();
    }
    public static void showGraph(double[] times, int[] totals) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PhantomBlock Video Analysis");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new VideoObservationGraph(times, totals));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
        });
    }
}
