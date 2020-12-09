package FC;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

//TODO: bug - click correct twice before number updates

public class Frame extends JFrame {

    Card currentCard;
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
    private JLabel cardTextArea;
    private JLabel deckName;
    private JButton correctButton;
    private JButton incorrectButton;
    private JButton definitionButton;
    private JButton resetButton;
    private JPanel studyButtonPanel;
    private JPanel topPanel;
    private JLabel numOfCards;

    StudyController studyController;
    String deckSelected;

    public Frame(){
        setSize(750,750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());

        leftPanel = new JPanel(new GridLayout(2,1));   //will hold two panels (new deck/existing & study/edit/delete deck)

        setupDeckOptions();
        setupExistingDeckOptions();


        middlePanel = new JPanel();
        topPanel = new JPanel(new FlowLayout());

        setupNewDeckField();
        setupStudyMode();


        deckBox.addItem("Sample");
        deckBox.addActionListener(actionEvent -> comboboxClicked());
        newDeckButton.addActionListener(actionEvent-> newDeckClicked());
        studyButton.addActionListener(actionEvent -> {
            try {
                studyOrResetClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        resetButton.addActionListener(actionEvent -> {
            try {
                studyOrResetClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });




        add(leftPanel, BorderLayout.WEST);
        add(middlePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);


    }

    private void setupStudyMode() {
        studyPanel = new JPanel();
        studyButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        studyPanel.setLayout(new BoxLayout(studyPanel, BoxLayout.Y_AXIS));
        studyPanel.setBorder(new EmptyBorder(200,0,0,0));
        deckName = new JLabel();
        cardTextArea = new JLabel();    //text starts in middle
        numOfCards = new JLabel();

        correctButton = new JButton("CORRECT!");
        incorrectButton = new JButton("INCORRECT!");
        definitionButton = new JButton("View Definition");
        resetButton = new JButton("RESET");
        studyButtonPanel.add(correctButton);
        studyButtonPanel.add(incorrectButton);
        studyButtonPanel.add(definitionButton);
        studyButtonPanel.add(resetButton);

        studyPanel.add(cardTextArea);
        studyPanel.add(Box.createVerticalStrut(15));
        studyPanel.add(studyButtonPanel);
        studyPanel.setVisible(false);
        //deckName.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel.add(deckName, BorderLayout.CENTER);
        topPanel.add(numOfCards, BorderLayout.EAST);
        topPanel.setVisible(false);

        middlePanel.add(studyPanel);
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
        studyPanel.setVisible(false);
        topPanel.setVisible(false);
    }
    private void comboboxClicked(){
        deckSelected = deckBox.getSelectedItem().toString();
        deckName.setText("Deck: " + deckSelected);
        newDeckPanel.setVisible(false);
        existingDeckPanel.setVisible(true);
        studyPanel.setVisible(false);
        topPanel.setVisible(true);


    }


    private void studyOrResetClicked() throws SQLException {
        studyController = new StudyController();
        studyController.startNewStudySession(deckSelected);
        currentCard = studyController.getNextToStudy();
        cardTextArea.setText(currentCard.getTerm());
        setNumOfCardsLabel();

        correctButton.setEnabled(true);
        incorrectButton.setEnabled(true);
        definitionButton.setEnabled(true);
        definitionButton.addActionListener(actionEvent -> cardTextArea.setText("<html>" + currentCard.getDef() + "</html>"));
        correctButton.addActionListener(actionEvent -> correctButtonClicked());
        incorrectButton.addActionListener(actionEvent -> incorrectButtonClicked());
        studyPanel.setVisible(true);
        topPanel.setVisible(true);
    }

    private void incorrectButtonClicked() {
        currentCard = studyController.getNextToStudy();
        cardTextArea.setText(currentCard.getTerm());
    }

    //BUG
    private void correctButtonClicked() {

        currentCard = studyController.getNextToStudy();
        setNumOfCardsLabel();
        if(currentCard != null){
            studyController.masterCard(currentCard);
            cardTextArea.setText(currentCard.getTerm());
        }
        else{
        cardTextArea.setText("You finished the deck! Click RESET to start over.");
        correctButton.setEnabled(false);
        incorrectButton.setEnabled(false);
        definitionButton.setEnabled(false);
        }
    }

    private void setNumOfCardsLabel() {
        numOfCards.setText("Cards left: " + getRemainingCards());
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

    private int getRemainingCards(){
        return studyController.sizeOfUnmastered();
    }
}

