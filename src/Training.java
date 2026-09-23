import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;

public class Training extends JPanel {
    private final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private final Color capColor = new Color(26, 34, 45);
    private final Color bgColor = new Color(91, 124, 135);

    private final Captcha captcha = new Captcha();
    private final DigitRenderer renderer = new DigitRenderer();
    private boolean waitingForKey = false;
    private int bindKey = 78;
    private long openedAt;

    private final JCheckBox zeroBox = new JCheckBox("Zero on end", true);
    private final JCheckBox delayOpening = new JCheckBox("Delay on opening", false);
    private final InputBox inputBox = new InputBox(this::acceptCaptcha, this::skipCaptcha);
    private final JLabel capKeyText = new JLabel("Captcha key: " + KeyEvent.getKeyText(bindKey));
    private final ChatPanel chat = new ChatPanel();

    Training() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(screen.width / 2, screen.height / 2));
        setFocusable(true);
        setBackground(Color.darkGray);

        JButton changeCapButton = new JButton("Change button for opening");
        changeCapButton.setFocusable(false);
        zeroBox.setFocusable(false);
        delayOpening.setFocusable(false);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(true);
        topPanel.add(changeCapButton);
        topPanel.add(zeroBox);
        topPanel.add(delayOpening);
        topPanel.add(capKeyText);

        inputBox.setVisible(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(inputBox);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(chat, BorderLayout.WEST);

        changeCapButton.addActionListener(e -> {
            waitingForKey = true;
            requestFocusInWindow();
            chat.addMessage("Press button for captcha opening (esc - cancel).", ChatPanel.HINT);
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = Training.this.getWidth();
                int h = Training.this.getHeight();

                int captchaBottom = h / 3 + h / 8;
                int margin = h / 40;
                int boxH = inputBox.getPreferredSize().height;
                int bottomGap = Math.max(0, h - captchaBottom - margin - boxH - 10);
                bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, bottomGap, 0));

                int chatW = Math.max(0, (w - w / 4) / 2 - 10);
                chat.setPreferredSize(new Dimension(chatW, 0));

                revalidate();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ESCAPE) {
                    return;
                }
                if (waitingForKey) {
                    bindKey = code;
                    waitingForKey = false;
                    chat.addMessage("New button: " + KeyEvent.getKeyText(code), ChatPanel.INFO);
                    capKeyText.setText("Captcha key: " + KeyEvent.getKeyText(bindKey));
                } else if (code == bindKey) {
                    requestOpen();
                }
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (waitingForKey) {
                    waitingForKey = false;
                    chat.addMessage("New button canceled.", ChatPanel.MUTED);
                } else {
                    skipCaptcha();
                }
            }
        });
    }

    private void openCaptcha() {
        captcha.generate(zeroBox.isSelected());
        openedAt = System.currentTimeMillis();
        inputBox.open();
        repaint();
    }

    private void requestOpen() {
        if (delayOpening.isSelected()) {
            Timer timer = new Timer(50, e -> openCaptcha());
            timer.setRepeats(false);
            timer.start();
        } else {
            openCaptcha();
        }
    }

    private void acceptCaptcha() {
        if (captcha.getValue() == null) {
            return;
        }
        String input = inputBox.getText();
        if (captcha.check(input)) {
            double seconds = (System.currentTimeMillis() - openedAt) / 1000.0;
            chat.addMessage(String.format("+! time: %.2f s", seconds), ChatPanel.SUCCESS);
        } else {
            chat.addMessage("-! captcha: " + captcha.getValue() + ", input: " + input, ChatPanel.ERROR);
        }
        cancelCaptcha();
    }

    private void skipCaptcha() {
        cancelCaptcha();
    }

    private void cancelCaptcha() {
        captcha.clear();
        inputBox.close();
        requestFocusInWindow();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        String value = captcha.getValue();
        if (value != null) {
            int boxW = getWidth() / 4;
            int boxH = getHeight() / 8;
            int boxX = (getWidth() - boxW) / 2;
            int boxY = getHeight() / 3;
            renderer.draw(g, value, boxX, boxY, boxW, boxH, bgColor, capColor);
        }
    }
}