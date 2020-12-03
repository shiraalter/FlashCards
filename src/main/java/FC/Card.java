package FC;

public class Card {
    private String id;
    private String term;
    private String def;
    private boolean mastered;

    public void setId(String id) {
        this.id = id;
    }

    public String getDef() {
        return def;
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

    public void toggleMastered() {
        mastered = !mastered;
    }
}
