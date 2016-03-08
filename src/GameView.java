
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;

public class GameView extends JFrame implements Observer {

    /* Fields */

    private JPanel bigPn, smallPn, mainPn, rightPn, bottomPn, upperPn, scoreBoard, extraButtonPn;
    private JPanel superRightPn, superLeftPn, drawRingPn, hiddenPn;
    private JLayeredPane layeredPane;

    private Box tipBox;
    private JLabel score = new JLabel("SCORE", SwingConstants.CENTER);
    private JLabel displayScore = new JLabel("", SwingConstants.CENTER);
    private JLabel giveUp, black, white, instruction, hole, hole2, hole3, hole4, hole5, hole6, gameCursor;
    private JLabel blank, blank2, blank3, blank4, blank5, blank6, numAttempt;
    private JLabel[] solution = new JLabel[6];

    private JButton newGame, quitGame, accept, changeIcon, hint;
    private JButton red, orange, yellow, green, aqua, blue, purple, pink;

    private ImageIcon iconWhite, iconBlack, blankIcon;
    private ImageIcon redBall, orangeBall, yellowBall, greenBall, aquaBall, blueBall, purpleBall, pinkBall;

    private Map holeMap, indicatorMap; // Map for holes
    private List<JLabel> listOfRing, drawHole, indicator, keyHoleList, keyIndicatorList, listSolution;
    private List<String> valueIndicatorList;

    /* Constructor  */

