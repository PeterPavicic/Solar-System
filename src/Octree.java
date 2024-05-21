import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.List;

//This class represents an Octree of cosmic components
public class Octree {

    private TreeNode root;

    public boolean add(Body b) {
        if (root == null) {
            root = new TreeNode();
        }
        if (root.get(b.getPosition()) == null) {
            root.addBody(b);
            return true;
        } else {
            return false;
        }
    }

    //Adds all Bodies in the list to the Octree, if a Body with the same position is already in the Octree, the Body will not be added
    //returns the number of new Bodies, that have been added
    public int add(List<Body> bList) {

        if (root == null) {
            root = new TreeNode();
        }

        int newBodies = 0;
        for (Body b : bList) {
            if (root.get(b.getPosition()) == null) {
                root.addBody(b);
                newBodies++;
            }
        }

        return newBodies;
    }

    public int numberOfBodies() {
        if (root == null) return 0;
        return root.numberOfBodies();
    }

    public List<Body> toList() {
        if (root == null) {
            return null;
        }
        return root.toList();
    }

    public void calcForce() {
        if (root == null) {
            return;
        }
        List<Body> list = toList();

        for (Body b : list) {
            b.setForce(root.forceOn(b));
        }
    }

    public void move() {

        List<Body> list = toList();
        for (Body b : list) {
            if (b.getForce() != null) {
                b.move();
            }
        }
    }

    public void drawOctants() {
        root.drawOctants();
    }
}

// nodes containing objects implementing cosmicComponents
class TreeNode implements CosmicComponent {

    // each node has either a body or non-null array os with octants
    private Body body;
    private TreeNode[] os; // stores child octants
    private Vector3 position;
    private double d; // gives the length of an edge of the cube
    private Vector3 massCenter;
    private double mass;


    public TreeNode(Vector3 pos, double sideLength, TreeNode parent) {
        position = pos;
        d = sideLength;
    }

    public TreeNode(Vector3 pos, double sideLength) {
        this(pos, sideLength, null);
    }

    public TreeNode() { // generating very fist TreeNode
        this(new Vector3(), Simulation.USize);
    }

    public boolean isEmpty() {
        return (body == null && os == null);
    }

    public TreeNode getChild(int octant) {
        return os[octant];
    }


    public Vector3 getPosition() {
        return position;
    }

    // initialises empty child nodes
    private void divide() {
        // function only called when adding 2nd component to node -> it already has 1 component
        os = new TreeNode[8];

        // first Vector: direction right, back, up (numbering 0)

        os[0] = new TreeNode(position.plus(new Vector3(d / 4.0, d / 4.0, d / 4.0)), d / 2.0, this);
        os[1] = new TreeNode(position.plus(new Vector3(-d / 4.0, d / 4.0, d / 4.0)), d / 2.0, this);
        os[2] = new TreeNode(position.plus(new Vector3(-d / 4.0, -d / 4.0, d / 4.0)), d / 2.0, this);
        os[3] = new TreeNode(position.plus(new Vector3(d / 4.0, -d / 4.0, d / 4.0)), d / 2.0, this);
        os[4] = new TreeNode(position.plus(new Vector3(d / 4.0, d / 4.0, -d / 4.0)), d / 2.0, this);
        os[5] = new TreeNode(position.plus(new Vector3(-d / 4.0, d / 4.0, -d / 4.0)), d / 2.0, this);
        os[6] = new TreeNode(position.plus(new Vector3(-d / 4.0, -d / 4.0, -d / 4.0)), d / 2.0, this);
        os[7] = new TreeNode(position.plus(new Vector3(d / 4.0, -d / 4.0, -d / 4.0)), d / 2.0, this);

    }

    //returns a body with the same position, returns null if none is found
    public Body get(Vector3 pos) {
        if (pos == null || isEmpty()) return null;
        else if (body != null) {
            if (body.getPosition().distanceTo(pos) < 0.0000001) return body;
            else return null;
        } else if (pos.minus(position).insideBox(d)) {
            int oct = getOctant(pos);
            return os[oct].get(pos);
        } else {
            return null;
        }
    }

    public void addBody(Body b) {
        if (!b.getPosition().minus(position).insideBox(d)) {
            System.out.println("Planet aus Universum geflogen");
            return;
        }
        if (isEmpty()) { // 1st component is being added
            body = b;
            massCenter = b.getPosition();
            mass = b.getMass();
            return;
        }
        else if (body != null) { // 2nd component is being added
            divide();
            Body temp = body;
            body = null;
            addBody(temp);
            addBody(b);
        } else { // octants initialised already & component == null
            int oct = getOctant(b);
            os[oct].addBody(b);
        }

        massCenter = massCenter.times(mass).plus(b.getPosition().times(mass)).times(1/(mass + b.getMass()));
        mass += b.getMass();
    }

    private int getOctant(Body b) {
        Vector3 cPos = b.getPosition();
        Vector3 localV = cPos.minus(position); // vector pointing from this.position to b.position
        if (localV.insideBox(d)) return localV.direction();
        else return -1;
    }

    private int getOctant(Vector3 pos) {
        Vector3 localV = pos.minus(position); // vector pointing from this.position to pos
        if (localV.insideBox(d)) return localV.direction();
        else return -1;
    }

    // calculates the force this exerts on body
    public Vector3 forceOn(Body body) {
        Vector3 forceOnBody = new Vector3();
        if (massCenter == null) return forceOnBody;

        Vector3 bodyPos = body.getPosition();
        double r = bodyPos.distanceTo(position);

        if (d / r <= Simulation.T) {
            Vector3 normVector = massCenter.minus(bodyPos); // vector pointing from body to this
            normVector.normalize();
            double multiplier = Simulation.G * (body.getMass() * mass) / (r * r); // this.getMass() -> mass
            forceOnBody = normVector.times(multiplier);
        } else {
            if (isEmpty()) return forceOnBody;
            else if (this.body != null) forceOnBody = body.gravitationalForce(this.body);
            else
                for (int i = 0; i < 8; i++) {
                    forceOnBody = forceOnBody.plus(getChild(i).forceOn(body));
                }
        }

        return forceOnBody;
    }

    @Override
    public int numberOfBodies() {
        if (isEmpty()) return 0;
        if (body != null) return 1;
        int counter = 0;
        for (TreeNode q : os) {
            counter += q.numberOfBodies();
        }
        return counter;
    }

    public List<Body> toList() {
        if (isEmpty()) {
            return null;
        }
        List<Body> output = new ArrayList<>();

        if (body != null) {
            output.add(body);
            return output;
        }
        for (int i = 0; i < 8; i++) {
            if (!os[i].isEmpty()) {
                output.addAll(os[i].toList());
            }
        }
        return output;
    }

    public void drawOctants() {
        if (isEmpty()) {
            System.out.println("Empty Octree");
            return;
        }
        if (body != null) {
            position.drawOctant(body.getColor(),d/2);
            return;
        }
        for (int i = 0; i < 8; i++) {
            if (!os[i].isEmpty()) {
                os[i].drawOctants();
            }
        }
    }

}


