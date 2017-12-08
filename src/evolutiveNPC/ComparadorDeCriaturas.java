package evolutiveNPC;

import java.util.Comparator;

public class ComparadorDeCriaturas implements Comparator<Criatura> {

    public int compare(Criatura a, Criatura b) {

        if (a.getFitness() > b.getFitness() || (a.getFitness() == b.getFitness() && a.getEnergia() > b.getEnergia())) {

            return 1;
        }

        if (a.getFitness() == b.getFitness()) {
            return 0;
        }

        return -1;
    }

}