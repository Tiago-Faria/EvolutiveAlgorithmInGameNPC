package evolutiveNPC;

import java.util.*;
import java.util.stream.Collectors;


public class ControladorDeGeracao {

    private List<Criatura> criaturas;
    private Criatura sintetizada;

    public ControladorDeGeracao(TipoDeCriatura tipoDeCriatura) {

        criaturas = new ArrayList<>();

        for (int i = 0; i < ConstantesDoJogo.TAMANHO_INICIAL_DA_POPULACAO; ++i) {

            Criatura criatura = new Criatura(tipoDeCriatura, new Coordenada(0, 0));
            criatura.randomizaPosicao();
            criaturas.add(criatura);
        }

    }

    public void atualizaGeracao(int geracaoAtual) {

        if (geracaoAtual % 20 != 0) {
            randomizaCriatura();
        } else {
            sintetizaCriatura();
        }

        evoluiCriaturas();
    }

    private void randomizaCriatura() {

        Criatura piorCriatura;
        if (sintetizada == null) {
            piorCriatura = Collections.min(new ArrayList<>(criaturas), new ComparadorDeCriaturas());
        }else{
            piorCriatura = Collections.min(criaturas.stream()
                    .filter(criatura -> criatura.getId() != sintetizada.getId())
                    .collect(Collectors.toList()), new ComparadorDeCriaturas());
        }
        for (int i = 0; i < 16; ++i) {
            piorCriatura.getCromossomo()[i] = (int)(Math.random() * 10);
        }

    }

    private void sintetizaCriatura() {
        Criatura piorCriatura;
        if (sintetizada == null) {
            piorCriatura = Collections.min(new ArrayList<>(criaturas), new ComparadorDeCriaturas());
        }else{
            piorCriatura = Collections.min(criaturas.stream()
                    .filter(criatura -> criatura.getId() != sintetizada.getId())
                    .collect(Collectors.toList()), new ComparadorDeCriaturas());
        }

        sintetizada = piorCriatura;

        int[] frequenciaGene = new int[10];

        for (int i = 0; i < 16; ++i) {

            Arrays.fill(frequenciaGene, 0);
            for (Criatura criatura : criaturas) {
                frequenciaGene[criatura.getCromossomo()[i]]++;
            }
            piorCriatura.getCromossomo()[i] = findMaxIndex(frequenciaGene);
        }


    }

    private void evoluiCriaturas(){

        Criatura pai = Collections.max(criaturas, new ComparadorDeCriaturas());
        criaturas.forEach(criatura -> {
            if (criatura.getId() != pai.getId()) {
                criatura.reproduz(pai.getCromossomo());
            }
        });

    }

    private int findMaxIndex(int[] arr) {
        int max = arr[0];
        int maxIdx = 0;
        for(int i = 1; i < arr.length; i++) {
            if(arr[i] > max) {
                max = arr[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }

    public List<Criatura> getCriaturas() {
        return criaturas;
    }

}
