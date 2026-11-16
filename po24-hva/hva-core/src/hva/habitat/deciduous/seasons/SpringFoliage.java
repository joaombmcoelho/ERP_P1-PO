package hva.habitat.deciduous.seasons;

import hva.habitat.TreeState;

import java.io.Serial;

public class SpringFoliage extends TreeState {
    @Serial
    private static final long serialVersionUID = 202407081733L;

    public SpringFoliage(int age, int seasons) {
        super("GERARFOLHAS", 1, age, seasons);
    }

    @Override
    public TreeState nextSeason() {
        super.nextSeason();
        return new SummerFoliage(_age, _seasons);
    }
}
