import javax.swing.*;

public class Game {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println("");
        }

        // Model
        GameModel model = new GameModel();

        // View
        GameView view = new GameView();

        // Controller
        GameController controller = new GameController(model, view);

        model.addObserver(view);
    }
}
