package FC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

//TODO: bug - re-enters clicked method

public class Frame extends JFrame {
    private Color beige = new Color(207, 182, 146);
    private Card currentCard;
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

    private StudyController studyController;
    private String deckSelected;

    private ComboBoxController boxController;
    private final EditController editController;

    private JButton addCardButton, deleteCardButton, enterAddButton, enterDeleteCardButton;
    private JPanel editPanel, editButtonPanel, addCardPanel, addTermPanel, addDefPanel, deleteCardPanel;
    private JLabel addTermLabel, addDefLabel;
    private JTextField addTermField;
    private JTextArea addDefArea;

    private JList cardList;
    private DefaultListModel model;
    private List<String> listOfDecks;
    private JPanel deleteDeckPanel;
    private JButton enterDeleteDeck;

    private String selectExistingDeckString;
    private JPanel welcomePanel;
    private JTextArea welcomeArea;

    public Frame() throws SQLException {
        setSize(750, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());

        leftPanel = new JPanel(new GridLayout(5,1));   //will hold two panels (new deck/existing & study/edit/delete deck)
        middlePanel = new JPanel();

        editController = new EditController();

        //setupWelcomePanel();
        setupDeckOptions();
        setupTopPanel();
        setupExistingDeckOptions();
        setupNewDeckMode();
        setupStudyMode();
        setupEditMode();
        setupDeleteDeckMode();

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
        correctButton.addActionListener(actionEvent -> correctButtonClicked());
        incorrectButton.addActionListener(actionEvent -> incorrectButtonClicked());
        editButton.addActionListener(actionEvent -> {
            try {
                editButtonClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        enterDeleteCardButton.addActionListener(actionEvent -> {
            try {
                enterDeleteCardClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        add(leftPanel, BorderLayout.WEST);
        add(middlePanel);
        add(topPanel, BorderLayout.NORTH);
    }

   /* private void setupWelcomePanel() {
        welcomePanel = new JPanel(new FlowLayout());
        welcomePanel.setVisible(true);
        welcomePanel.setBorder(new EmptyBorder(50,0,0,0));

        welcomeArea = new JTextArea(20,20);
        welcomeArea.setFont(new Font("Arial", Font.BOLD, 15));
        welcomeArea.setText("\"<htm>\" + \" Welcome to FlashCards UI. Please select an existing deck or create a new one!\" + \" </html>\"");
        welcomeArea.setEditable(false);
        welcomePanel.add(welcomeArea);
        middlePanel.add(welcomePanel);
    }

    */

    private void setupDeleteDeckMode() {
        enterDeleteDeck = new JButton("DELETE THIS DECK");
        enterDeleteDeck.setPreferredSize(new Dimension(500,100));
        enterDeleteDeck.setBackground(Color.red);
        enterDeleteDeck.setFont(new Font("Arial", Font.BOLD, 40));
        deleteDeckPanel = new JPanel(new FlowLayout());
        deleteDeckPanel.setBorder(new EmptyBorder(150,0,0,0));
        deleteDeckPanel.add(enterDeleteDeck);
        middlePanel.add(deleteDeckPanel);
        deleteDeckPanel.setVisible(false);

        deleteDeckButton.addActionListener(actionEvent -> deletePanelClicked());
        enterDeleteDeck.addActionListener(actionEvent -> {
            try {
                deleteDeckWarning();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void deletePanelClicked() {
        deleteDeckPanel.setVisible(true);
        studyPanel.setVisible(false);
        editPanel.setVisible(false);
    }

    private void deleteDeckWarning() throws SQLException {
        int optionChosen = JOptionPane.showConfirmDialog(deleteCardPanel,"THIS IS PERMANENT.\n ARE YOU SURE YOU WANT TO DELETE THIS DECK?");
        if (optionChosen == JOptionPane.YES_OPTION){
            editController.deleteDeck(deckSelected);
            deckBox.removeItem(deckSelected);
        }
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
        deleteDeckPanel.setVisible(false);
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
       createAddCardComponents(); //TODO: reuse this method in new deck

        deleteCardPanel = new JPanel(new GridLayout(2, 1,0,20));
        deleteCardPanel.setBorder(new EmptyBorder(20,0,0,0));

        enterDeleteCardButton = new JButton("Delete me!");
        enterDeleteCardButton.setBackground(beige);

        //create JList as model to add and remove cards
        model = new DefaultListModel();
        cardList = new JList(model);

        deleteCardPanel.add(cardList);
        deleteCardPanel.add(enterDeleteCardButton);

        editPanel.add(addCardPanel, BorderLayout.CENTER);
        editPanel.add(deleteCardPanel, BorderLayout.SOUTH);
        enterAddButton.addActionListener(actionEvent -> enterAddCardClicked());
        editPanel.setVisible(false);
        middlePanel.add(editPanel);
    }

    private void createAddCardComponents() {
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
        enterAddButton.setBackground(beige);
        addCardPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        addCardPanel.add(addTermPanel);
        addCardPanel.add(addDefPanel);

        addCardPanel.add(enterAddButton, BorderLayout.SOUTH);
        addCardPanel.setVisible(false);
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

    }

    private void enterDeleteCardClicked() throws SQLException {
        if(cardList.getSelectedValue() !=  null) {
            editController.deleteCard((Card) cardList.getSelectedValue(), deckSelected);
            model.removeElement(cardList.getSelectedValue());
            setNumOfCardsEditMode();
        }
    }

    private void enterAddCardClicked() {
        try {
            if (!addTermField.getText().isEmpty() && !addDefArea.getText().isEmpty()) {
                editController.insertCard(deckSelected, addTermField.getText(), addDefArea.getText());
                setNumOfCardsEditMode();
                addTermField.setText("");
                addDefArea.setText("");
            }
            else{
                JOptionPane.showMessageDialog(middlePanel,"You must insert a term and a definition");
            }

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

    private void setupNewDeckMode() {

        newDeckPanel = new JPanel();
        newDeckPanel.setLayout(new BoxLayout(newDeckPanel, BoxLayout.Y_AXIS));
        newDeckPanel.setBorder(new EmptyBorder(150, 0, 0, 0));

        newDeckLabel = new JLabel("Please Enter The Name Of Your New Deck");
        deckNameTb = new JTextField();
        enterDeckButton = new JButton("Enter New Deck!");
        enterDeckButton.setBackground(beige);
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
        createAddCardComponents(); // TODO : put this yere????
    }


    private void enterNewDeckClicked() throws SQLException {
        String deckNameEntered = deckNameTb.getText();
        if(!deckNameEntered.isEmpty()) {
            editController.initializeNewDeck(deckNameEntered);
            deckNameTb.setText("");
            if(boxController.getAllDecks().contains(deckNameEntered)) {
                deckBox.addItem(deckNameEntered);
            }
            else{
                JOptionPane.showMessageDialog(middlePanel, "ERROR: Deck could not be added");
            }
        }
        else{
            JOptionPane.showMessageDialog(middlePanel,"You must insert a deck name");
        }
    }

    private void newDeckClicked() {
        newDeckPanel.setVisible(true);
        existingButtonPanel.setVisible(false);
        studyPanel.setVisible(false);
        topPanel.setVisible(false);
        editPanel.setVisible(false);
        deleteDeckPanel.setVisible(false);

    }

    private void comboboxClicked() {
        if(deckBox.getSelectedItem() == selectExistingDeckString){      //TODO: welcome screen - dont auto "sample"
        }
        else {
            deckSelected = Objects.requireNonNull(deckBox.getSelectedItem()).toString();
            deckName.setText("Deck: " + deckSelected);
            deckName.setFont(new Font("Arial", Font.BOLD, 14));
            numOfCards.setText("");
        }
        newDeckPanel.setVisible(false);
        existingButtonPanel.setVisible(true);
        studyPanel.setVisible(false);
        topPanel.setVisible(true);
        editPanel.setVisible(false);
        deleteDeckPanel.setVisible(false);


    }

    private void studyOrResetClicked() throws SQLException {
        if(editController.sizeOfCurrentDeck(deckSelected) != 0) {
            studyController = new StudyController();
            studyController.startNewStudySession(deckSelected);
            currentCard = studyController.getNextToStudy();
            cardTextArea.setText(currentCard.getTerm());
            setNumOfCardsStudyMode();

            correctButton.setEnabled(true);
            incorrectButton.setEnabled(true);
            definitionButton.setEnabled(true);
            definitionButton.addActionListener(actionEvent -> cardTextArea.setText("<html>" + currentCard.getDef() + "</html>"));
            studyPanel.setVisible(true);
            topPanel.setVisible(true);
            editPanel.setVisible(false);
            deleteDeckPanel.setVisible(false);
        }
        else{
            JOptionPane.showMessageDialog(middlePanel,"You cannot study an empty deck. \nPlease add a card");
        }
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
            if (studyController.sizeOfStudyDeck() != 0) {           //click on correct twice when 0
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
        numOfCards.setText("Cards in deck: " + studyController.sizeOfStudyDeck());
    }
    private void setNumOfCardsEditMode() throws SQLException {
        //pull number of cards from edit controller if in edit mode
        numOfCards.setText("Cards in deck: " + editController.sizeOfCurrentDeck(deckSelected));
        }



    private void setupExistingDeckOptions() {
        existingButtonPanel = new JPanel(new GridLayout(1,3));
        studyButton = new JButton("Study");
        studyButton.setBackground(Color.PINK);
        editButton = new JButton("Edit Deck");
        editButton.setBackground(Color.PINK);
        deleteDeckButton = new JButton("Delete Deck");
        deleteDeckButton.setBackground(Color.PINK);
        existingButtonPanel.add(studyButton);
        existingButtonPanel.add(editButton);
        existingButtonPanel.add(deleteDeckButton);
        add(existingButtonPanel, BorderLayout.SOUTH);
        existingButtonPanel.setVisible(false);
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
        populateComboBox();

        newDeckButton = new JButton("New Deck");
        chooseButtonPanel.add(newDeckButton);
        chooseButtonPanel.add(deckBox);
        chooseDeckPanel.add(chooseButtonPanel, BorderLayout.CENTER);
        leftPanel.add(chooseDeckPanel);
    }

    private void populateComboBox() {
        selectExistingDeckString = "Select Existing Deck";
        deckBox.addItem(selectExistingDeckString);
        for(String deck : listOfDecks){
            deckBox.addItem(deck);
        }
    }


}

