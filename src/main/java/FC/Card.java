package FC;

public class Card {


    private String id;
    private String term;
    private String def;

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
            Card c = (Card) obj;
            return this.id == ((Card) obj).getId();
        } else {
            return false;
        }

    }
}
