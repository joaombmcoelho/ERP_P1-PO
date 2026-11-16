package hva.habitat;

public enum HabitatInfluence {
    NEG(-20),
    NEU(0),
    POS(20);

    private final double _value;

    HabitatInfluence(double value) {
        _value = value;
    }

    public double value() {
        return _value;
    }
}
