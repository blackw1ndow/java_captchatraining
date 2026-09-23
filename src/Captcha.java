import java.util.Random;

public class Captcha {
    private static final int LENGTH = 5;
    private static final String SECRET_CODE = "12390";

    private final Random random = new Random();
    private String value;

    public void generate(boolean zeroOnEnd) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < LENGTH - 1; i++) {
            builder.append(random.nextInt(10));
        }
        if (zeroOnEnd) {
            builder.append(0);
        } else {
            builder.append(random.nextInt(10));
        }
        value = builder.toString();
    }

    public boolean check(String input) {
        return value != null && value.equals(input);
    }

    public String getValue() {
        return value;
    }

    public void clear() {
        value = null;
    }

    public boolean isSecretCode(String input) {
        return SECRET_CODE.equals(input);
    } // так называемая посхалко
}