    public GameView() {
        setLayout(new BorderLayout());

        /**
         * superLeftPn contains smallPn (SOUTH) + bigPn (NORTH/CENTER)
         * smallPn contains color balls
         * bigPn contains rightPn (EAST) + layeredPn (CENTER) + bottomPn (SOUTH) + "I give up" (NORTH)
         * rightPn contains indicators ( 6 blank ball on the right )
         * bottomPn contains accept button
         * layeredPn contains mainPn (6 holes) and on top of that is the drawRingPn(to draw the gameCursor)
         */

        superLeftPn = new JPanel();
        superLeftPn.setLayout(new BorderLayout());

        bigPn = new JPanel();
        bigPn.setLayout(new BorderLayout());
        bigPn.setPreferredSize(new Dimension(600, 500));

        /* Layered Panel */

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 500));

        drawRingPn = new JPanel();
        drawRingPn.setLayout(new GridLayout(3, 7));
        drawRingPn.setSize(layeredPane.getPreferredSize());
        drawRingPn.setLocation(0, 0);
        drawRingPn.setOpaque(false);

        /* Draw the gameCursor cursor and move it */

        gameCursor = new JLabel(new ImageIcon("src/images/ring-cursor.png"));
        gameCursor.setHorizontalAlignment(JLabel.CENTER);

        // listOfRing = List of rings

        listOfRing = new ArrayList<JLabel>();

        for (int i = 0; i < 21; i++) {
            listOfRing.add(i, new JLabel(""));
        }

        listOfRing.set(2, gameCursor);

        for (int i = 0; i < 21; i++) {
            drawRingPn.add(listOfRing.get(i));
        }

        /*  Main Panel */
        mainPn = new JPanel();
        mainPn.setLayout(new GridLayout(3, 7));
        mainPn.setSize(layeredPane.getPreferredSize());
        mainPn.setLocation(0, 0);

        hole = new JLabel();
        hole2 = new JLabel();
        hole3 = new JLabel();
        hole4 = new JLabel();
        hole5 = new JLabel();
        hole6 = new JLabel();

        hole.setIcon(new ImageIcon("src/images/hole.png", "blank"));
        hole.setHorizontalAlignment(JLabel.CENTER);

        hole2.setIcon(new ImageIcon("src/images/hole.png", "blank"));
        hole2.setHorizontalAlignment(JLabel.CENTER);

        hole3.setIcon(new ImageIcon("src/images/hole.png", "blank"));
        hole3.setHorizontalAlignment(JLabel.CENTER);

        hole4.setIcon(new ImageIcon("src/images/hole.png", "blank"));
        hole4.setHorizontalAlignment(JLabel.CENTER);

        hole5.setIcon(new ImageIcon("src/images/hole.png", "blank"));
        hole5.setHorizontalAlignment(JLabel.CENTER);

        hole6.setIcon(new ImageIcon("src/images/hole.png", "blank"));
        hole6.setHorizontalAlignment(JLabel.CENTER);

        holeMap = new LinkedHashMap<JLabel, String>();
        holeMap.put(hole, ((ImageIcon) hole.getIcon()).getDescription());
        holeMap.put(hole2, ((ImageIcon) hole2.getIcon()).getDescription());
        holeMap.put(hole3, ((ImageIcon) hole3.getIcon()).getDescription());
        holeMap.put(hole4, ((ImageIcon) hole4.getIcon()).getDescription());
        holeMap.put(hole5, ((ImageIcon) hole5.getIcon()).getDescription());
        holeMap.put(hole6, ((ImageIcon) hole6.getIcon()).getDescription());

        keyHoleList = new ArrayList<JLabel>(holeMap.keySet());

        // drawHole = List of holes
        drawHole = new ArrayList<JLabel>();

        for (int i = 0; i < 21; i++) {
            drawHole.add(i, new JLabel(""));
        }
        drawHole.set(2, keyHoleList.get(0));
        drawHole.set(4, keyHoleList.get(1));
        drawHole.set(12, keyHoleList.get(2));
        drawHole.set(18, keyHoleList.get(3));
        drawHole.set(16, keyHoleList.get(4));
        drawHole.set(8, keyHoleList.get(5));

        for (int i = 0; i < 21; i++) {
            mainPn.add(drawHole.get(i));
        }

        /*  Solution Panel - hiddenPn */

        hiddenPn = new JPanel();
        hiddenPn.setLayout(new GridLayout(3, 7));
        hiddenPn.setSize(layeredPane.getPreferredSize());
        hiddenPn.setLocation(0, 0);
        hiddenPn.setOpaque(false);
        hiddenPn.setVisible(false);

        listSolution = new ArrayList<JLabel>();

        for (int i = 0; i < 6; i++) {
            solution[i] = new JLabel("");
            solution[i].setVerticalAlignment(JLabel.CENTER);
        }
        for (int i = 0; i < 21; i++) {
            listSolution.add(i, new JLabel(""));
        }

        listSolution.set(2, solution[0]);
        listSolution.set(4, solution[1]);
        listSolution.set(12, solution[2]);
        listSolution.set(18, solution[3]);
        listSolution.set(16, solution[4]);
        listSolution.set(8, solution[5]);

        for (int i = 0; i < 21; i++) {
            hiddenPn.add(listSolution.get(i));
        }

        layeredPane.add(mainPn, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(drawRingPn, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(hiddenPn, JLayeredPane.MODAL_LAYER);

        /*=========================================================================================================== */

        /* Indicator Panel (indicate if the ball is right or wrong) */

        rightPn = new JPanel();
        rightPn.setLayout(new GridLayout(6, 1));

        blankIcon = new ImageIcon("src/images/blank-ball.png");
        iconBlack = new ImageIcon("src/images/black_peg.png");
        iconWhite = new ImageIcon("src/images/white_peg.png");

        blank = new JLabel();
        blank.setIcon(blankIcon);

        blank2 = new JLabel();
        blank2.setIcon(blankIcon);

        blank3 = new JLabel();
        blank3.setIcon(blankIcon);

        blank4 = new JLabel();
        blank4.setIcon(blankIcon);

        blank5 = new JLabel();
        blank5.setIcon(blankIcon);

        blank6 = new JLabel();
        blank6.setIcon(blankIcon);

        indicatorMap = new LinkedHashMap<JLabel, String>();

        indicatorMap.put(blank, ((ImageIcon) blank.getIcon()).getDescription());
        indicatorMap.put(blank2, ((ImageIcon) blank2.getIcon()).getDescription());
        indicatorMap.put(blank3, ((ImageIcon) blank3.getIcon()).getDescription());
        indicatorMap.put(blank4, ((ImageIcon) blank4.getIcon()).getDescription());
        indicatorMap.put(blank5, ((ImageIcon) blank5.getIcon()).getDescription());
        indicatorMap.put(blank6, ((ImageIcon) blank6.getIcon()).getDescription());

        keyIndicatorList = new ArrayList<JLabel>(indicatorMap.keySet());
        valueIndicatorList = new ArrayList<String>(holeMap.values());

        indicator = new LinkedList<JLabel>();
        indicator.add(keyIndicatorList.get(0));
        indicator.add(keyIndicatorList.get(1));
        indicator.add(keyIndicatorList.get(2));
        indicator.add(keyIndicatorList.get(3));
        indicator.add(keyIndicatorList.get(4));
        indicator.add(keyIndicatorList.get(5));

        for (int i = 0; i < 6; i++) {
            rightPn.add(indicator.get(i));
        }

        /*=========================================================================================================== */

        accept = new JButton(new ImageIcon("src/images/accept-button.png"));

        numAttempt = new JLabel("", SwingConstants.CENTER);
        numAttempt.setFont(new Font("Monospace", Font.BOLD, 30));
        numAttempt.setForeground(Color.BLUE);

        bottomPn = new JPanel(new BorderLayout());
        bottomPn.add(numAttempt, BorderLayout.CENTER);
        bottomPn.add(accept, BorderLayout.EAST);

        /*=========================================================================================================== */

        giveUp = new JLabel("I GIVE UP");
        giveUp.setFont(new Font("Serif", Font.PLAIN, 14));

        giveUp.setToolTipText("In case you want to give up, click here.");

        bigPn.add(layeredPane, BorderLayout.CENTER);
        bigPn.add(rightPn, BorderLayout.EAST);
        bigPn.add(bottomPn, BorderLayout.SOUTH);
        bigPn.add(giveUp, BorderLayout.NORTH);

        /*=========================================================================================================== */

        redBall = new ImageIcon("src/images/red.png", "red");
        red = new JButton(redBall);

        orangeBall = new ImageIcon("src/images/orange.png", "orange");
        orange = new JButton(orangeBall);

        yellowBall = new ImageIcon("src/images/yellow.png", "yellow");
        yellow = new JButton(yellowBall);

        greenBall = new ImageIcon("src/images/green.png", "green");
        green = new JButton(greenBall);

        aquaBall = new ImageIcon("src/images/aqua.png", "aqua");
        aqua = new JButton(aquaBall);

        blueBall = new ImageIcon("src/images/blue.png", "blue");
        blue = new JButton(blueBall);

        purpleBall = new ImageIcon("src/images/purple.png", "purple");
        purple = new JButton(purpleBall);

        pinkBall = new ImageIcon("src/images/pink.png", "pink");
        pink = new JButton(pinkBall);

        FlowLayout layout = new FlowLayout();
        layout.setHgap(35);
        layout.setVgap(17);

        smallPn = new JPanel(layout);
        smallPn.setPreferredSize(new Dimension(600, 70));

        smallPn.add(red);
        smallPn.add(orange);
        smallPn.add(yellow);
        smallPn.add(green);
        smallPn.add(aqua);
        smallPn.add(blue);
        smallPn.add(purple);
        smallPn.add(pink);

        superLeftPn.add(bigPn, BorderLayout.CENTER);
        superLeftPn.add(smallPn, BorderLayout.SOUTH);

        add(superLeftPn, BorderLayout.WEST);
        add(Box.createRigidArea(new Dimension(5, 0)), BorderLayout.CENTER); // add space


        /*=========================================================================================================== */

        superRightPn = new JPanel(new BorderLayout());
        superRightPn.setPreferredSize((new Dimension(250, 600)));

        instruction = new JLabel("    Code Breaker Answer Marbles: ");

        black = new JLabel("In code, in right position.", iconBlack, JLabel.CENTER);
        black.setHorizontalTextPosition(JLabel.RIGHT);

        white = new JLabel("In code, not in right position.", iconWhite, JLabel.CENTER);
        white.setHorizontalTextPosition(JLabel.RIGHT);

        tipBox = Box.createVerticalBox();
        tipBox.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.WHITE, Color.BLACK));

        tipBox.add(instruction);
        tipBox.add(black);
        tipBox.add(white);
        tipBox.setMinimumSize(new Dimension(Integer.MAX_VALUE, tipBox.getMinimumSize().height));

        newGame = new JButton(new ImageIcon("src/images/new-game.png"));
        quitGame = new JButton(new ImageIcon("src/images/quit-button.png"));

        score.setFont(new Font("Monospace", Font.BOLD, 25));

        upperPn = new JPanel();
        upperPn.setLayout(new BorderLayout());
        upperPn.add(newGame, BorderLayout.NORTH);
        upperPn.add(tipBox, BorderLayout.CENTER);

        superRightPn.add(upperPn, BorderLayout.NORTH);

        displayScore.setFont(new Font("Monospace", Font.BOLD, 25));

        extraButtonPn = new JPanel(new BorderLayout());

        changeIcon = new JButton("Change Icon");
        changeIcon.setPreferredSize(new Dimension(Integer.MAX_VALUE, changeIcon.getMinimumSize().height + 20));

        hint = new JButton("Hint");
        hint.setPreferredSize(new Dimension(Integer.MAX_VALUE, changeIcon.getMinimumSize().height + 20));

        extraButtonPn.add(changeIcon, BorderLayout.NORTH);
        extraButtonPn.add(hint, BorderLayout.SOUTH);

        scoreBoard = new JPanel(new BorderLayout());
        scoreBoard.add(score, BorderLayout.NORTH);
        scoreBoard.add(displayScore, BorderLayout.CENTER);
        scoreBoard.add(extraButtonPn, BorderLayout.SOUTH);

        superRightPn.add(scoreBoard, BorderLayout.CENTER);
        superRightPn.add(quitGame, BorderLayout.SOUTH);

        add(superRightPn, BorderLayout.EAST);

        // Modify buttons appearance
        modifyBtn();

        this.setResizable(false);
        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    /* Methods */

    @Override
    public void update(Observable o, Object obj) {
        valueIndicatorList = (ArrayList<String>) obj;
        updateIndicator(valueIndicatorList);
    }

    public void updateIndicator(List list) {
        for (int i = 0; i < 6; i++) {
            if (list.get(i).equals("blank")) {
                keyIndicatorList.get(i).setIcon(blankIcon);
            }
            if (list.get(i).equals("Black")) {
                keyIndicatorList.get(i).setIcon(iconBlack);
            }
            if (list.get(i).equals("White")) {
                keyIndicatorList.get(i).setIcon(iconWhite);
            }
        }
    }

    public JLabel ringPosition(List<JLabel> list, JLabel label) {
        int k = list.indexOf(label);
        if (k == 2) {
            return hole;
        }
        if (k == 4) {
            return hole2;
        }
        if (k == 12) {
            return hole3;
        }
        if (k == 18) {
            return hole4;
        }
        if (k == 16) {
            return hole5;
        }
        if (k == 8)
            return hole6;
        else
            return null;
    }

    public void reDrawMainPn(JPanel pn, List<JLabel> list) {
        pn.removeAll();

        for (int i = 0; i < 21; i++) {
            pn.add(list.get(i));
        }

        pn.revalidate();
        pn.repaint();
    }

    public void reDrawRightPn(JPanel pn, List<JLabel> list) {
        pn.removeAll();

        for (int i = 0; i < 6; i++) {
            pn.add(list.get(i));
        }

        pn.revalidate();
        pn.repaint(
        );
    }

    public void reDrawSmallPn() {
        smallPn.removeAll();

        red.setIcon(redBall);
        orange.setIcon(orangeBall);
        yellow.setIcon(yellowBall);
        green.setIcon(greenBall);
        aqua.setIcon(aquaBall);
        blue.setIcon(blueBall);
        purple.setIcon(purpleBall);
        pink.setIcon(pinkBall);

        smallPn.add(red);
        smallPn.add(orange);
        smallPn.add(yellow);
        smallPn.add(green);
        smallPn.add(aqua);
        smallPn.add(blue);
        smallPn.add(purple);
        smallPn.add(pink);

        smallPn.revalidate();
        smallPn.repaint();

    }

    // Modify color buttons
    public void modifyBtn() {
        ArrayList<JButton> list = new ArrayList<JButton>();
        list.add(red);
        list.add(orange);
        list.add(yellow);
        list.add(aqua);
        list.add(blue);
        list.add(purple);
        list.add(green);
        list.add(pink);
        list.add(newGame);
        list.add(quitGame);
        list.add(accept);

        for (JButton btn : list) {
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    /* Accessors */

    public JLabel[] getSolution() {
        return solution;
    }

    public JPanel getHiddenPn() {
        return hiddenPn;
    }

    public JLabel getGameCursor() {
        return this.gameCursor;
    }

    public List getListOfRing() {
        return this.listOfRing;
    }

    public ImageIcon getRedBall() {
        return this.redBall;
    }

    public JPanel getMainPn() {
        return mainPn;
    }

    public JPanel getRightPn() {
        return rightPn;
    }

    public JPanel getDrawRingPn() {
        return drawRingPn;
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    public JLabel getDisplayScore() {
        return displayScore;
    }

    public JLabel getNumAttempt() {
        return numAttempt;
    }

    public JButton getAccept() {
        return accept;
    }

    public JButton getChangeIcon() {
        return changeIcon;
    }

    public JButton getHint() {
        return hint;
    }

    public JButton getRed() {
        return red;
    }

    public JButton getOrange() {
        return orange;
    }

    public JButton getYellow() {
        return yellow;
    }

    public JButton getGreen() {
        return green;
    }

    public JButton getAqua() {
        return aqua;
    }

    public JButton getBlue() {
        return blue;
    }

    public JButton getPurple() {
        return purple;
    }

    public JButton getPink() {
        return pink;
    }

    public ImageIcon getOrangeBall() {
        return orangeBall;
    }

    public ImageIcon getYellowBall() {
        return yellowBall;
    }

    public ImageIcon getGreenBall() {
        return greenBall;
    }

    public ImageIcon getAquaBall() {
        return aquaBall;
    }

    public ImageIcon getBlueBall() {
        return blueBall;
    }

    public ImageIcon getPurpleBall() {
        return purpleBall;
    }

    public ImageIcon getPinkBall() {
        return pinkBall;
    }

    public List<JLabel> getDrawHole() {
        return drawHole;
    }

    public List<JLabel> getIndicator() {
        return indicator;
    }

    public List<JLabel> getKeyHoleList() {
        return keyHoleList;
    }

    public JLabel getGiveUp() {
        return giveUp;
    }

    public void setOrangeBall(ImageIcon orangeBall) {
        this.orangeBall = orangeBall;
    }

    public void setYellowBall(ImageIcon yellowBall) {
        this.yellowBall = yellowBall;
    }

    public void setGreenBall(ImageIcon greenBall) {
        this.greenBall = greenBall;
    }

    public void setAquaBall(ImageIcon aquaBall) {
        this.aquaBall = aquaBall;
    }

    public void setBlueBall(ImageIcon blueBall) {
        this.blueBall = blueBall;
    }

    public void setPurpleBall(ImageIcon purpleBall) {
        this.purpleBall = purpleBall;
    }

    public void setPinkBall(ImageIcon pinkBall) {
        this.pinkBall = pinkBall;
    }

    public void setRedBall(ImageIcon redBall) {
        this.redBall = redBall;
    }


    /* Button Listener */

    void newGameListener(ActionListener a) {
        newGame.addActionListener(a);
    }

    void quitGameListener(ActionListener a) {
        quitGame.addActionListener(a);
    }

    void acceptListener(ActionListener a) {
        accept.addActionListener(a);
    }

    void giveUp(MouseAdapter a) {
        giveUp.addMouseListener(a);
    }

    void changeIcon(ActionListener a) {
        changeIcon.addActionListener(a);
    }

    void showHint(ActionListener a) {
        hint.addActionListener(a);
    }

    /* Balls Listeners */

    void redBallListener(ActionListener a) {
        red.addActionListener(a);
    }

    void yellowBallListener(ActionListener a) {
        yellow.addActionListener(a);
    }

    void orangeBallListener(ActionListener a) {
        orange.addActionListener(a);
    }

    void aquaBallListener(ActionListener a) {
        aqua.addActionListener(a);
    }

    void greenBallListener(ActionListener a) {
        green.addActionListener(a);
    }

    void blueBallListener(ActionListener a) {
        blue.addActionListener(a);
    }

    void purpleBallListener(ActionListener a) {
        purple.addActionListener(a);
    }

    void pinkBallListener(ActionListener a) {
        pink.addActionListener(a);
    }

}