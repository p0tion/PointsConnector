import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * @author Tulskih Anton.
 *
 * @ {Date}.
 *
 * There is a set of random points on a plane. It's necessary to connect all the points with one broken line, so it have
 * no intersections.
 *
 * Input data: a set of random coordinates of points on a plane.
 *
 * Output data: sorted list of points on a plane that need to be connected in a broken line.
 */
class MyPoint extends Point {

    public MyPoint(int x, int y) {
        super(x,y);
    }

    @Override
    public String toString() {
        return "[" + (int)getX() + ", " + (int)getY() + "]";
    }

}

public class PointsConnector {

    ArrayList<MyPoint> randomListOfPoints; //will store a set of random points.
    ArrayList<MyPoint> listOfPointsToBeConnected; //will store a sorted list of points that will be connected to a line.
    Random random; //used for random calculations

    /**
     * No-arg constructor.
     * Initializes randomListOfPoints and listOfPointsToBeConnected. For initialization of the randomListOfPoints is
     * using HashSet<MyPoint> randomSetOfPoints, so we can be sure, that there won't be equal points.
     * randomListOfPoints will store from 30 to 50 unique random points with coordinates in range of 100<=X<=700 and
     * 100<=Y<=500.
     */
    PointsConnector() {

        random = new Random();
        listOfPointsToBeConnected = new ArrayList<MyPoint>();
        HashSet<MyPoint> randomSetOfPoints = new HashSet<MyPoint>();
        int initialCapacity = random.nextInt(21)+30;
        int x,y;

        while (randomSetOfPoints.size() != initialCapacity) {

            x = random.nextInt(601)+100;
            y = random.nextInt(401)+100;

            randomSetOfPoints.add(new MyPoint(x,y));

        }

        randomListOfPoints = new ArrayList<MyPoint>(randomSetOfPoints);

        Collections.sort(randomListOfPoints, new YcoordSorterComparator()); //sorting randomListOfPoints by Y.

        System.out.println("Sorted by Y input list of points:\n" + randomListOfPoints);

    }

    /**
     * Outputs filled with points listOfPointsToBeConnected.
     * Finds the point with the lowest Y - that will be the first point of the broken line. If It finds several points
     * with equal Ys - makes a line from the most left (lowest X) point to the most right point, then connects this
     * point with next point with the lowest Y from the remaining set. The last point of the broken line will be the
     * point with the highest Y, or if there will be several points with the highest Y - the point with the highest X
     * from this set.
     */
    void connectPoints() {

        ArrayList<MyPoint> pointsInOneRow = new ArrayList<MyPoint>(); //will store points with equal Ys.
        MyPoint currentPoint, nextPoint;
        ListIterator<MyPoint> itr;

        while (randomListOfPoints.size() > 0) {

            pointsInOneRow.clear(); //clear the pointsInOneRow.
            itr = randomListOfPoints.listIterator();
            //initialize list iterator and place it before the first element in the randomListOfPoints.
            currentPoint = itr.next(); //the first element from the randomListOfPoints.
            itr.remove(); //delete the first element from the randomListOfPoints.
            if (itr.hasNext()) { //if it's not the end of the randomListOfPoints.

                nextPoint = itr.next(); //the second element from the randomListOfPoints.

            } else {

                //the point not from the range of possible Xs and Ys, so we can be sure that its' Y won't be equal to the currentPoints'.
                nextPoint = new MyPoint(-1, -1);

            }
            pointsInOneRow.add(currentPoint); //add current point to a list of points, that lies on one line.
            //if the currentPoint and the nextPoint are on the same line, that is parallel to the X axis.
            while (currentPoint.getY() == nextPoint.getY()) {

                pointsInOneRow.add(nextPoint); //add the nextPoint to a list of points, that lies on one line.
                itr.remove(); //delete the second element from the randomListOfPoints .
                currentPoint = nextPoint; //the currentPoint equals to the nextPoint now.
                if (itr.hasNext()) { //if it's not the end of the randomListOfPoints.

                    nextPoint = itr.next();//the second element from the randomListOfPoints.

                } else {

                    //the point not from the range of possible Xs and Ys, so we can be sure that its' Y won't be equal to the currentPoints'.
                    nextPoint = new MyPoint(-1,-1);

                }

            }

            Collections.sort(pointsInOneRow, new XcoordSorterComparator()); //sort the pointsInOneRow by X
            /* add all elements from the pointsInOneRow to the end of the listOfPointsToBeConnected.
             * If the listOfPointsToBeConnected.size == 0 - the first element from the pointsInOneRow will be the start
             * of the broken line, if the listOfPointsToBeConnected.size != 0 - the first element from the
             * pointsInOneRow will be connected with the last element from the listOfPointsToBeConnected*/
            listOfPointsToBeConnected.addAll(listOfPointsToBeConnected.size(), pointsInOneRow);

        }

        System.out.println("\n\nList of connected points:\n" + listOfPointsToBeConnected);

    }

    /**
     * Class used as a comparator in the pointsInOneRow to sort points by X.
     */
    class XcoordSorterComparator implements Comparator<MyPoint> {

        @Override
        public int compare(MyPoint o1, MyPoint o2) {
            return (int)o1.getX()-(int)o2.getX();
        }
    }

    /**
     * Class used as a comparator in the randomListOfPoints to sort points by Y.
     */
    class YcoordSorterComparator implements Comparator<MyPoint> {

        @Override
        public int compare(MyPoint o1, MyPoint o2) {
            return (int)o1.getY()-(int)o2.getY();
        }

    }

    /**
     * Displays a frame with the broken line, tha consists of points from the listOfPointsToBeConnected.
     */
    void draw() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new MyPanel());

        frame.setSize(800, 600);
        frame.setVisible(true);

    }

    class MyPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            for (int i = 0; i < listOfPointsToBeConnected.size() - 1; i++) {

                g.drawLine((int) listOfPointsToBeConnected.get(i).getX(), (int) listOfPointsToBeConnected.get(i).getY(), (int) listOfPointsToBeConnected.get(i + 1).getX(), (int) listOfPointsToBeConnected.get(i + 1).getY());

            }

        }

    }

    public static void main(String[] args) {

        PointsConnector pointsConnector = new PointsConnector();

        pointsConnector.connectPoints();

//        pointsConnector.draw(); //uncomment this line to display the broken line.

    }

}