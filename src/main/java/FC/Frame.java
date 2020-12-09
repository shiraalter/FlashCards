package FC;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private JComboBox<String> deckBox;

    private JPanel leftPanel;
    private JPanel chooseDeckPanel;
    private JPanel existingDeckPanel;

    public Frame(){
        setSize(750,750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());


       leftPanel = new JPanel(new GridLayout(2,1));   //will hold two panels (new deck/existing & study/edit/delete deck)
       chooseDeckPanel = new JPanel();
       leftPanel.add(chooseDeckPanel);
       existingDeckPanel = new JPanel();
       leftPanel.add(existingDeckPanel);








    }

}

