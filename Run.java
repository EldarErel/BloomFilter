import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to run the app
 * Control the gui interface and the data functions -
 * Loading data from a file into the structure
 * Reading data from a file and looking it up in the structure
 * @author - Eldar Erel
 * @version - 20.12.20
 */
public class Run {
    public static void main(String[] args) { // runs the program
        run();
    }
    /**
     * Runs the app
     */
    public static void run() {
        // variables initializing
        final JFrame frame = new JFrame("Enter details");
        final JTextField txtSize = new JTextField("");
        final JTextField txtQuantity = new JTextField("");
        JPanel panel = new JPanel();
        JPanel btnPanel = new JPanel();
        JLabel lblSize = new JLabel("Please enter the size of the table:");
        JLabel lblQuantity = new JLabel("Please enter the number of hash functions to use:");
        JButton cmdOk = new JButton("OK");
        JButton cmdCancel = new JButton("Cancel");
        //creating the view
        btnPanel.add(cmdOk);
        btnPanel.add(cmdCancel);
        cmdOk.setPreferredSize(cmdCancel.getPreferredSize());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(lblSize);
        panel.add(txtSize);
        panel.add(lblQuantity);
        panel.add(txtQuantity);
        panel.add(btnPanel);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        // button's listeners
        cmdCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        cmdOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int m, k;
                try {
                    m = Integer.parseInt(txtSize.getText());
                    k = Integer.parseInt(txtQuantity.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please provide Integers only", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (m < 1) {
                    JOptionPane.showMessageDialog(null, "The size must be bigger then 1", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (k < 1) {
                    JOptionPane.showMessageDialog(null, "The number of hash function must be bigger then 1", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                HashStructure table = new HashStructure(m, k);
                createWindow(table);
                frame.dispose();
            }
        });
    }

    /**
     * Creates the app window
     * @param table - data structure
     */

    public static void createWindow(final HashStructure table) {
        final JFrame frame = new JFrame("Data Structure");
        JPanel panel = new JPanel();
        JButton cmdAdd = new JButton("Add");
        JButton cmdSearch = new JButton("Search");
        JLabel lbl = new JLabel("Choose file for:");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        cmdAdd.setPreferredSize(cmdSearch.getPreferredSize());
        panel.add(lbl);
        panel.add(cmdAdd);
        panel.add(cmdSearch);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        // button's listeners
        cmdSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchWindow(table);
            }
        });
        cmdAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load(table);
            }
        });
    }
    /**
     * Creates the searching window
     * @param table - data structure
     */
    // creates the search window
    private static void searchWindow(HashStructure table) {
        JFrame frame = new JFrame("Search Results");
        JPanel panel = new JPanel();
        JTextArea txt = new JTextArea();
        JScrollPane sp = new JScrollPane(txt);
        panel.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.add(sp);
        frame.add(panel);
        JFileChooser fc = new JFileChooser();
        int r = fc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    String[] toSearch = currentLine.split(",");
                    for (String search : toSearch) {
                        try {
                            int x = Integer.parseInt(search);
                            txt.append("Searching result for " + search + ": " + table.lookUp(x) + "\n");
                        } catch (NumberFormatException e) {
                            txt.append("Searching result for " + search + ": " + table.lookUp(search) + "\n");
                        }
                    }
                }
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     *  Insert data from file into a data structure
     * @param table - data structure
     */
    // loading data from file to the hash table
    private static void load(HashStructure table) {
        JFileChooser fc = new JFileChooser();
        int r = fc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    String[] toAdd = currentLine.split(",");
                    for (String s : toAdd) {
                        try { // trying to parse int
                            int in = Integer.parseInt(s);
                            table.add(in);
                        } catch (NumberFormatException x) { //  not an integer
                            try {
                                table.add(s);
                            } catch (IllegalArgumentException ex) { // not a string
                                JOptionPane.showMessageDialog(null, "File format error", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                        }
                    }
                }
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
