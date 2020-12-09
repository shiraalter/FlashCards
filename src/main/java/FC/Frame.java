package FC;

import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderStroke;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.StrokeBorder;
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

    //creating new deck
    private JPanel middlePanel;

    private JPanel newDeckPanel;
    private JLabel newDeckLabel;
    private JTextField deckNameTb;
    private JButton enterDeckButton;

    private JPanel studyPanel;



    public Frame(){
        setSize(750,750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());

        leftPanel = new JPanel(new GridLayout(2,1));   //will hold two panels (new deck/existing & study/edit/delete deck)

        setupDeckOptions();
        setupExistingDeckOptions();


        middlePanel = new JPanel();
       //middlePanel.setLayout(new BorderLayout());
        setupNewDeckField();



        deckBox.addItem("HAPPY");
        deckBox.addActionListener(actionEvent -> comboboxClicked());
        newDeckButton.addActionListener(actionEvent-> newDeckClicked());


        add(leftPanel, BorderLayout.WEST);
        add(middlePanel, BorderLayout.CENTER);




    }



    private void setupNewDeckField() {
        newDeckPanel = new JPanel();
        newDeckPanel.setLayout(new BoxLayout(newDeckPanel, BoxLayout.Y_AXIS));
        newDeckPanel.setBorder(new EmptyBorder(200,0,0,0));

        newDeckLabel = new JLabel("Please Enter The Name Of Your New Deck");
        deckNameTb = new JTextField();
        enterDeckButton = new JButton("Enter New Deck!");
        newDeckPanel.add(newDeckLabel);
        newDeckPanel.add(Box.createVerticalStrut(15));
        newDeckPanel.add(deckNameTb);
        newDeckPanel.add(Box.createVerticalStrut(15));
        newDeckPanel.add(enterDeckButton);
        newDeckPanel.setVisible(false);
        middlePanel.add(newDeckPanel);
    }

    private void newDeckClicked(){
        newDeckPanel.setVisible(true);
        existingDeckPanel.setVisible(false);
    }
    private void comboboxClicked(){
        newDeckPanel.setVisible(false);
        existingDeckPanel.setVisible(true);
    }

    private void setupExistingDeckOptions() {
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
    }

    private void setupDeckOptions() {
        chooseDeckPanel = new JPanel(new BorderLayout());
        chooseDeckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        existingDeckPanel = new JPanel(new BorderLayout());
        existingDeckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        chooseButtonPanel = new JPanel(new GridLayout(2,1));
        deckBox = new JComboBox<>();
        newDeckButton = new JButton("New Deck");
        chooseButtonPanel.add(deckBox);
        chooseButtonPanel.add(newDeckButton);
        chooseDeckPanel.add(chooseButtonPanel, BorderLayout.CENTER);
        leftPanel.add(chooseDeckPanel);
    }

}

