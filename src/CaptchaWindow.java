import java.awt.*;
import javax.swing.*;

public class CaptchaWindow extends JDialog {



    CaptchaWindow(Window owner, String captcha) {
        super(owner, "Captcha");
            setSize(400, 250);
            setLocationRelativeTo(owner);
    }
}