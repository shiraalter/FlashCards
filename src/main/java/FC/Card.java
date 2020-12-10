package FC;

public class Card {


    private String id;
    private String term;
    private String def;

    public Card(String id, String term, String def){
        this.id = id;
        this.term = term;
        this.def = def;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDef() {
        return def;
    }
    public String getId() {
        return id;
    }
    public void setDef(String def) {
        this.def = def;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(this.getClass())){
            Card card = (Card) obj;
            return this.id.equals(card.getId());
        } else {
            return false;
        }

    }
}
