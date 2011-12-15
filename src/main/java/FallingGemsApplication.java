
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class FallingGemsApplication extends JFrame {

    private FallingGemsPanel panel = new FallingGemsPanel();
    
    public FallingGemsApplication() {
        super("Falling Gems");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
        
        Image hatter = Toolkit.getDefaultToolkit().getImage(FallingGemsApplication.class.getResource("GemBack.gif")); 
        Image vesztettkep = Toolkit.getDefaultToolkit().getImage(FallingGemsApplication.class.getResource("YouLoose.gif")); 
        Image[] kokepek = new Image[7];
        for (int i = 0; i < 7; i++) {
            kokepek[i] = Toolkit.getDefaultToolkit().getImage(FallingGemsApplication.class.getResource(i + ".gif"));
        }
        panel.setHatter(hatter);
        panel.setVesztettkep(vesztettkep);
        panel.setKokepek(kokepek);        
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        
        pack();        
    }
    
    public static void main(String[] args) {
        FallingGemsApplication app = new FallingGemsApplication();        
        app.setVisible(true);
    }
}
