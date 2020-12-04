package FC;

import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck(){

    }

    public Deck(List<Card> cards){
        this.cards = cards;
    }

    public void addCard(Card card){
        cards.add(card);
    }


}
