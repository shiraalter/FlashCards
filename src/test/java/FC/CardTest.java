package FC;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void setId() {
        //given
        Card card = new Card();
        String id = "1";

        //when
        card.setId(id);

        //then
        assert(card.id.equals("1"));
    }

    @Test
    public void getDef() {
    }

    @Test
    public void getId() {
    }

    @Test
    public void setDef() {
    }

    @Test
    public void getTerm() {
    }

    @Test
    public void setTerm() {
    }

    @Test
    public void testEquals() {
    }
}