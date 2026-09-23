import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InputBox extends JPanel {
    private static final Color PANEL_BG = new Color(18, 18, 18);
    private static final Color HEADER_BG = new Color(38, 38, 38);
    private static final Color TITLE_COLOR = new Color(255, 110, 60);
    private static final Color FIELD_BG = new Color(8, 8, 8);
    private static final int ARC = 16;

    private final JLabel title = new JLabel("Тренировка капчи");
    private final JTextField field = new JTextField(14);

    public InputBox(Runnable onAccept, Runnable onCancel) {
        setOpaque(false);
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 14, 14, 14));

        title.setForeground(TITLE_COLOR);
        title.setFont(new Font("Arial", Font.BOLD, 12));
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        JLabel message = new JLabel("<html>Введите <font color='#f5d142'>5</font> символов, которые<br>"
                + "появились у Вас <font color='#6ee26e'>на</font> экране.</html>");
        message.setForeground(Color.WHITE);
        message.setFont(new Font("Arial", Font.BOLD, 12));

        field.setBackground(FIELD_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.BOLD, 18));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        field.addKeyListener(new KeyAdapter() {
            private boolean enterPressed = false;

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && enterPressed) {
                    enterPressed = false;
                    onAccept.run();
                }
            }
        });

        JPanel fieldRow = new JPanel(new BorderLayout(6, 0));
        fieldRow.setOpaque(false);
        fieldRow.add(field, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(message, BorderLayout.NORTH);
        center.add(fieldRow, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 14, 0));
        buttons.setOpaque(false);
        buttons.add(new RoundButton("Принять", onAccept));
        buttons.add(new RoundButton("Отмена", onCancel));
        add(buttons, BorderLayout.SOUTH);
    }

    public void open() {
        field.setText("");
        setVisible(true);
        field.requestFocusInWindow();
    }

    public void close() {
        field.setText("");
        setVisible(false);
    }

    public String getText() {
        return field.getText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(PANEL_BG);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);

        int headerH = title.getY() + title.getHeight();
        g2.setColor(HEADER_BG);
        g2.fillRoundRect(0, 0, getWidth(), headerH, ARC, ARC);
        g2.fillRect(0, headerH - ARC, getWidth(), ARC);

        g2.dispose();
    }

    private static class RoundButton extends JButton {
        RoundButton(String text, Runnable action) {
            super(text);
            setFocusable(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
            addActionListener(e -> action.run());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color fill;
            if (getModel().isPressed()) {
                fill = new Color(70, 70, 70);
            } else if (getModel().isRollover()) {
                fill = new Color(48, 48, 48);
            } else {
                fill = new Color(26, 26, 26);
            }
            g2.setColor(fill);
            g2.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, ARC, ARC);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, ARC, ARC);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}