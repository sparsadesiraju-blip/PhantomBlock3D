
package phantomblock;
import javax.swing.*;
import java.awt.*;
public class RegionalObservationGraph extends JPanel {
    private final double[] times;
    private final int[][] regions;
    private final Color[] regionColors = {
        Color.RED, Color.BLUE, new Color(0, 140, 0),
        Color.ORANGE, Color.MAGENTA, Color.CYAN,
        Color.PINK, Color.GRAY, new Color(120, 70, 180)
    };
    public RegionalObservationGraph(double[] times, int[][] regions) {
        this.times = times;
        this.regions = regions;

        setPreferredSize(new Dimension(950, 600));
        setBackground(Color.WHITE);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int left = 75;
        int right = getWidth() - 180;
        int top = 60;
        int bottom = getHeight() - 80;
        int width = right - left;
        int height = bottom - top;
        double maxTime = 1;
        int maxCount = 1;
        for (double time : times) {
            maxTime = Math.max(maxTime, time);
        }
        for (int[] row : regions) {
            for (int count : row) {
                maxCount = Math.max(maxCount, count);
            }
        }
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(Color.BLACK);
        g2.drawString("PhantomBlock - Regional Contour Activity", left, 30);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (int i = 0; i <= 5; i++) {
            int y = bottom - i * height / 5;
            int value = i * maxCount / 5;
            g2.setColor(new Color(225, 225, 225));
            g2.drawLine(left, y, right, y);

            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(value), left - 35, y + 5);
        }
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (int i = 0; i <= 5; i++) {
            double tickTime = maxTime * i / 5.0;
            int x = left + i * width / 5;
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(x, top, x, bottom);
            g2.setColor(Color.BLACK);
            String label = String.format("%.1f", tickTime);
            g2.drawString(label, x - 12, bottom + 20);
        }
        g2.drawLine(left, top, left, bottom);
        g2.drawLine(left, bottom, right, bottom);
        g2.drawString("Contours per region", 10, top - 15);
        g2.drawString("Time (seconds)", right - 80, bottom + 40);
        if (times.length < 2 || regions.length != times.length) {
            g2.drawString("Insufficient matching observations.", left, top + 25);
            g2.dispose();
            return;
        }
        for (int region = 0; region < 9; region++) {
            g2.setColor(regionColors[region]);
            g2.setStroke(new BasicStroke(2f));
            int previousX = 0;
            int previousY = 0;
            for (int i = 0; i < times.length; i++) {
                if (regions[i].length < 9) {
                    continue;
                }
                int x = left + (int) (times[i] / maxTime * width);
                int y = bottom - (int) (
                    (double) regions[i][region] / maxCount * height
                );
                if (i > 0 && regions[i - 1].length >= 9) {
                    g2.drawLine(previousX, previousY, x, y);
                }
                g2.fillOval(x - 3, y - 3, 6, 6);
                previousX = x;
                previousY = y;
            }
            int legendY = top + region * 25;
            g2.fillRect(right + 15, legendY - 8, 12, 12);
            g2.drawString("Area " + (region + 1), right + 34, legendY + 3);
        }
        g2.dispose();
    }
    public static void showGraph(double[] times, int[][] regions) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PhantomBlock - Regional Analysis");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new RegionalObservationGraph(times, regions));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

