import java.util.*;

public class GameModel extends Observable {
    /* Fields */

    private List<String> secretCode, holes, indicator;
    private Set<String> set;

    /* Constructor */

    public GameModel() {

        // List of available colors to choose

        List<String> colorBag = new ArrayList<String>();
        colorBag.add("red");
        colorBag.add("orange");
        colorBag.add("yellow");
        colorBag.add("green");
        colorBag.add("aqua");
        colorBag.add("blue");
        colorBag.add("purple");
        colorBag.add("pink");

        /* shuffle the colorBag, pick the first 6 out 8 color then put into secretCode
        => we have a randomly generated secret code */

        secretCode = new ArrayList<String>();

        Collections.shuffle(colorBag);
        for (int i = 0; i < 6; i++) {
            secretCode.add(i, colorBag.get(i));
        }

        holes = new ArrayList<String>();        // 6 holes player would need to fill colors in
        indicator = new ArrayList<String>();    // Indicator shows whether the color filled in is correct or not
        for (int i = 0; i < 6; i++) {
            holes.add("blank");
            indicator.add("blank");
        }
    }

    /* Methods */

    public List getSecretCode() {
        return secretCode;
    }

    public List<String> getHoles() {
        return holes;
    }

    public List getIndicator() {
        return indicator;
    }

    // Compare the selections with solution and notify changes
    public void compare() {
        int rightColor = 0; // will show black
        int inSecret = 0; // will show white

        set = new LinkedHashSet<String>();

        for (int i = 0; i < 6; i++) {
            set.add(holes.get(i));
        }

        // Count number of correct color and position
        for (int i = 0; i < 6; i++) {
            if (secretCode.get(i).equals(holes.get(i))) {   // right color AND right position
                rightColor++;
            } else {
                if (set.contains(secretCode.get(i))){       // right color but wrong position
                    inSecret++;
                }
            }
        }

        // Redraw the indicator

        for (int k = 0; k < rightColor; k++) {
            indicator.set(k, "Black");
        }

        for (int j = rightColor; j < (rightColor + inSecret); j++) {
            indicator.set(j, "White");
        }

        for (int n = rightColor + inSecret; n < 6; n++) {
            indicator.set(n, "blank");
        }

        setChanged();
        notifyObservers(indicator);
    }
}
