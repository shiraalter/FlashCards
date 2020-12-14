package FC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Frame extends JFrame {
    private final Color beige = new Color(207, 182, 146);
    private Card currentCard;
    private final JPanel leftPanel;
    private JComboBox<String> deckBox;

    private JPanel existingButtonPanel;
    private JButton deleteDeckButton;

    //creating new deck
    private final JPanel middlePanel;
    private JPanel studyPanel;
    private JPanel newDeckPanel;

    private JTextField deckNameTb;

    private JLabel termTextArea;
    private JLabel deckName;
    private JButton correctButton;
    private JButton incorrectButton;
    private JButton definitionButton;
    private JPanel topPanel;
    private JLabel numOfCards;

    private StudyController studyController;
    private String deckSelected;

    private ComboBoxController boxController;
    private final EditController editController;

    private JPanel editPanel;
    private JPanel addCardPanel;
    private JPanel deleteCardPanel;
    private JTextField addTermField;
    private JTextArea addDefArea;

    private JList<Card> cardList;
    private DefaultListModel<Card> model;
    private List<String> listOfDecks;
    private JPanel deleteDeckPanel;

    private String selectExistingDeckString;
    //private JPanel welcomePanel;
    //private JTextArea welcomeArea;

    public Frame() throws SQLException {
        setSize(750, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());

        leftPanel = new JPanel(new GridLayout(5, 1));
        middlePanel = new JPanel();
        editController = new EditController();

        setupDeckOptions();
        //setupWelcomePanel();
        setupTopPanel();
        setupExistingDeckOptions();
        setupNewDeckMode();
        setupStudyMode();
        setupEditMode();
        setupDeleteDeckMode();
        deckBox.addActionListener(actionEvent -> comboBoxClicked());    //scope issue with moving

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

    private void setupDeckOptions() throws SQLException {
        JPanel chooseDeckPanel = new JPanel(new BorderLayout());
        chooseDeckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel existingDeckPanel = new JPanel(new BorderLayout());
        existingDeckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel chooseButtonPanel = new JPanel(new GridLayout(2, 1));
        boxController = new ComboBoxController();

        listOfDecks = boxController.getAllDecks();
        deckBox = new JComboBox<>();

        populateComboBox();

        JButton newDeckButton = new JButton("New Deck");

        newDeckButton.addActionListener(actionEvent -> newDeckClicked());
        chooseButtonPanel.add(newDeckButton);
        chooseButtonPanel.add(deckBox);
        chooseDeckPanel.add(chooseButtonPanel, BorderLayout.CENTER);
        leftPanel.add(chooseDeckPanel);
    }

    private void setupTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        deckName = new JLabel();
        numOfCards = new JLabel();
        topPanel.add(deckName);
        topPanel.add(numOfCards);
    }

    private void setupExistingDeckOptions() {
        existingButtonPanel = new JPanel(new GridLayout(1, 3));
        JButton studyButton = new JButton("Study");
        studyButton.setBackground(Color.PINK);
        studyButton.addActionListener(actionEvent -> {
            try {
                studyOrResetClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        JButton editButton = new JButton("Edit Deck");
        editButton.addActionListener(actionEvent -> {
            try {
                editButtonClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        editButton.setBackground(Color.PINK);
        deleteDeckButton = new JButton("Delete Deck");
        deleteDeckButton.setBackground(Color.PINK);
        existingButtonPanel.add(studyButton);
        existingButtonPanel.add(editButton);
        existingButtonPanel.add(deleteDeckButton);
        add(existingButtonPanel, BorderLayout.SOUTH);
        existingButtonPanel.setVisible(false);
    }

    private void setupNewDeckMode() {
        newDeckPanel = new JPanel();
        newDeckPanel.setLayout(new BoxLayout(newDeckPanel, BoxLayout.Y_AXIS));
        newDeckPanel.setBorder(new EmptyBorder(150, 0, 0, 0));

        JLabel newDeckLabel = new JLabel("Please Enter The Name Of Your New Deck");
        deckNameTb = new JTextField();
        JButton enterDeckButton = new JButton("Enter New Deck!");
        enterDeckButton.setBackground(beige);
        enterDeckButton.addActionListener(actionEvent -> {
            try {
                enterNewDeckClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        newDeckPanel.add(newDeckLabel);
        newDeckPanel.add(Box.createVerticalStrut(15));
        newDeckPanel.add(deckNameTb, Box.createVerticalStrut(15));
        newDeckPanel.add(Box.createVerticalStrut(15));
        newDeckPanel.add(enterDeckButton);
        newDeckPanel.setVisible(false);
        middlePanel.add(newDeckPanel);
        createAddCardComponents();
    }

    private void setupDeleteDeckMode() {
        JButton enterDeleteDeck = new JButton("DELETE THIS DECK");
        enterDeleteDeck.setPreferredSize(new Dimension(500, 100));
        enterDeleteDeck.setBackground(Color.red);
        enterDeleteDeck.setFont(new Font("Arial", Font.BOLD, 40));
        deleteDeckPanel = new JPanel(new FlowLayout());
        deleteDeckPanel.setBorder(new EmptyBorder(150, 0, 0, 0));
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
        int optionChosen = JOptionPane.showConfirmDialog(deleteCardPanel, "THIS IS PERMANENT.\n ARE YOU SURE YOU WANT TO DELETE THIS DECK?");
        if (optionChosen == JOptionPane.YES_OPTION) {
            editController.deleteDeck(deckSelected);
            deckBox.removeItem(deckSelected);
        }
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
        JButton addCardButton = new JButton("Add Card");
        addCardButton.addActionListener(actionEvent -> addCardClicked());
        JButton deleteCardButton = new JButton("Delete Card");
        deleteCardButton.addActionListener(actionEvent ->
        {
            try {
                deleteCardButtonClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        editPanel = new JPanel(new BorderLayout());
        JPanel editButtonPanel = new JPanel(new FlowLayout());
        editButtonPanel.add(addCardButton);
        editButtonPanel.add(deleteCardButton);
        editPanel.add(editButtonPanel, BorderLayout.NORTH);

        createAddCardComponents();
        createDeleteCardComponents();

        editPanel.add(addCardPanel, BorderLayout.CENTER);
        editPanel.add(deleteCardPanel, BorderLayout.SOUTH);
        editPanel.setVisible(false);
        middlePanel.add(editPanel);
    }

    private void createDeleteCardComponents() {
        deleteCardPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        deleteCardPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        JButton enterDeleteCardButton = new JButton("Delete me!");
        enterDeleteCardButton.addActionListener(actionEvent -> {
            try {
                enterDeleteCardClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        enterDeleteCardButton.setBackground(beige);

        //create JList as model to add and remove cards
        model = new DefaultListModel<Card>();
        cardList = new JList<Card>(model);
        JScrollPane scrollableTextArea = new JScrollPane(cardList);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        deleteCardPanel.add(scrollableTextArea);
        deleteCardPanel.add(enterDeleteCardButton);
    }

    private void createAddCardComponents() {
        addCardPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JPanel addTermPanel = new JPanel(new GridLayout(1, 2));
        JPanel addDefPanel = new JPanel(new GridLayout(1, 2));

        JLabel addTermLabel = new JLabel("Term: ");
        JLabel addDefLabel = new JLabel("Definition:");
        addTermField = new JTextField();
        addDefArea = new JTextArea(5, 19);
        addDefArea.setLineWrap(true);
        addTermPanel.add(addTermLabel);
        addTermPanel.add(addTermField);
        addDefPanel.add(addDefLabel);
        addDefPanel.add(addDefArea);

        JButton enterAddButton = new JButton("Enter New Card");
        enterAddButton.addActionListener(actionEvent -> enterAddCardClicked());
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

        List<Card> listOfCards = editController.getTermsInDeck(deckSelected);

        for (Card card : listOfCards) {
            model.addElement(card);
        }

    }

    private void enterDeleteCardClicked() throws SQLException {
        if (cardList.getSelectedValue() != null) {
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
            } else {
                JOptionPane.showMessageDialog(middlePanel, "You must insert a term and a definition");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void enterNewDeckClicked() throws SQLException {
        String deckNameEntered = deckNameTb.getText();
        if (!deckNameEntered.isEmpty()) {
            editController.initializeNewDeck(deckNameEntered);
            deckNameTb.setText("");
            if (boxController.getAllDecks().contains(deckNameEntered)) {
                deckBox.addItem(deckNameEntered);
            } else {
                JOptionPane.showMessageDialog(middlePanel, "ERROR: Deck could not be added");
            }
        } else {
            JOptionPane.showMessageDialog(middlePanel, "You must insert a deck name");
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


    private void comboBoxClicked() {
        if (deckBox.getSelectedItem() == selectExistingDeckString) {
            //TODO: welcome screen - dont auto "sample"
        } else {
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

    private void setupStudyMode() {
        studyPanel = new JPanel();
        JPanel studyButtonPanel = new JPanel(new FlowLayout()); //just for buttons
        studyPanel.setLayout(new BoxLayout(studyPanel, BoxLayout.Y_AXIS));
        studyPanel.setBorder(new EmptyBorder(200, 0, 0, 0));
        termTextArea = new JLabel();//text starts in middle
        termTextArea.setLayout(new FlowLayout());
        JLabel defTextArea = new JLabel();


        correctButton = new JButton("CORRECT!");
        incorrectButton = new JButton("INCORRECT!");
        definitionButton = new JButton("View Definition");

        correctButton.addActionListener(actionEvent -> correctButtonClicked());
        incorrectButton.addActionListener(actionEvent -> incorrectButtonClicked());
        definitionButton.addActionListener(actionEvent -> definitionButtonClicked());

        JButton resetButton = new JButton("RESET");
        resetButton.addActionListener(actionEvent -> {
            try {
                studyOrResetClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        studyButtonPanel.add(correctButton);
        studyButtonPanel.add(incorrectButton);
        studyButtonPanel.add(definitionButton);
        studyButtonPanel.add(resetButton);

        studyPanel.add(termTextArea);
        studyPanel.add(defTextArea);
        studyPanel.add(Box.createVerticalStrut(15));
        studyPanel.add(studyButtonPanel);
        studyPanel.setVisible(false);

        topPanel.setVisible(false);

        middlePanel.add(studyPanel);
    }

    //TODO: move "setvisibles" to sep method for study button
    private void studyOrResetClicked() throws SQLException {
        if (editController.sizeOfCurrentDeck(deckSelected) != 0) {
            studyController = new StudyController();
            studyController.startNewStudySession(deckSelected);
            currentCard = studyController.getNextToStudy();
            termTextArea.setText(currentCard.getTerm());
            setNumOfCardsStudyMode();

            correctButton.setEnabled(true);
            incorrectButton.setEnabled(true);
            definitionButton.setEnabled(true);
            studyPanel.setVisible(true);
            topPanel.setVisible(true);
            editPanel.setVisible(false);
            deleteDeckPanel.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(middlePanel, "You cannot study an empty deck. \nPlease add a card");
        }
    }

    private void incorrectButtonClicked() {
        currentCard = studyController.getNextToStudy();
        termTextArea.setText(currentCard.getTerm());
    }

    private void correctButtonClicked() {
        studyController.masterCard(currentCard);
        setNumOfCardsStudyMode();
        if (studyController.getNextToStudy() != null) {
            currentCard = studyController.getNextToStudy();
            termTextArea.setText(currentCard.getTerm());

        } else {
            termTextArea.setText("You finished the deck! Click RESET to start over.");
            correctButton.setEnabled(false);
            incorrectButton.setEnabled(false);
            definitionButton.setEnabled(false);
        }
    }

    private void definitionButtonClicked() {
        termTextArea.setText("<html>" + currentCard.getDef() + "</html>");
    }


    private void setNumOfCardsStudyMode() {
        //pull number of cards from study controller in study mode (unmastered list changes while studying)
        numOfCards.setText("Cards in deck: " + studyController.sizeOfStudyDeck());
    }

    private void setNumOfCardsEditMode() throws SQLException {
        numOfCards.setText("Cards in deck: " + editController.sizeOfCurrentDeck(deckSelected));
    }

    private void populateComboBox() throws SQLException {
        selectExistingDeckString = "Select Existing Deck";
        deckBox.addItem(selectExistingDeckString);
        for (String deck : listOfDecks) {
            deckBox.addItem(deck);
        }
    }
}

