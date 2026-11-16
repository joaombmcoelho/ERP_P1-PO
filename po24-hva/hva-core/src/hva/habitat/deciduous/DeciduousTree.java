package hva.habitat.deciduous;

import hva.EntityWriter;
import hva.Season;
import hva.habitat.Tree;
import hva.habitat.deciduous.seasons.AutumnFoliage;
import hva.habitat.deciduous.seasons.SpringFoliage;
import hva.habitat.deciduous.seasons.SummerFoliage;
import hva.habitat.deciduous.seasons.WinterFoliage;

import java.io.Serial;

public class DeciduousTree extends Tree {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    public DeciduousTree(String key, String name, double baseUpkeep, int initialAge, Season season) {
        super(key, name, baseUpkeep, switch (season) {
            case SPRING -> new SpringFoliage(initialAge, 0);
            case SUMMER -> new SummerFoliage(initialAge, 0);
            case AUTUMN -> new AutumnFoliage(initialAge, 0);
            case WINTER -> new WinterFoliage(initialAge, 0);
        });
    }

    @Override
    public String typeName() {
        return "CADUCA";
    }

    public String accept(EntityWriter w) {
        return w.writeDeciduousTree(this);
    }

}
