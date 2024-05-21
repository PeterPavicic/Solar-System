import java.awt.*;

// This class represents vectors in a 3D vector space.
public class Vector3 {

    private double x;
    private double y;
    private double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    // Returns the sum of this vector and vector 'v'.
    public Vector3 plus(Vector3 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    // Returns the product of this vector and 'd'.
    public Vector3 times(double d) {
        return new Vector3(x * d, y * d, z * d);
    }

    // Returns the sum of this vector and -1*v.
    public Vector3 minus(Vector3 v) {
        return new Vector3(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    // Returns the Euclidean distance of this vector
    // to the specified vector 'v'.
    public double distanceTo(Vector3 v) {
        return minus(v).length();
    }

    // Returns the length (norm) of this vector.
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    // Normalizes this vector: changes the length of this vector such that it becomes 1.
    // The direction and orientation of the vector is not affected.
    public void normalize() {
        double l = length();
        x /= l;
        y /= l;
        z /= l;
    }

    // Draws a filled circle with a specified radius centered at the (x,y) coordinates of this vector
    // in the existing StdDraw canvas. The z-coordinate is not used.
    public void drawAsDot(double radius, Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(x, y, radius);
    }

    public void drawOctant(Color color, double d) {
        StdDraw.setPenColor(color);
        StdDraw.square(x,y,d);
    }

    // Returns the coordinates of this vector in brackets as a string
    // in the form "[x,y,z]", e.g., "[1.48E11,0.0,0.0]".
    public String toString() {
        return String.format("[%g,%g,%g]", x, y, z);
    }

    // returns vector direction with same numbering as octants: see numbering.png in figures folder
    public int direction() {
        boolean right = x > 0; // right-left: x position
        boolean back = y > 0; // near-far: y position
        boolean top = z > 0; // top-bottom: z position

        if (right && back && top) return 0;
        else if (!right && back && top) return 1;
        else if (!right && !back && top) return 2;
        else if (right && !back && top) return 3;
        else if (right && back) return 4;
        else if (!right && back) return 5;
        else if (!right) return 6;
        else return 7;
    }

    // Checks whether vector starting from position (0, 0, 0) is inside cube centered at same position with edge length l
    public boolean insideBox(double l) {
        return !(Math.abs(x) > l / 2.0) && !(Math.abs(y) > l / 2.0) && !(Math.abs(z) > l / 2.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3 v = (Vector3) o;

        return distanceTo(v) < 0.00000001;
    }

}

