/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutiveAlgorithmInGameNPC;

import java.util.Arrays;

/**
 *
 * @author tiago
 */
public class Dna {
    public int cromossomos[] = new int[16];
    private float FitnessPais[] = new float[5];
    private int GeracaoMaisAntiga;
    public float Fitness;
    
    Dna(){
        for(int i=0;i<16;i++){
            this.cromossomos[i] = (int)(Math.random()*2);
        }
        GeracaoMaisAntiga = 0;
        Arrays.fill(FitnessPais, -1);
    }
    Dna(int cromossomos[],float FitnessPais[],int GeracaoMaisAntiga){
        this.cromossomos = cromossomos;
        this.FitnessPais = FitnessPais;
        this.GeracaoMaisAntiga = GeracaoMaisAntiga;
    }
    
    public void morrer(float fitness){
        this.FitnessPais[GeracaoMaisAntiga] = fitness;
        this.GeracaoMaisAntiga++;
        if(GeracaoMaisAntiga > 4)GeracaoMaisAntiga = 0;
        
        int quantidade = 0;
        for (int i = 0; i < 5; i++) {
            if(this.FitnessPais[i] !=-1){
                this.Fitness += this.FitnessPais[i];
                quantidade++;
            }
        }
        this.Fitness /= quantidade;
    }
    public void PrintDna(){
        for(int i=0;i<16;i++){
            System.out.println(this.cromossomos[i]);
        }
    }
    public void reproduzir(Dna pai){
        for(int i=0;i<16;i++){
            if(Math.random() > 0.5){
                this.cromossomos[i] = pai.cromossomos[i];
            }
            if(Math.random() < 0.02){
                this.cromossomos[i] = (int)(Math.random()*2);
            }
        }
    }
}
