package circle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class Dot extends JComponent 
{
    protected Vector2D _position, _velocity, _acceleration;
    protected Color _color;
    
    public Dot()
    {
        super();
        _position = new Vector2D(this.getX(), this.getY());
        _velocity = new Vector2D();
        _acceleration = new Vector2D();
    }
    
    public Dot(Vector2D velocity, Vector2D acceleration)
    {
        super();
        _velocity = velocity;
        _acceleration = acceleration;
    }
    
    public void setPosition(Vector2D v)
    {
        _position = v;
        this.setLocation((int) v.getX(), (int) v.getY());
    }
    
    public void setPosition(double x, double y)
    {
        _position.setX(x);
        _position.setY(y);
        this.setLocation((int) x, (int) y);
    }
    
    public Vector2D getPosition()
    {
        return _position;
    }
    
    public void setVelocity(double x, double y)
    {
        _velocity.setX(x);
        _velocity.setY(y);
    }
    
    public void setVelocity(Vector2D v)
    {
        _velocity = v;
    }
    
    public void setAcceleration(double x, double y)
    {
        _acceleration.setX(x);
        _acceleration.setY(y);
    }
    
    public void setAcceleration(Vector2D v)
    {
        _acceleration = v;
    }
    
    public void setColor(Color c)
    {
        _color = c;
    }
    
    public void updatePhysics(double timeStep)
    {
        _velocity.add(_acceleration.scalarProduct(timeStep));
        Vector2D velocity_prime = new Vector2D(_velocity).scalarProduct(timeStep);
        
        // Physics:
        // x_1 = x_0 + (v_x)*t + 1/2(a_x)*t^2
        _position.setX(_position.getX() + velocity_prime.getX() + (0.5 * _acceleration.getX() * timeStep * timeStep));
        _position.setY(_position.getY() + velocity_prime.getY() + (0.5 * _acceleration.getY() * timeStep * timeStep));
        
        this.setLocation((int) _position.getX(), (int) _position.getY());
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(_color);
        g2d.fillOval(0, 0, getWidth(), getHeight());
    }
    
}
