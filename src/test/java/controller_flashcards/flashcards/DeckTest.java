package controller_flashcards.flashcards;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DeckTest {

    @Test
    public void addCard() {
        //given
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);
        Deck deck = new Deck();

        //when
        deck.addCard(card);

        //then
        assert (deck.getSize() == 1);
    }


    @Test
    public void addCardToOldDeck() {
        //given
        List<Card> cardList = new ArrayList<>();
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);
        Card card2 = new Card(id, term, def);
        Card card3 = new Card(id, term, def);
        cardList.add(card);
        cardList.add(card2);
        Deck deck = new Deck(cardList);

        //when
        deck.addCard(card3);

        //then
        assert (deck.getSize() == 3);
    }

    @Test
    public void removeCard() {
        //given
        List<Card> cardList = new ArrayList<>();
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);
        Card card2 = new Card(id, term, def);
        Card card3 = new Card(id, term, def);
        cardList.add(card);
        cardList.add(card2);
        cardList.add(card3);
        Deck deck = new Deck(cardList);

        //when
        deck.removeCard(card);

        //then
        assert (deck.getSize() == 2);
    }

    @Test
    public void getCard() {
        //given
        List<Card> cardList = new ArrayList<>();
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);
        Card card2 = new Card("2", "terms", "defs");
        Card card3 = new Card("3", "ter", "defin");
        cardList.add(card);
        cardList.add(card2);
        cardList.add(card3);
        Deck deck = new Deck(cardList);

        //when

        //then
        assert (deck.getCard(0) == card);
    }

    @Test
    public void getSize() {
        //given
        List<Card> cardList = new ArrayList<>();
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);
        cardList.add(card);
        cardList.add(card);
        Deck deck = new Deck(cardList);

        //when
        deck.addCard(card);

        //then
        assert (deck.getSize() == 3);
    }

    @Test
    public void clear() {
        //given
        List<Card> cardList = new ArrayList<>();
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);
        Card card2 = new Card("2", "terms", "defs");
        Card card3 = new Card("3", "ter", "defin");
        cardList.add(card);
        cardList.add(card2);
        cardList.add(card3);
        Deck deck = new Deck(cardList);

        //when
        deck.clear();

        //then
        assert (deck.getSize() == 0);
    }
}
