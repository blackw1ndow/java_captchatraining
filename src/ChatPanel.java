import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ChatPanel extends JPanel {
    public static final Color INFO = Color.WHITE;
    public static final Color HINT = new Color(245, 209, 66);
    public static final Color SUCCESS = new Color(110, 226, 110);
    public static final Color ERROR = new Color(255, 90, 90);
    public static final Color MUTED = new Color(170, 170, 170);

    private static final int MAX_MESSAGES = 10;

    private final List<Message> messages = new ArrayList<>();
    private final Font font = new Font("Arial", Font.BOLD, 16);

    public ChatPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(320, 0));
    }

    public void addMessage(String text, Color color) {
        messages.add(new Message(text, color));
        if (messages.size() > MAX_MESSAGES) {
            messages.remove(0);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        int x = 12;
        int y = 12 + fm.getAscent();

        for (Message m : messages) {
            g2.setColor(Color.BLACK);
            g2.drawString(m.text, x + 1, y + 1);
            g2.setColor(m.color);
            g2.drawString(m.text, x, y);
            y += fm.getHeight();
        }

        g2.dispose();
    }

    private static class Message {
        final String text;
        final Color color;

        Message(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }
}