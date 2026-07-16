package chess.view;

import chess.engine.GameEngine;
import chess.engine.GameSnapshot;
import chess.input.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GameWindow
{
    private static final int FRAME_DELAY = 16;

    private final GameEngine engine;
    private final Controller controller;
    private final Renderer renderer;

    private JFrame frame;
    private JLabel boardLabel;
    private JLabel blackScore;
    private JLabel whiteScore;
    private JTextArea blackMoves;
    private JTextArea whiteMoves;

    private long previousTime;

    public GameWindow(
            GameEngine engine,
            Controller controller,
            Renderer renderer
    )
    {
        this.engine = engine;
        this.controller = controller;
        this.renderer = renderer;
    }

    public void start()
    {
        SwingUtilities.invokeLater(() ->
        {
            createWindow();
            startTimer();
        });
    }

    private void createWindow()
    {
        frame = new JFrame("Kung Fu Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        boardLabel = new JLabel();

        boardLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                controller.click(
                        event.getX(),
                        event.getY()
                );

                render();
            }
        });

        frame.add(createPlayerPanel(false), BorderLayout.WEST);
        frame.add(boardLabel, BorderLayout.CENTER);
        frame.add(createPlayerPanel(true), BorderLayout.EAST);

        render();

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createPlayerPanel(boolean white)
    {
        JLabel score = new JLabel(
                "Score: 0",
                SwingConstants.CENTER
        );

        score.setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.BOLD,
                        18
                )
        );

        JTextArea moves = new JTextArea();
        moves.setEditable(false);
        moves.setFocusable(false);
        moves.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        14
                )
        );

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(220, 800));
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        white ? "White" : "Black"
                )
        );

        panel.add(score, BorderLayout.NORTH);
        panel.add(new JScrollPane(moves), BorderLayout.CENTER);

        if (white)
        {
            whiteScore = score;
            whiteMoves = moves;
        }
        else
        {
            blackScore = score;
            blackMoves = moves;
        }

        return panel;
    }

    private void startTimer()
    {
        previousTime = System.nanoTime();

        new Timer(FRAME_DELAY, event ->
        {
            long now = System.nanoTime();

            int elapsed =
                    (int) ((now - previousTime)
                            / 1_000_000);

            previousTime = now;

            if (elapsed > 0)
            {
                engine.waitTime(elapsed);
            }

            render();
        }).start();
    }

    private void render()
    {
        GameSnapshot snapshot = engine.snapshot();

        Img image = renderer.render(
                snapshot,
                controller.getSelected()
        );

        boardLabel.setIcon(
                new ImageIcon(image.get())
        );

        updatePanel(
                blackScore,
                blackMoves,
                engine.getBlackScore(),
                engine.getBlackMoves()
        );

        updatePanel(
                whiteScore,
                whiteMoves,
                engine.getWhiteScore(),
                engine.getWhiteMoves()
        );

        boardLabel.repaint();
    }

    private void updatePanel(
            JLabel scoreLabel,
            JTextArea movesArea,
            int score,
            List<String> moves
    )
    {
        scoreLabel.setText("Score: " + score);
        movesArea.setText(movesText(moves));
    }

    private String movesText(List<String> moves)
    {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < moves.size(); i++)
        {
            text.append(i + 1)
                    .append(". ")
                    .append(moves.get(i))
                    .append("\n");
        }

        return text.toString();
    }
}