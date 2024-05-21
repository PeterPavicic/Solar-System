import java.awt.*;

// This class represents celestial bodies like stars, planets, asteroids, etc..
public class Body implements CosmicComponent {

    private final double mass;
    private final double radius;
    private Vector3 position; // position of the center.
    private Vector3 currentMovement;
    private Color color; // for drawing the body.
    private Vector3 force;

    public Body(double mass, double radius, Vector3 position, Vector3 currentMovement, Color color) {

        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.currentMovement = currentMovement;
        this.color = color;
        force = new Vector3();
    }

    // Returns the distance between this body and the specified 'body'.
    public double distanceTo(CosmicComponent comp) {

        return position.distanceTo(comp.getPosition());
    }

    // calculates the force body exerts on this
    public Vector3 gravitationalForce(Body body) {

        if (body == this) return new Vector3();
        Vector3 normVector = body.position.minus(position); // vector pointing from this to body
        normVector.normalize();

        double distance = distanceTo(body);
        double multiplier = Simulation.G * (this.mass * body.mass) / (distance * distance);

        return normVector.times(multiplier);
    }

    public void move(Vector3 force) {

        //new Position = position + force * (1/mass) + currentMovement
        Vector3 newPosition = position.plus(force.times(1 / mass)).plus(currentMovement);
        Vector3 newMovement = newPosition.minus(position);

        position = newPosition;
        currentMovement = newMovement;
    }

    public void move() {
        move(force);
    }

    public void setForce(Vector3 force){
        this.force = force;
    }

    public Vector3 getForce() {return force;}

    public Color getColor() {return color;}

    public void draw() {
        position.drawAsDot(1e9 * Math.log10(radius), color);
    }


    @Override
    public int numberOfBodies() {
        return 1;
    }

    public double getMass() {
        return mass;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    public String toString() {
        return "" + mass + " " + position + " " + color;
    }


}

