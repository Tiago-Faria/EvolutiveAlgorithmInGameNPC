/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutiveAlgorithmInGameNPC;

/**
 *
 * @author tiago
 */
public class ControladorEvolucao {
    public Criatura Criaturas[] = new Criatura[100];

    
    private int DecidirPai(){
        int melhor = 0;
        for (int i = 1; i < 100; i++) {
            if(Criaturas[i].dna.Fitness > Criaturas[melhor].dna.Fitness){
                melhor = i;
            }
            else if(Criaturas[i].dna.Fitness == Criaturas[melhor].dna.Fitness &&
                    Criaturas[i].energia > Criaturas[melhor].energia){
                melhor = i;
            }
        }
        return melhor;
    }
    
    public void Evoluir(){
        int pai = DecidirPai();
        
        for (int i = 0; i < 100; i++) {
            if(i != pai){
                Criaturas[i].dna.reproduzir(Criaturas[pai].dna);
            }
        }
    }
    
}
