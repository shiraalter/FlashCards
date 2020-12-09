package FC;

import javafx.scene.layout.BorderStroke;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Frame extends JFrame {


    private JPanel leftPanel;
    private JPanel chooseDeckPanel;
    private JComboBox<String> deckBox;
    private JButton newDeckButton;
    private JPanel chooseButtonPanel;

    private JPanel existingDeckPanel;
    private JPanel existingButtonPanel;
    private JButton studyButton;
    private JButton editButton;
    private JButton deleteDeckButton;

    public Frame(){
        setSize(750,750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());


       leftPanel = new JPanel(new GridLayout(2,1));   //will hold two panels (new deck/existing & study/edit/delete deck)
       chooseDeckPanel = new JPanel(new BorderLayout());
       chooseDeckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
       existingDeckPanel = new JPanel(new BorderLayout());
       existingDeckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

       chooseButtonPanel = new JPanel(new GridLayout(2,1));
       deckBox = new JComboBox<>();
       newDeckButton = new JButton("New DecK");
       chooseButtonPanel.add(deckBox);
       chooseButtonPanel.add(newDeckButton);
       chooseDeckPanel.add(chooseButtonPanel, BorderLayout.CENTER);
       leftPanel.add(chooseDeckPanel);

       existingButtonPanel = new JPanel(new GridLayout(3,1));
       studyButton = new JButton("Study");
       studyButton.setBackground(Color.PINK);
       editButton = new JButton("Edit Deck");
       editButton.setBackground(Color.PINK);
       deleteDeckButton = new JButton("Delete Deck");
       deleteDeckButton.setBackground(Color.PINK);
       existingButtonPanel.add(studyButton);
       existingButtonPanel.add(editButton);
       existingButtonPanel.add(deleteDeckButton);
       existingDeckPanel.add(existingButtonPanel, BorderLayout.CENTER);
       leftPanel.add(existingDeckPanel);

       existingDeckPanel.setVisible(false);

       add(leftPanel, BorderLayout.WEST);

       deckBox.addItem("HAPPY");
       deckBox.addActionListener(actionEvent -> existingDeckPanel.setVisible(true));
       newDeckButton.addActionListener(actionEvent -> existingDeckPanel.setVisible(false));











    }

}

