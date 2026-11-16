package hva.habitat.evergreen;

import hva.EntityWriter;
import hva.Season;
import hva.habitat.Tree;
import hva.habitat.evergreen.seasons.AutumnFoliage;
import hva.habitat.evergreen.seasons.SpringFoliage;
import hva.habitat.evergreen.seasons.SummerFoliage;
import hva.habitat.evergreen.seasons.WinterFoliage;

import java.io.Serial;

public class EvergreenTree extends Tree {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    public EvergreenTree(String key, String name, double baseUpkeep, int initialAge, Season season) {
        super(key, name, baseUpkeep, switch (season) {
            case SPRING -> new SpringFoliage(initialAge, 0);
            case SUMMER -> new SummerFoliage(initialAge, 0);
            case AUTUMN -> new AutumnFoliage(initialAge, 0);
            case WINTER -> new WinterFoliage(initialAge, 0);
        });
    }

    @Override
    public String typeName() {
        return "PERENE";
    }

    public String accept(EntityWriter w) {
        return w.writeEvergreenTree(this);
    }

}
