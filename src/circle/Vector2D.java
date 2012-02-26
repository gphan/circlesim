package circle;

public class Vector2D
{
    protected double _x, _y;
    public Vector2D(double x, double y)
    {
        _x = x;
        _y = y;
    }
    
    public Vector2D(Vector2D v)
    {
        _x = v._x;
        _y = v._y;
    }
    
    public Vector2D()
    {
        _x = 0;
        _y = 0;
    }
    
    public void setX(double x)
    {
        _x = x;
    }
    
    public double getX()
    {
        return _x;
    }
    
    public void setY(double y)
    {
        _y = y;
    }
    
    public double getY()
    {
        return _y;
    }
    
    public double dotProduct(Vector2D o)
    {
        return (this._x * o._x) + (this._y * o._y);
    }
    
    public void add(Vector2D v)
    {
        this._x += v._x;
        this._y += v._y;
    }
    
    public Vector2D scalarProduct(double scalar)
    {
        Vector2D v = new Vector2D(this);
        v._x *= scalar;
        v._y *= scalar;
        return v;
    }
}
