package hva.habitat;

import hva.EntityWriter;

import java.io.Serial;
import java.io.Serializable;

public abstract class Tree implements Serializable, Comparable<Tree> {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private final String _key;

    private final String _name;

    private final double _baseUpkeep;

    private TreeState _state;

    public Tree(String key, String name, double baseUpkeep, TreeState initialState) {
        _key = key;
        _name = name;
        _baseUpkeep = baseUpkeep;
        _state = initialState;
    }

    public String getKey() {
        return _key;
    }

    public String getName() {
        return _name;
    }

    public double getBaseUpkeep() {
        return _baseUpkeep;
    }

    public double seasonalUpkeep() {
        return _state.seasonalUpkeep();
    }

    public double upkeep() {
        return getBaseUpkeep() * seasonalUpkeep() * Math.log(1 + age());
    }

    public int age()   {
        return _state.getAge();
    }

    public String lifecycle() {
        return _state.lifecycleLabel();
    }

    public void advanceSeason() {
        _state = _state.nextSeason();
    }

    public abstract String typeName();

    public abstract String accept(EntityWriter writer);

    @Override
    public int compareTo(Tree tree) {
        return _key.compareToIgnoreCase(tree._key);
    }

}
