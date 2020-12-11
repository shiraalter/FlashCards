package FC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

//TODO: bug - re-enters clicked method

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
    private JPanel editPanel, editButtonPanel, addCardPanel, addTermPanel, addDefPanel, deleteCardPanel;
    private JLabel addTermLabel, addDefLabel;
    private JTextField addTermField;
    private JTextArea addDefArea;

    JList cardList;
    DefaultListModel model;
    List<String> listOfDecks;

    public Frame() throws SQLException {
        setSize(750, 750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());

        leftPanel = new JPanel(new GridLayout(2, 1));   //will hold two panels (new deck/existing & study/edit/delete deck)
        middlePanel = new JPanel();

        editController = new EditController();

        setupDeckOptions();
        setupTopPanel();
        setupExistingDeckOptions();
        setupNewDeckField();
        setupStudyMode();
        setupEditMode();

        //action listeners for buttons
        deckBox.addActionListener(actionEvent -> comboboxClicked());
        newDeckButton.addActionListener(actionEvent -> newDeckClicked());
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
        editButton.addActionListener(actionEvent -> {
            try {
                editButtonClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        add(leftPanel, BorderLayout.WEST);
        add(middlePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
    }

    private void setupTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20,0));
        deckName = new JLabel();
        numOfCards = new JLabel();
        topPanel.add(deckName);
        topPanel.add(numOfCards);

    }

    private void editButtonClicked() throws SQLException {
        studyPanel.setVisible(false);
        editPanel.setVisible(true);
        addCardPanel.setVisible(false);
        deleteCardPanel.setVisible(false);
        setNumOfCardsEditMode();
    }

    private void setupEditMode() throws SQLException {
        addCardButton = new JButton("Add Card");
        addCardButton.addActionListener(actionEvent -> addCardClicked());
        deleteCardButton = new JButton("Delete Card");
        deleteCardButton.addActionListener(actionEvent -> {
            try {
                deleteCardButtonClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        editPanel = new JPanel(new BorderLayout());
        editButtonPanel = new JPanel(new FlowLayout());
        editButtonPanel.add(addCardButton);
        editButtonPanel.add(deleteCardButton);
        editPanel.add(editButtonPanel, BorderLayout.NORTH);

        //separate method for new deck to use?
        addCardPanel = new JPanel(new GridLayout(3,1,10,10));
        addTermPanel = new JPanel(new GridLayout(1, 2));
        addDefPanel = new JPanel(new GridLayout(1, 2));

        addTermLabel = new JLabel("Term: ");
        addDefLabel = new JLabel("Definition:");
        addTermField = new JTextField();
        addDefArea = new JTextArea(5,19);
        addDefArea.setLineWrap(true);
        addTermPanel.add(addTermLabel);
        addTermPanel.add(addTermField);
        addDefPanel.add(addDefLabel);
        addDefPanel.add(addDefArea);

        enterAddButton = new JButton("Enter New Card");


        addCardPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        addCardPanel.add(addTermPanel);
        addCardPanel.add(addDefPanel);

        addCardPanel.add(enterAddButton, BorderLayout.SOUTH);
        addCardPanel.setVisible(false);

        deleteCardPanel = new JPanel(new GridLayout(2, 1));
        enterDeleteButton = new JButton("Delete me!");

        //create JList as model to add and remove cards
        model = new DefaultListModel();
        cardList = new JList(model);

        deleteCardPanel.add(cardList);
        deleteCardPanel.add(enterDeleteButton);

        editPanel.add(addCardPanel, BorderLayout.CENTER);
        editPanel.add(deleteCardPanel, BorderLayout.SOUTH);
        enterAddButton.addActionListener(actionEvent -> enterAddCardClicked());
        editPanel.setVisible(false);
        middlePanel.add(editPanel);
    }


    private void addCardClicked() {
        addCardPanel.setVisible(true);
        deleteCardPanel.setVisible(false);
    }

    private void deleteCardButtonClicked() throws SQLException {
        model.clear();  //clear old list
        addCardPanel.setVisible(false);
        deleteCardPanel.setVisible(true);

        List<Card> listOfcards = editController.getTermsInDeck(deckSelected);

        //add cards from specified deck into list
        for (int i = 0; i < listOfcards.size(); i++) {
            model.addElement(listOfcards.get(i));
        }
        enterDeleteButton.addActionListener(actionEvent -> {
            try {
                enterDeleteButtonClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void enterDeleteButtonClicked() throws SQLException {
        if(cardList.getSelectedValue() !=  null) {
            editController.deleteCard((Card) cardList.getSelectedValue(), deckSelected);
            model.removeElement(cardList.getSelectedValue());
            setNumOfCardsEditMode();
        }
    }

    private void enterAddCardClicked() {
        try {
           editController.insertCard(deckSelected, addTermField.getText(), addDefArea.getText());
           setNumOfCardsEditMode();
           addTermField.setText("");
           addDefArea.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupStudyMode() {
        studyPanel = new JPanel();
        studyButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        studyPanel.setLayout(new BoxLayout(studyPanel, BoxLayout.Y_AXIS));
        studyPanel.setBorder(new EmptyBorder(200, 0, 0, 0));
        cardTextArea = new JLabel();    //text starts in middle

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


        topPanel.setVisible(false);

        middlePanel.add(studyPanel);
    }

    private void setupNewDeckField() {
        newDeckPanel = new JPanel();
        newDeckPanel.setLayout(new BoxLayout(newDeckPanel, BoxLayout.Y_AXIS));
        newDeckPanel.setBorder(new EmptyBorder(200, 0, 0, 0));

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
        enterDeckButton.addActionListener(actionEvent -> {
            try {
                enterNewDeckClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void enterNewDeckClicked() throws SQLException {
        String deckNameEntered = deckNameTb.getText();
        editController.initializeNewDeck(deckNameEntered);
        deckNameTb.setText("");
        deckBox.addItem(deckNameEntered);    //TODO: fix so don't have to call deck again and loop through?

    }

    private void newDeckClicked() {
        newDeckPanel.setVisible(true);
        existingDeckPanel.setVisible(false);
        studyPanel.setVisible(false);
        topPanel.setVisible(false);
        editPanel.setVisible(false);
    }

    private void comboboxClicked() {
        deckSelected = deckBox.getSelectedItem().toString();
        deckName.setText("Deck: " + deckSelected);
        numOfCards.setText("");
        newDeckPanel.setVisible(false);
        existingDeckPanel.setVisible(true);
        studyPanel.setVisible(false);
        topPanel.setVisible(true);
        editPanel.setVisible(false);

    }

    private void studyOrResetClicked() throws SQLException {
        studyController = new StudyController();
        studyController.startNewStudySession(deckSelected);
        currentCard = studyController.getNextToStudy();
        cardTextArea.setText(currentCard.getTerm());
        setNumOfCardsStudyMode();

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
        if (studyController.getNextToStudy() != null) {
            studyController.masterCard(currentCard);
            setNumOfCardsStudyMode();
            if (studyController.sizeOfUnmastered() != 0) {           //click on correct twice when 0
                currentCard = studyController.getNextToStudy();
                cardTextArea.setText(currentCard.getTerm());
            }
        } else {
            cardTextArea.setText("You finished the deck! Click RESET to start over.");
            correctButton.setEnabled(false);
            incorrectButton.setEnabled(false);
            definitionButton.setEnabled(false);
        }
    }


    private void setNumOfCardsStudyMode() {
        //pull number of cards from study controller in study mode (unmastered list changes while studying)
        numOfCards.setText("Cards in deck: " + studyController.sizeOfUnmastered());
    }
    private void setNumOfCardsEditMode() throws SQLException {
        //pull number of cards from edit controller if in edit mode
        numOfCards.setText("Cards in deck: " + editController.sizeOfEditDeck(deckSelected));
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

        listOfDecks =  boxController.getAllDecks();
        deckBox = new JComboBox<>();
        populateComboBox(); //TODO: combobox

        newDeckButton = new JButton("New Deck");
        chooseButtonPanel.add(deckBox);
        chooseButtonPanel.add(newDeckButton);
        chooseDeckPanel.add(chooseButtonPanel, BorderLayout.CENTER);
        leftPanel.add(chooseDeckPanel);
    }

    private void populateComboBox() throws SQLException {
        for(String deck : listOfDecks){
            deckBox.addItem(deck);
        }
    }


}

