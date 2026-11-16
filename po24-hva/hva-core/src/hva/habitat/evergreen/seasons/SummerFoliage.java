package hva.habitat.evergreen.seasons;

import hva.habitat.TreeState;

import java.io.Serial;

public class SummerFoliage extends TreeState {
    @Serial
    private static final long serialVersionUID = 202407081733L;

    public SummerFoliage(int age, int seasons) {
        super("COMFOLHAS", 1, age, seasons);
    }

    @Override
    public TreeState nextSeason() {
        super.nextSeason();
        return new AutumnFoliage(_age, _seasons);
    }
}
