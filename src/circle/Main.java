package circle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Main extends JPanel
{
    protected final Dot _dot;
    protected final Dot _centerDot;
    protected final JLabel _fpsLabel;
    protected final JLabel _posLabel, _velLabel;
    protected final JTextField _initialX, _initialY;
    protected final JTextField _initialXpos, _initialYpos;
    protected final JButton _resetSim, _resetPool;
    protected double _centerX, _centerY;
    protected static final int _inYpos = 20;
    protected static final double _gravitation = 0.15;
    protected static final long _threshold = 100;
    protected static final Vector2D _initialVel = new Vector2D(200, 0);
    protected static final Dimension _winSize = new Dimension(800, 800);
    
    // Dots pool
    protected ArrayList<Dot> _dotsPool;
    protected static final int _poolDots = 10;
    protected static Random _rand;
    
    public Main()
    {       
        setLayout(null);
        setLocation(0, 0);
        setSize(_winSize);
        setBackground(Color.lightGray);
        
        _centerX = this.getWidth() / 2;
        _centerY = this.getHeight() / 2;
        
        _dot = new Dot();
        _dot.setSize(15, 15);
        _dot.setColor(Color.red);
        _dot.setVisible(true);
        _dot.setVelocity(_initialVel);
        _dot.setPosition(this.getWidth() / 2, _inYpos);
        
        _centerDot = new Dot();
        _centerDot.setPosition(this.getWidth() / 2, this.getHeight() / 2);
        _centerDot.setColor(Color.blue);
        _centerDot.setSize(10, 10);
        _centerDot.setVisible(true);
        _centerDot.addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseMoved(MouseEvent e)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void mouseDragged(MouseEvent e)
            {
                Point p = e.getPoint();
                int x = _centerDot.getX() + p.x;
                int y = _centerDot.getY() + p.y;
                _centerDot.setLocation(_centerDot.getX() + p.x, _centerDot.getY() + p.y);
                _centerX = x;
                _centerY = y;
            }
        });
        
        // Generate pooled dots
        _dotsPool = new ArrayList<Dot>(_poolDots);
        for(int i = 0; i < _poolDots; i++)
        {
            final Dot d = new Dot();
            d.setVisible(true);
            _dotsPool.add(d);
            add(d);
        }
        
        // Randomize pool
        randomizeDotPool();
        
        // GUI components
        _fpsLabel = new JLabel();
        _fpsLabel.setLocation(5, 5);
        _fpsLabel.setSize(100, 10);
        
        _posLabel = new JLabel("Position (X, Y)");
        _posLabel.setLocation(100, 40);
        _posLabel.setSize(100, 25);
        
        _velLabel = new JLabel("Velocity (Xi, Yi)");
        _velLabel.setLocation(260, 40);
        _velLabel.setSize(100, 25);

        _initialXpos = new JTextField(Double.toString(this.getWidth() / 2));
        _initialXpos.setLocation(100, 10);
        _initialXpos.setSize(75, 30);
        
        _initialYpos = new JTextField(Double.toString(_inYpos));
        _initialYpos.setLocation(180, 10);
        _initialYpos.setSize(75, 30);
        
        _initialX = new JTextField(Double.toString(_initialVel.getX()));
        _initialX.setLocation(260, 10);
        _initialX.setSize(75, 30);
        
        _initialY = new JTextField(Double.toString(_initialVel.getY()));
        _initialY.setLocation(340, 10);
        _initialY.setSize(75, 30);
        
        _resetSim = new JButton("Reset");
        _resetSim.setLocation(420, 10);
        _resetSim.setSize(75, 30);
        _resetSim.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    _dot.setVelocity(Double.parseDouble(_initialX.getText()), Double.parseDouble(_initialY.getText()));
                    _dot.setPosition(Double.parseDouble(_initialXpos.getText()), Double.parseDouble(_initialYpos.getText()));
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        
        _resetPool = new JButton("Reset Pool");
        _resetPool.setLocation(500, 10);
        _resetPool.setSize(100, 30);
        _resetPool.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                randomizeDotPool();
            }
        });
        
        add(_dot);
        add(_centerDot);
        add(_fpsLabel);
        add(_initialX);
        add(_initialY);
        add(_initialXpos);
        add(_initialYpos);
        add(_posLabel);
        add(_velLabel);
        add(_resetSim);
        add(_resetPool);
        
        // Physics
        new PhysicsThread();
    }
    
    private void randomizeDotPool()
    {
        if (_rand == null) {
            _rand = new Random(System.currentTimeMillis());
        }
        
        for (Dot d : _dotsPool) {
            d.setPosition(_rand.nextInt(this.getWidth()), _rand.nextInt(this.getHeight()));
            int circleSize = 5 + _rand.nextInt(15);
            d.setSize(circleSize, circleSize);
            d.setColor(new Color(_rand.nextInt(256), _rand.nextInt(256), _rand.nextInt(256)));
            d.setVelocity((_rand.nextBoolean() ? 1 : -1) * _rand.nextInt(100), (_rand.nextBoolean() ? 1 : -1) * _rand.nextInt(100));
        }
    }
    
    public class PhysicsThread extends Thread
    {
        long _prevClock;
        long _frames, _frameClock;
        
        public PhysicsThread()
        {
            super();
            _prevClock = System.currentTimeMillis();
            _frameClock = System.currentTimeMillis();
            this.start();
        }
        
        @Override
        public void run()
        {
            while(true)
            {
                // Update and draw dot
                double timeStep = (double) (System.currentTimeMillis() - _prevClock) / _threshold;
                _dot.setAcceleration(getVectorToCenter(_dot).scalarProduct(_gravitation));
                _dot.updatePhysics(timeStep);
                _dot.repaint();
                
                // Update all dots in pool
                for (Dot d : _dotsPool)
                {
                    d.setAcceleration(getVectorToCenter(d).scalarProduct(_gravitation));
                    d.updatePhysics(timeStep);
                    d.repaint();
                }
                
                // Change prevclock to update timestep
                _prevClock = System.currentTimeMillis();
                
                // Update FPS
                // Book keeping
                long diff = System.currentTimeMillis() - _frameClock;
                if (diff >= 1000) {
                    _fpsLabel.setText("FPS: " + _frames);
                    _frames = 0;
                    _frameClock = System.currentTimeMillis();
                }
                _frames++;
                
                try {
                    Thread.sleep(10);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public Vector2D getVectorToCenter(Dot d)
    {
        Vector2D v = new Vector2D();
        v.setX(_centerX - d.getPosition().getX());
        v.setY(_centerY - d.getPosition().getY());
        
        return v;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run()
            {
                JFrame frame = new JFrame();
                Toolkit kit = Toolkit.getDefaultToolkit();
                Dimension screenSize = kit.getScreenSize();
                frame.setSize(_winSize);
                frame.setUndecorated(true);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setLocation(screenSize.width / 2 - _winSize.width / 2, screenSize.height / 2 - _winSize.height / 2);
                frame.setTitle("Circle Physics Experiment");
                frame.setLayout(null);
                frame.add(new Main());
                frame.setVisible(true);
            }
        });
    }

}
