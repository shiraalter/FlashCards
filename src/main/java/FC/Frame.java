package FC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Frame extends JFrame {
    private final Color beige = new Color(207, 182, 146);
    private Card currentCard;
    JTextPane textPane;

    private final JPanel leftPanel = new JPanel(new GridLayout(11, 1));
    private JComboBox<String> deckBox;
    private ComboBoxController boxController;
    private List<String> listOfDecks;
    private String selectExistingDeckString, deckSelected;

    private final JPanel middlePanel = new JPanel();
    private final JPanel topPanel = new JPanel();
    private JPanel welcomePanel, studyPanel, newDeckPanel, editPanel, addCardPanel,
            deleteCardPanel, deleteDeckPanel, existingButtonPanel;
    private JTextField deckNameTb, addTermField;
    private JTextArea addDefArea;
    private JLabel deckName, numOfCards;
    private JButton correctButton, incorrectButton, definitionButton, deleteDeckButton;

    private final StudyController studyController = new StudyController();
    private EditController editController;

    private JList<Card> cardList;
    private DefaultListModel<Card> model;

    public Frame() throws SQLException {
        setSize(750, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flashcard UI");
        setLayout(new BorderLayout());
        initializePanels();
    }

    private void initializePanels() throws SQLException {
        add(middlePanel);
        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        setupTopPanel();
        setupExistingDeckOptions();
        setupNewDeckMode();
        setupStudyMode();
        setupEditMode();
        setupDeleteDeckMode();
        setWelcomePanel();
        setupDeckOptions();
    }

    private void setupDeckOptions() throws SQLException {
        JButton newDeckButton = new JButton("New Deck");
        newDeckButton.setBackground(SystemColor.PINK);
        newDeckButton.addActionListener(actionEvent -> newDeckClicked());
        setUpDeckBox();
        leftPanel.add(newDeckButton);
        leftPanel.add(deckBox);
    }

    private void setUpDeckBox() throws SQLException {
        boxController = new ComboBoxController();
        listOfDecks = boxController.getAllDecks();
        deckBox = new JComboBox<>();
        deckBox.setBackground(Color.PINK);
        populateComboBox();
        deckBox.addActionListener(actionEvent -> comboBoxClicked());
    }

    private void setupTopPanel() {
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
                studyClicked();
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
        editController = new EditController();
        JButton addCardButton = new JButton("Add Card");
        addCardButton.setBackground(beige);
        addCardButton.addActionListener(actionEvent -> addCardClicked());
        JButton deleteCardButton = new JButton("Delete Card");
        deleteCardButton.setBackground(beige);
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
        enterDeleteCardButton.setBackground(beige);
        enterDeleteCardButton.addActionListener(actionEvent -> {
            try {
                enterDeleteCardClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        JScrollPane scrollableTextArea = setUpCardList();
        deleteCardPanel.add(scrollableTextArea);
        deleteCardPanel.add(enterDeleteCardButton);
    }

    private JScrollPane setUpCardList() {
        model = new DefaultListModel<>();
        cardList = new JList<>(model);
        JScrollPane scrollableTextArea = new JScrollPane(cardList);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollableTextArea;
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
        model.clear();
        addCardPanel.setVisible(false);
        deleteCardPanel.setVisible(true);
        List<Card> listOfCards = editController.getTermsInDeck(deckSelected);
        for (Card card : listOfCards) {
            model.addElement(card);
        }

    }

    private void enterDeleteCardClicked() throws SQLException {
        if (cardList.getSelectedValue() != null) {
            editController.deleteCard(cardList.getSelectedValue(), deckSelected);
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
        welcomePanel.setVisible(false);
        newDeckPanel.setVisible(true);
        existingButtonPanel.setVisible(false);
        studyPanel.setVisible(false);
        topPanel.setVisible(false);
        disableMutatorPanels();

    }

    private void setWelcomePanel() {
        String welcomeMessage = " Hello! Welcome to our Flash Cards UI.\n\n Select an existing deck or create a new one!\n\n " +
                "Good luck studying!";
        welcomePanel = new JPanel();
        welcomePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        JTextArea welcomeArea = new JTextArea(welcomeMessage);
        welcomeArea.setEditable(false);
        welcomeArea.setBackground(beige);
        welcomeArea.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomePanel.add(welcomeArea);
        middlePanel.add(welcomePanel);
    }

    private void comboBoxClicked() {
        if (deckBox.getSelectedItem() == selectExistingDeckString) {
            topPanel.setVisible(false);
            existingButtonPanel.setVisible(false);
            welcomePanel.setVisible(true);
        } else {
            deckSelected = Objects.requireNonNull(deckBox.getSelectedItem()).toString();
            deckName.setText("Deck: " + deckSelected);
            deckName.setFont(new Font("Arial", Font.BOLD, 14));
            numOfCards.setText("");
            welcomePanel.setVisible(false);
            topPanel.setVisible(true);
            existingButtonPanel.setVisible(true);
        }
        newDeckPanel.setVisible(false);
        studyPanel.setVisible(false);
        disableMutatorPanels();
    }

    private void setupStudyMode() {
        JPanel studyButtonPanel = setUpStudyControls();
        setUpStudyPanel(studyButtonPanel);

        topPanel.setVisible(false);
        middlePanel.add(studyPanel);
    }

    private void setUpStudyPanel(JPanel studyButtonPanel) {
        studyPanel = new JPanel();
        studyPanel.setLayout(new BoxLayout(studyPanel, BoxLayout.Y_AXIS));
        studyPanel.setBorder(new EmptyBorder(200, 0, 0, 0));

        Font font = new Font("Arial", Font.PLAIN, 26);
        textPane = new JTextPane();


        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        textPane.setCharacterAttributes(center, true);
        textPane.setBorder(new LineBorder((Color.BLACK)));
        textPane.setFont(font);
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        Rectangle rect = new Rectangle(0, 0, 415, 249);
        JPanel container = new JPanel();
        scrollPane.setPreferredSize(new Dimension(rect.width, rect.height));
        scrollPane.setBounds(rect);
        container.add(scrollPane);

        studyPanel.add(container, BorderLayout.CENTER);
        studyPanel.add(Box.createVerticalStrut(15));
        studyPanel.add(studyButtonPanel);
        studyPanel.setVisible(false);
    }


    private JPanel setUpStudyControls() {
        JPanel studyButtonPanel = new JPanel(new FlowLayout());
        correctButton = new JButton("CORRECT!");
        incorrectButton = new JButton("INCORRECT!");
        definitionButton = new JButton("View Definition");
        correctButton.addActionListener(actionEvent -> correctButtonClicked());
        incorrectButton.addActionListener(actionEvent -> incorrectButtonClicked());
        definitionButton.addActionListener(actionEvent -> definitionButtonClicked());

        JButton resetButton = new JButton("RESET");
        resetButton.addActionListener(actionEvent -> {
            try {
                resetClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        studyButtonPanel.add(correctButton);
        studyButtonPanel.add(incorrectButton);
        studyButtonPanel.add(definitionButton);
        studyButtonPanel.add(resetButton);
        return studyButtonPanel;
    }

    private void studyClicked() throws SQLException {
        if (studyController.getDeck(deckSelected).getSize() != 0) {
            initializeStudyLogic();
            textPane.setText(currentCard.getTerm());
            setRemainingCardsToStudy();

            enableStudyControls();
            showStudyModePanels();
            disableMutatorPanels();
        } else {
            JOptionPane.showMessageDialog(middlePanel, "You cannot study an empty deck. \nPlease add a card");
        }
    }

    private void disableMutatorPanels() {
        editPanel.setVisible(false);
        deleteDeckPanel.setVisible(false);
    }

    private void showStudyModePanels() {
        studyPanel.setVisible(true);
        topPanel.setVisible(true);
    }

    private void resetClicked() throws SQLException {
        initializeStudyLogic();
        textPane.setText(currentCard.getTerm());
        setRemainingCardsToStudy();
        enableStudyControls();
    }

    private void initializeStudyLogic() throws SQLException {

        studyController.startNewStudySession(deckSelected);
        currentCard = studyController.getNextToStudy();
    }

    private void enableStudyControls() {
        correctButton.setEnabled(true);
        incorrectButton.setEnabled(true);
        definitionButton.setEnabled(true);
    }

    private void incorrectButtonClicked() {
        currentCard = studyController.getNextToStudy();
        textPane.setText(currentCard.getTerm());
    }

    private void correctButtonClicked() {
        studyController.masterCard(currentCard);
        setRemainingCardsToStudy();
        if (studyController.getNextToStudy() != null) {
            currentCard = studyController.getNextToStudy();
            textPane.setText(currentCard.getTerm());


        } else {
            textPane.setText("You finished the deck! Click RESET to start over.");
            correctButton.setEnabled(false);
            incorrectButton.setEnabled(false);
            definitionButton.setEnabled(false);
        }
    }

    private void definitionButtonClicked() {
        textPane.setText(currentCard.getDef());
    }

    private void setRemainingCardsToStudy() {
        numOfCards.setText("Remaining cards to master: " + studyController.sizeOfStudyDeck());
    }

    private void setNumOfCardsEditMode() throws SQLException {
        numOfCards.setText("Cards in deck: " + editController.sizeOfCurrentDeck(deckSelected));
    }

    private void populateComboBox() {
        selectExistingDeckString = "Select Existing Deck";
        deckBox.addItem(selectExistingDeckString);
        for (String deck : listOfDecks) {
            deckBox.addItem(deck);
        }
    }
}

