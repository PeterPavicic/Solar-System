import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class testClass {

    public static void main(String[] args) {

        System.out.println("Testing Octree Methods:");

        Octree testSystem = new Octree();

        Body sun = new Body(1.989e30, 696340e3, new Vector3(0, 0, 0), new Vector3(1, 1, 1), StdDraw.YELLOW);
        Body earth = new Body(5.972e24, 6371e3, new Vector3(-1.394555e11, 5.103346e10, 0), new Vector3(-10308.53, -28169.38, 0), StdDraw.BLUE);
        Body mercury = new Body(3.301e23, 2440e3, new Vector3(-5.439054e10, 9.394878e9, 0), new Vector3(-17117.83, -46297.48, -1925.57), StdDraw.GRAY);
        Body venus = new Body(4.86747e24, 6052e3, new Vector3(-1.707667e10, 1.066132e11, 2.450232e9), new Vector3(-34446.02, -5567.47, 2181.10), StdDraw.PINK);
        Body mars = new Body(6.41712e23, 3390e3, new Vector3(-1.010178e11, -2.043939e11, -1.591727E9), new Vector3(20651.98, -10186.67, -2302.79), StdDraw.RED);
        Body mars2 = new Body(6.41712e23, 3390e3, new Vector3(-1.010178e11, -2.043938e11, -1.591727E9), new Vector3(20651.98, -10186.67, -2302.79), StdDraw.RED);

        System.out.println("Testing Body list methods");
        List<Body> testList = new ArrayList<>();
        testList.add(sun);
        testList.add(earth);
        testList.add(mercury);
        testList.add(venus);
        testList.add(mars);

        Octree testListSystem = new Octree();
        testValue(testListSystem.add(testList),5);


        System.out.println("\nTesting numberOfBodies, add(Body):");
        testValue(testSystem.numberOfBodies(),0);

        System.out.println("\nAdding Bodies");
        testValue(testSystem.add(sun),true);

        testValue(testSystem.add(earth),true);
        testValue(testSystem.add(mercury),true);
        testValue(testSystem.add(venus),true);
        testValue(testSystem.add(mars),true);

        testValue(testSystem.numberOfBodies(),5);

        System.out.println("Adding Bodies with the same positions");
        testValue(testSystem.add(earth),false);
        testValue(testSystem.numberOfBodies(),5);

        System.out.println("Adding Body, close to an existing Body");
        testValue(testSystem.add(mars2),true);

    }

    //Testmethoden von Übungsaufgaben kopiert
    public static void testValue(Object given, Object expected) {
        if (given == expected) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: " + expected + " / Given value: " + given);
        }
    }

    public static void testStringEquals(String given, String expected) {
        if (given.equals(expected)) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected String: " + expected + " / Given String: " + given);
        }
    }

    public static void testValue(double given, double expected, double delta) {
        if (given - delta <= expected && given + delta >= expected) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected Value: " + expected + " / Given Value: " + given);
        }
    }

    public static void testVector3(Vector3 given, Vector3 expected, double delta) {
        double distance = given.distanceTo(expected);
        if (distance - delta <= 0.0 && distance + delta >= 0.0) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected Value: " + expected + " / Given Value: " + given);
        }
    }
}
