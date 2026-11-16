package hva.habitat.evergreen.seasons;

import hva.habitat.TreeState;

import java.io.Serial;

public class AutumnFoliage extends TreeState {
    @Serial
    private static final long serialVersionUID = 202407081733L;

    public AutumnFoliage(int age, int seasons) {
        super("COMFOLHAS", 1, age, seasons);
    }

    @Override
    public TreeState nextSeason() {
        super.nextSeason();
        return new WinterFoliage(_age, _seasons);
    }
}
