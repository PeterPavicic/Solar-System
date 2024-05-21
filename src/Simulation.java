import java.awt.*;
import java.util.List;


public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    //Schwellwert für die Berechnung der Distanz
    public static final int T = 1;
    //Number of Bodies, that need to be generated
    public static final int n = 100;

    // specifies after how many in-simulation seconds the frame refreshes
    public static final int refresh_rate = 5;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9;
    //Size of the universe
    public static final double USize = 8 * AU;


    //returns a randomly generated Body
    //z-coordinate set to 0 temporarily - fix after testing
    public static Body randomBody() {

        //alle Himmelskörper sollten zumindest eine Masse von 1e22 haben
        double mass = Math.random() * Math.pow(10, 23 + Math.round(Math.random() * 10));

        //Radius soll von der Masse abhängig sein, darf aber Abweichungen enthalten, da jeder Himmelskörper aus verschiedenen Elementen besteht
        /*
        Referenzen:
        Masse/Radius - Rate
        sol: 2.8564e21
        earth: 9.3737e17
        mercury: 1.3529e17
        venus: 8.0427e17
        */
        double radius = mass / (Math.random() * Math.pow(10, 18 + Math.round(Math.random() * 5)));

        //random Coordinaten innerhalb von USize: USize - (Math.random() * (2 * USize));
        Vector3 position = new Vector3(USize - (Math.random() * (2 * USize)), USize - (Math.random() * (2 * USize)), USize - (Math.random() * (2 * USize)));

        //movement bis höchstens 1e5, wird random bestimmt, ob es negativ oder positiv ist
        Vector3 currentMovement = new Vector3(
                Math.random() * Math.pow(10, 5) * Math.pow(-1, Math.round(Math.random())),
                Math.random() * Math.pow(10, 5) * Math.pow(-1, Math.round(Math.random())),
                Math.random() * Math.pow(10, 5) * Math.pow(-1, Math.round(Math.random())));

        //jeder farbwert sollte zumindest 150 sein, damit die Galaxie bunter wirkt
        //es sollte zumindest eine insgesamte Differenz von 100 zwischen den drei Farbwerten geben, da die Farbe sonst zu grau wird
        int r = 150 + (int) (Math.round(Math.random() * 105));
        int g = 150 + (int) (Math.round(Math.random() * 105));
        int b = 150 + (int) (Math.round(Math.random() * 105));
        while (Math.abs(r - g) + Math.abs(r - b) + Math.abs(g - b) < 50) {
            r = 150 + (int) (Math.round(Math.random() * 105));
            g = 150 + (int) (Math.round(Math.random() * 105));
            b = 150 + (int) (Math.round(Math.random() * 105));
        }
        Color color = new Color(r, g, b);

        //wenn der Himmelskörper zu klein ist, soll nichts ausgegeben werden
        if (radius < 500e3) {
            return null;
        }
        return new Body(mass, radius, position, currentMovement, color);
    }

    public static void main(String[] args) {
        //TODO: please use this class to run your simulation

        //Octree with randomly generated Bodies
        int counter = n;
        Body toAdd;
        Octree test = new Octree();
        //adding black hole to universe center
        test.add(new Body(1.989e39, 696340e3, new Vector3(0, 0, 0), new Vector3(0, 0, 0), StdDraw.DARK_GRAY));
        while (counter > 0) {
            toAdd = randomBody();
            if (toAdd != null) {
                Vector3 diff = new Vector3().minus(toAdd.getPosition());
                if (diff.insideBox(USize)) {
                    test.add(toAdd);
                    counter--;
                }
            }
        }

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(-0.75*USize, 0.75*USize);
        StdDraw.setYscale(-0.75*USize, 0.75*USize);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(StdDraw.BLACK);


        List<Body> bList = test.toList();

        for (Body b : bList) {
//            System.out.println(b);
            b.draw();
        }

        StdDraw.show();

        Octree currTree = test;
        List<Body> bodyList;

        double seconds = 0;
        // simulation loop
        while (true) {

            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            currTree.calcForce();
            currTree.move();
            bodyList = currTree.toList();
//            System.out.println(seconds);

            Octree newTree = new Octree();
            newTree.add(bodyList);

            currTree = newTree;

            // show all movements in StdDraw canvas every refresh_rate seconds
            if (seconds % (refresh_rate) == 0) {

                StdDraw.clear(StdDraw.BLACK);

                for (Body b : bodyList) {
                    b.draw();
                }

//                currTree.drawOctants();
                //Universe border
                StdDraw.setPenColor(StdDraw.YELLOW);
//                StdDraw.square(0,0,USize/2);
                // show new positions
                StdDraw.show();
            }

        }

    }

}
