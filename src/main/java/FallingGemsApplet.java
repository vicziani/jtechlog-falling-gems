
import java.applet.Applet;
import java.awt.Image;

public class FallingGemsApplet extends Applet {

    private FallingGemsPanel panel = new FallingGemsPanel();

    @Override
    public void init() {
        Image hatter = getImage(getClass().getResource("GemBack.gif"));
        Image vesztettkep = getImage(getClass().getResource("YouLoose.gif"));
        Image[] kokepek = new Image[7];
        for (int i = 0; i < 7; i++) {
            kokepek[i] = getImage(getClass().getResource(i + ".gif"));
        }
        panel.setHatter(hatter);
        panel.setVesztettkep(vesztettkep);
        panel.setKokepek(kokepek);
        
        add(panel);
    }

    @Override
    public String getAppletInfo() {
        return "Falling Gems version 1.0-SNAPSHOT - logikai játék\nViczián István\nhttp://jtechlog.blogspot.com";
    }
}
