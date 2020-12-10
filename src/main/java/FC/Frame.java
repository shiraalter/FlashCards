package FC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;

//TODO: bug - re-enters clicked method
//TODO move top panel to different method

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
    private JPanel middlePanel, studyPanel, newDeckPanel;

    private JLabel newDeckLabel;
    private JTextField deckNameTb;
    private JButton enterDeckButton;

    private JLabel cardTextArea, deckName;
    private JButton correctButton, incorrectButton, definitionButton, resetButton;
    private JPanel studyButtonPanel;
    private JPanel topPanel;
    private JLabel numOfCards;

    StudyController studyController;
    String deckSelected;

    ComboBoxController boxController;
    EditController editController;

    private JButton addCardButton, deleteCardButton, enterAddButton, enterDeleteButton;
    private JPanel editPanel, editButtonPanel, addCardPanel, addLabelPanel, addFieldPanel, deleteCardPanel;
    private JLabel addTermLabel, addDefLabel;
    private JTextField addTermField, addDefField;
    JScrollPane scrollPane;
    JList cardList;

    public Frame() throws SQLException {
        setSize(750,750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());

        editController = new EditController();

        leftPanel = new JPanel(new GridLayout(2,1));   //will hold two panels (new deck/existing & study/edit/delete deck)
        middlePanel = new JPanel();
        topPanel = new JPanel(new FlowLayout());

        setupDeckOptions();
        setupExistingDeckOptions();
        setupNewDeckField();
        setupStudyMode();


        //action listeners for buttons
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
        editButton.addActionListener(actionEvent -> editButtonClicked());


        setupEditMode();

        add(leftPanel, BorderLayout.WEST);
        add(middlePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
    }

    private void editButtonClicked() {
        studyPanel.setVisible(false);
        editPanel.setVisible(true);
        addCardPanel.setVisible(false);
    }

    private void setupEditMode()  {
        addCardButton = new JButton("Add Card");
        addCardButton.addActionListener(actionEvent -> addCardClicked());
        deleteCardButton = new JButton("Delete Card");
        deleteCardButton.addActionListener(actionEvent -> addCardPanel.setVisible(false));
        editPanel = new JPanel(new BorderLayout());
        editButtonPanel = new JPanel(new FlowLayout());
        editButtonPanel.add(addCardButton);
        editButtonPanel.add(deleteCardButton);
        editPanel.add(editButtonPanel, BorderLayout.NORTH);

        //separate method for new deck to use?
        addCardPanel = new JPanel(new BorderLayout());
        addLabelPanel = new JPanel(new GridLayout(2,1));
        addFieldPanel = new JPanel(new GridLayout(2,1));

        addTermLabel = new JLabel("Term: ");
        addDefLabel = new JLabel("Definition:");
        addLabelPanel.add(addTermLabel);
        addLabelPanel.add(addDefLabel);

        addTermField = new JTextField();
        addDefField = new JTextField();
        addFieldPanel.add(addTermField);
        addFieldPanel.add(addDefField);

        enterAddButton = new JButton("Enter");

        addCardPanel.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
        addCardPanel.add(addLabelPanel, BorderLayout.WEST);
        addCardPanel.add(addFieldPanel, BorderLayout.CENTER);
        addCardPanel.add(enterAddButton, BorderLayout.SOUTH);
        addCardPanel.setVisible(false);


        deleteCardPanel = new JPanel();
        deleteCardButton.addActionListener(actionEvent -> {
            try {
                deleteCardButtonClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        deleteCardPanel.setVisible(false);
        enterDeleteButton = new JButton("Delete me!");
        deleteCardPanel.add(enterDeleteButton);

        editPanel.add(addCardPanel, BorderLayout.CENTER);
        editPanel.add(deleteCardPanel, BorderLayout.SOUTH);
        enterAddButton.addActionListener(actionEvent -> enterCardClicked());
        editPanel.setVisible(false);
        middlePanel.add(editPanel);
    }

    private void addCardClicked() {
        addCardPanel.setVisible(true);
        deleteCardPanel.setVisible(false);
    }

    private void deleteCardButtonClicked() throws SQLException {
        addCardPanel.setVisible(false);
        deleteCardPanel.setVisible(true);

         cardList = new JList(editController.getTermsInDeck(deckSelected).toArray());

         cardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         editPanel.add(cardList);


    }

    private void enterCardClicked() {
        try {
            editController.insertCard(deckSelected, addTermField.getText(), addDefField.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        newDeckPanel.add(deckNameTb, Box.createVerticalStrut(15));
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
        numOfCards.setText("");
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
        editPanel.setVisible(false);
    }

    private void incorrectButtonClicked() {
        currentCard = studyController.getNextToStudy();
        cardTextArea.setText(currentCard.getTerm());
    }

    //BUG
    private void correctButtonClicked() {
        if(studyController.getNextToStudy() != null){
            studyController.masterCard(currentCard);
            setNumOfCardsLabel();
            if(studyController.sizeOfUnmastered() != 0) {           //click on correct twice when 0
                currentCard = studyController.getNextToStudy();
                cardTextArea.setText(currentCard.getTerm());
            }
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

    private void setupDeckOptions() throws SQLException {
        chooseDeckPanel = new JPanel(new BorderLayout());
        chooseDeckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        existingDeckPanel = new JPanel(new BorderLayout());
        existingDeckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        chooseButtonPanel = new JPanel(new GridLayout(2,1));
        boxController = new ComboBoxController();
        deckBox = new JComboBox<>();

        for(String deck : boxController.getAllDecks()){
            deckBox.addItem(deck);
        }

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

