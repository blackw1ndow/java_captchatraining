import java.awt.*;

public class DigitRenderer {
    private static final String[][] DIGITS = {
            { "###", "#.#", "#.#", "#.#", "#.#", "#.#", "###" }, // 0
            { ".#.", ".#.", ".#.", ".#.", ".#.", ".#.", ".#." }, // 1
            { "###", "..#", "..#", "###", "#..", "#..", "###" }, // 2
            { "###", "..#", "..#", "###", "..#", "..#", "###" }, // 3
            { "#.#", "#.#", "#.#", "###", "..#", "..#", "..#" }, // 4
            { "###", "#..", "#..", "###", "..#", "..#", "###" }, // 5
            { "###", "#..", "#..", "###", "#.#", "#.#", "###" }, // 6
            { "###", "..#", "..#", "..#", "..#", "..#", "..#" }, // 7
            { "###", "#.#", "#.#", "###", "#.#", "#.#", "###" }, // 8
            { "###", "#.#", "#.#", "###", "..#", "..#", "###" }, // 9
    };

    public void draw(Graphics g, String text, int x, int y, int w, int h,
                     Color bgColor, Color fgColor) {
        int border = Math.max(4, h / 20);
        g.setColor(fgColor);
        g.fillRect(x, y, w, h);
        g.setColor(bgColor);
        g.fillRect(x + border, y + border, w - 2 * border, h - 2 * border);

        if (text == null || text.isEmpty()) {
            return;
        }

        int pad = border + h / 20;
        int areaX = x + pad;
        int areaY = y + pad;
        int areaW = w - 2 * pad;
        int areaH = h - 2 * pad;

        int cellW = areaW / text.length();
        int gap = Math.max(2, cellW / 30);

        g.setColor(fgColor);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < '0' || c > '9') {
                continue;
            }
            String[] pattern = DIGITS[c - '0'];
            int digitX = areaX + i * cellW + gap / 2;
            drawDigit(g, pattern, digitX, areaY, cellW - gap, areaH);
        }
    }

    private void drawDigit(Graphics g, String[] pattern, int x, int y, int w, int h) {
        int rows = pattern.length;
        int cols = pattern[0].length();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (pattern[row].charAt(col) == '#') {
                    int left = x + col * w / cols;
                    int right = x + (col + 1) * w / cols;
                    int top = y + row * h / rows;
                    int bottom = y + (row + 1) * h / rows;
                    g.fillRect(left, top, right - left, bottom - top);
                }
            }
        }
    }
}