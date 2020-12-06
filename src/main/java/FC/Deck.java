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

    public void removeCard(Card card){
        cards.remove(card);
    }

    public Card getCard(int index){
        return cards.get(index);
    }

    public int getSize(){
        return cards.size();
    }
    
}
