
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GameController extends JFrame {
    private GameModel model;
    private GameView view;
    private static int numMove = 0;

    /* Constructor */

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        // Color button listeners
        this.view.redBallListener(new RedBallListener());
        this.view.yellowBallListener(new YellowBallListener());
        this.view.orangeBallListener(new OrangeBallListener());
        this.view.aquaBallListener(new AquaBallListener());
        this.view.greenBallListener(new GreenBallListener());
        this.view.blueBallListener(new BlueBallListener());
        this.view.purpleBallListener(new PurpleBallListener());
        this.view.pinkBallListener(new PinkBallListener());

        // Button listeners
        this.view.acceptListener(new AcceptListener());
        this.view.quitGameListener(new QuitGameListener());
        this.view.newGameListener(new NewGameListener());
        this.view.giveUp(new GiveUp());
        this.view.changeIcon(new ChangeIcon());
        this.view.showHint(new ShowHint());
    }

    /* Methods */

    // Move the cursor to the next position
    public void moveCursor(List<JLabel> list, JPanel panel, JLabel label) {
        int k = list.indexOf(label);
        if (k == 2) {
            list.set(2, new JLabel(""));
            list.set(4, label);
        } else if (k == 4) {
            list.set(4, new JLabel(""));
            list.set(12, label);
        } else if (k == 12) {
            list.set(12, new JLabel(""));
            list.set(18, label);
        } else if (k == 18) {
            list.set(18, new JLabel(""));
            list.set(16, label);
        } else if (k == 16) {
            list.set(16, new JLabel(""));
            list.set(8, label);
        } else if (k == 8) {
            list.set(8, new JLabel(""));
            list.set(2, label);
        }

        panel.removeAll();

        for (int i = 0; i < 21; i++) {
            panel.add(list.get(i));
        }

        panel.validate();
        panel.repaint();

    }

    // Calculate Score
    public void calculateScore(int i) {
        if (i == 6)
            view.getDisplayScore().setText("1000");
        if (i > 6 && i <= 12)
            view.getDisplayScore().setText("750");
        if (i > 12 && i <= 36)
            view.getDisplayScore().setText("500");
        if (i > 36 && i <= 42)
            view.getDisplayScore().setText("250");
        if (i > 42 && i <= 60)
            view.getDisplayScore().setText("100");
        if (i > 60 && i <= 72)
            view.getDisplayScore().setText("50");
        if (i > 72)
            view.getDisplayScore().setText("0");
    }

    // Show the solution when finish/give up
    public void showSolution() {
        for (int i = 0; i < 6; i++) {
            String s = model.getSecretCode().get(i).toString();
            view.getSolution()[i].setIcon(new ImageIcon("src/images/" + s.concat(".png")));
        }
    }

    // Disable buttons after win/give up
    public void disableButtons() {
        // Display the panel with solution
        view.getHiddenPn().setVisible(true);

        // hide the cursor
        view.getGameCursor().setVisible(false);

        // Disable functional buttons
        view.getAccept().setEnabled(false);
        view.getChangeIcon().setEnabled(false);
        view.getHint().setEnabled(false);

        // Disable color buttons
        view.getRed().setEnabled(false);
        view.getOrange().setEnabled(false);
        view.getYellow().setEnabled(false);
        view.getBlue().setEnabled(false);
        view.getGreen().setEnabled(false);
        view.getPink().setEnabled(false);
        view.getPurple().setEnabled(false);
        view.getAqua().setEnabled(false);
    }

    // Action when win
    public void winGame() {

        int occurrences = Collections.frequency(model.getIndicator(), "Black");
        if (occurrences == 6) {
            calculateScore(numMove);
            disableButtons();
            showSolution();
            JOptionPane.showMessageDialog(null, "Congratulation! You have broken code!");
        }
    }

    // Change Color
    public void changeColor(ImageIcon icon) {
        JLabel currentPosition = view.ringPosition(view.getListOfRing(), view.getGameCursor());
        currentPosition.setIcon(icon);
        int holePosition = view.getHolesList().indexOf(currentPosition);
        String color = currentPosition.getIcon().toString();

        model.getHoles().set(holePosition, color); // set color
        view.reDrawMainPn(view.getMainPn(), view.getDrawHole());
    }

    /* Color Listeners */

    private class RedBallListener implements ActionListener {
        @Override

        public void actionPerformed(ActionEvent e) {
            changeColor(view.getRedBall());
        }
    }

    private class YellowBallListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            changeColor(view.getYellowBall());
        }
    }

    private class OrangeBallListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            changeColor(view.getOrangeBall());
        }
    }

    private class AquaBallListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            changeColor(view.getAquaBall());
        }
    }

    private class GreenBallListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            changeColor(view.getGreenBall());
        }
    }

    private class BlueBallListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            changeColor(view.getBlueBall());
        }
    }

    private class PurpleBallListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            changeColor(view.getPurpleBall());
        }
    }

    private class PinkBallListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            changeColor(view.getPinkBall());
        }
    }

    /* Button Listeners */

    // Start new game button
    private class NewGameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.dispose();
            dispose();
            GameView newView = new GameView();
            GameModel newModel = new GameModel();
            newModel.addObserver(newView);
            numMove = 0;
            new GameController(newModel, newView);
        }
    }

    // Quit Game button
    private class QuitGameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    // Give up
    private class GiveUp extends MouseAdapter {
        Font font = view.getGiveUp().getFont();

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            disableButtons();
            showSolution();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            Font newFont = font;
            Map attributes = newFont.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            view.getGiveUp().setFont(newFont.deriveFont(attributes));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            view.getGiveUp().setFont(font);
        }
    }

    // Change icon button
    private class ChangeIcon implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.setRedBall(new ImageIcon("src/images/box_red.png", "red"));
            view.setOrangeBall(new ImageIcon("src/images/box_orange.png", "orange"));
            view.setYellowBall(new ImageIcon("src/images/box_yellow.png", "yellow"));
            view.setGreenBall(new ImageIcon("src/images/box_green.png", "green"));
            view.setAquaBall(new ImageIcon("src/images/box_aqua.png", "aqua"));
            view.setBlueBall(new ImageIcon("src/images/box_blue.png", "blue"));
            view.setPinkBall(new ImageIcon("src/images/box_pink.png", "pink"));
            view.setPurpleBall(new ImageIcon("src/images/box_purple.png", "purple"));

            view.reDrawSmallPn();
            view.revalidate();
            view.repaint();
        }
    }

    // Show hint button
    private class ShowHint implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            // Find current ring position
            JLabel current = view.ringPosition(view.getListOfRing(), view.getGameCursor());

            // Get the index of the current hole
            int position = view.getHolesList().indexOf(current);

            // get the color
            String color = model.getSecretCode().get(position).toString();

            JOptionPane.showMessageDialog(null, "The current secret color is " + color.toUpperCase());
        }
    }

    // Accept button
    private class AcceptListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            // Move the ring to the next hole
            moveCursor(view.getListOfRing(), view.getDrawRingPn(), view.getGameCursor());

            // increase number of attempts
            numMove++;
            view.getNumAttempt().setText("Attempt: " + String.valueOf(numMove));

            // Redraw indicators
            model.compare();
            view.reDrawRightPn(view.getRightPn(), view.getIndicator());

            // Win game
            winGame();
        }
    }

}
