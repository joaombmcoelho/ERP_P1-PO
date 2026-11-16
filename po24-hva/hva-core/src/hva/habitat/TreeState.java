package hva.habitat;

import java.io.Serial;
import java.io.Serializable;

public abstract class TreeState implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    protected String _lifecycleLabel;

    protected int _seasonalCost = 0;

    protected int _age = 0; // years

    protected int _seasons = 0; // when 3, increment age and reset to 0

    protected TreeState(String label, int cost, int age, int seasons) {
        _lifecycleLabel = label;
        _seasonalCost = cost;
        _age = age;
        _seasons = seasons;
    }

    public int getAge() {
        return _age;
    }

    public int seasonalUpkeep() {
        return _seasonalCost;
    }

    public String lifecycleLabel() {
        return _lifecycleLabel;
    }

    public TreeState nextSeason() {
        incrementSeasons();
        return this;
    }

    protected void incrementSeasons() {
        _seasons++;
        if (_seasons % 4 == 0) {
            _age++;
            _seasons = 0;
        }
    }
}
