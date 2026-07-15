package chess.view;

import chess.engine.GameEngine;
import chess.engine.GameSnapshot;
import chess.input.Controller;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWindow {

    private static final int FRAME_DELAY_MS = 16;

    private final GameEngine engine;
    private final Controller controller;
    private final Renderer renderer;

    private JFrame frame;
    private JLabel imageLabel;
    private Timer timer;

    private long previousTime;

    public GameWindow(
            GameEngine engine,
            Controller controller,
            Renderer renderer
    ) {
        this.engine = engine;
        this.controller = controller;
        this.renderer = renderer;
    }

    public void start() {
        SwingUtilities.invokeLater(() -> {
            createWindow();
            startGameLoop();
        });
    }

    private void createWindow() {
        frame = new JFrame("Kung Fu Chess");

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        imageLabel = new JLabel();

        imageLabel.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(
                            MouseEvent event
                    ) {
                        handleClick(
                                event.getX(),
                                event.getY()
                        );
                    }
                }
        );

        frame.add(imageLabel);

        renderCurrentState();

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleClick(
            int x,
            int y
    ) {
        // הלחיצה עוברת ל־Controller האמיתי
        controller.click(x, y);

        // מציירים מיד כדי להציג את הבחירה הצהובה
        renderCurrentState();
    }

    private void startGameLoop() {
        previousTime = System.nanoTime();

        timer = new Timer(
                FRAME_DELAY_MS,
                event -> updateFrame()
        );

        timer.start();
    }

    private void updateFrame() {
        long now = System.nanoTime();

        long elapsedMillis =
                (now - previousTime)
                        / 1_000_000;

        previousTime = now;

        if (elapsedMillis > 0) {
            engine.waitTime(
                    (int) elapsedMillis
            );
        }

        renderCurrentState();
    }

    private void renderCurrentState() {
        GameSnapshot snapshot =
                engine.snapshot();

        Img image = renderer.render(
                snapshot,
                controller.getSelected()
        );

        imageLabel.setIcon(
                new ImageIcon(image.get())
        );

        imageLabel.revalidate();
        imageLabel.repaint();
    }
}