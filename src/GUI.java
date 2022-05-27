import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI {

    // user input label indicates go how many ticks
    private final JTextField nRun;

    // the run indicated number button
    private JButton nRunButton;

    // input label for users to input file name
    private final JTextField fileName;

    //
    private final JTextArea textArea;

    // the main panel
    private final JPanel panel;

    // the sub frame for more information
    private final JFrame subFrame;

    public GUI(Simulator simulator) {
        // the frame shown
        JFrame frame = new JFrame();
        subFrame = new JFrame();
        frame.setSize(10 * simulator.width, 10 * simulator.length);
        subFrame.setSize(400, 400);

        // this is the main panel that draws circles
        // red circles represents active agents
        // green circles represents quiet agents
        // black circles represents agents in jail
        // blue circles represents healthy cops
        // purple circles represents injured cops
        // yellow circles represents injured agents
        // duplicate and smaller circles means they are at same place
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Point coord : simulator.map.keySet()) {
                    if (simulator.map.get(coord).isEmpty()) continue;
                    int size = 20;
                    for (Turtle turtle : simulator.map.get(coord)) {
                        // set color for circles
                        if (turtle instanceof Cop) {
                            if (turtle.injureTerm <= 0) g.setColor(Color.blue);
                            else g.setColor(Color.magenta);
                        } else if (turtle instanceof Agent) {
                            if (((Agent) turtle).isActive)
                                g.setColor(Color.red);
                            else if (((Agent) turtle).jailTerm > 0)
                                g.setColor(Color.gray);
                            else if (turtle.injureTerm > 0)
                                g.setColor(Color.yellow);
                            else g.setColor(Color.green);
                        }
                        // draw the shapes
                        g.fillOval(20 * coord.x + 20 - size, 20 * coord.y
                                + 20 - size, size, size);
                        // draw the outlines
                        g.setColor(Color.black);
                        g.drawOval(20 * coord.x + 20 - size, 20 * coord.y
                                + 20 - size, size, size);
                        size -= 3;
                    }
                }
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(400, 400, 400,
                400));
        // add function when clicking on a point, opens sub frame to show detail
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textArea.setText("");
                if (simulator.map.containsKey(new Point(e.getX() / 20,
                        e.getY() / 20)) && !simulator.map.get(new Point(
                                e.getX() / 20, e.getY() / 20)).isEmpty())
                {
                    for (Turtle turtle : simulator.map.get(new Point(e.getX()
                            / 20, e.getY() / 20))) {
                        textArea.setText(textArea.getText() + turtle.toString()
                                + '\n');
                    }
                    subFrame.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        panel.addMouseListener(mouseListener);

        // create go button and logic
        // the go button
        JButton goButton = new JButton("go");
        ActionListener goListener = e -> {
            if (simulator.map.containsKey(new Point(0, 0))) {
                simulator.go();
                panel.removeAll();
                panel.updateUI();
            } else {
                textArea.setText("please set up before go!");
                subFrame.setVisible(true);
            }
        };
        goButton.addActionListener(goListener);

        // create set-up button and logic
        // the set-up button
        JButton setupButton = new JButton("setup");
        ActionListener setupListener = e -> {
            simulator.setup();
            panel.removeAll();
            panel.updateUI();
        };
        setupButton.addActionListener(setupListener);

        // the text: "please enter number of runs below, press enter to update"
        JLabel nRunText = new JLabel("please enter number of runs below, "
                + "press enter to update");

        // create user input for number of runs and set update button rule
        nRun = new JTextField("1000");
        ActionListener runTextListener = e -> {
            nRunButton.setText("run " + nRun.getText() + " times");
        };
        nRun.addActionListener(runTextListener);

        // create run n times button and logic
        nRunButton = new JButton("run " + nRun.getText() + " times");
        ActionListener runListener = e -> {
            for (int i = 0; i < Integer.parseInt(nRun.getText()); i++) {
                simulator.go();
            }
            panel.removeAll();
            panel.updateUI();
        };
        nRunButton.addActionListener(runListener);

        // the text "please enter a file name to save as .csv"
        JLabel textFileName = new JLabel("please enter a file name to " +
                "save as " + ".csv");

        fileName = new JTextField("dataSample.csv");
        // create save file button and its logic
        // the button to save file
        JButton saveFile = new JButton("save file");
        ActionListener saveListener = e -> {
            simulator.writeToCsv(fileName.getText());
        };
        saveFile.addActionListener(saveListener);

        // create sub-frame information bar
        textArea = new JTextArea(10, 20);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        // add everything into a sidebar on frame window
        // the sidebar panel
        JPanel tools = new JPanel();
        tools.setLayout(new GridLayout(20, 1));
        tools.add(setupButton);
        tools.add(goButton);
        tools.add(nRunText);
        tools.add(nRun);
        tools.add(nRunButton);
        tools.add(textFileName);
        tools.add(fileName);
        tools.add(saveFile);
        subFrame.add(textArea);
        frame.add(tools, BorderLayout.EAST);
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("simulator");
        frame.pack();
        frame.setVisible(true);
    }
}
