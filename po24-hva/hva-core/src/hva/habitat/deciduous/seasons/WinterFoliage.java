package hva.habitat.deciduous.seasons;

import hva.habitat.TreeState;

import java.io.Serial;

public class WinterFoliage extends TreeState {
    @Serial
    private static final long serialVersionUID = 202407081733L;

    public WinterFoliage(int age, int seasons) {
        super("SEMFOLHAS", 0, age, seasons);
    }

    @Override
    public TreeState nextSeason() {
        super.nextSeason();
        return new SpringFoliage(_age, _seasons);
    }
}
