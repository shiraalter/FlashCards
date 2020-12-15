package flashcards;

import org.junit.Test;

public class CardTest {


    @Test
    public void getId() {
        //given
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);

        //when
        card.setId(id);

        //then
        assert (card.getId().equals("1"));
    }

    @Test
    public void getDef() {
        //given
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);

        //when
        card.setDef(def);

        //then
        assert (card.getDef().equals("definition"));
    }


    @Test
    public void getTerm() {
        //given
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);

        //when
        card.setTerm(term);

        //then
        assert (card.getTerm().equals("term"));
    }

    @Test
    public void testEquals() {
        //given
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);

        Card card2 = new Card(id, term, def);

        //when
        card.setId(id);

        //then
        assert (card.equals(card2));

    }

    @Test
    public void testEqualsFalse() {
        //given
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);

        String id2 = "2";
        String term2 = "terms";
        String def2 = "definitions";
        Card card2 = new Card(id2, term2, def2);

        //when
        boolean cardE = card.equals(card2);

        //then
        assert (!cardE);

    }

    @Test
    public void testEqualsFalseObject() {
        //given
        String id = "1";
        String term = "term";
        String def = "definition";
        Card card = new Card(id, term, def);

        Object obj = new Object();

        //when
        boolean cardE = card.equals(obj);

        //then
        assert (!cardE);

    }
}
