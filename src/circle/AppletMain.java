package circle;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

public class AppletMain extends JApplet
{
    @Override
    public void init()
    {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run()
            {
                setSize(800, 800);
                add(new Main());
                setVisible(true);
            }
        });
    }
}
