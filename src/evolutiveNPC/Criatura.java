package evolutiveNPC;

import java.util.ArrayList;
import java.util.List;

public class Criatura implements Updater {

    private int id;
    private int energia;
    private int velocidade;
    private TipoDeCriatura tipo;
    private int herbcarn;
    private double fitness;
    private int cromossomo[];
    private Coordenada coordenada;
    private Sprite sprite;
    private int proximaDirecaoAleatoria;
    private int numeroDeVezesQueAndouAleatoriamente;
    private boolean morto;
    private long momentoNascimento;

    private static int nextId = 0;

    public Criatura(TipoDeCriatura tipo, Coordenada coordenada) {

        Application.getInstance()
                .setNumeroDeCriaturasVivas(1 + Application.getInstance().getNumeroDeCriaturasVivas());


        momentoNascimento = System.currentTimeMillis();
        fitness = 0;
        UpdaterList.AddUpdater(this);
        randomizaProximaDirecaoAleatoria();

        this.cromossomo = new int[16];
        for (int i = 0; i < 16; i++) {
            this.cromossomo[i] = (int) (Math.random() * 10);
        }

        this.id = nextId++;
        this.coordenada = coordenada;
        this.energia = ConstantesDoJogo.QUANTIDADE_INICIAL_DE_ENERGIA;
        this.velocidade = ConstantesDoJogo.VELOCIDADE_PADRAO;
        this.tipo = tipo;

        switch (tipo) {
            case JOGADOR:
                this.herbcarn = ConstantesDoJogo.VALOR_HERBCARN_JOGADOR;
                this.sprite = new Sprite(this.coordenada,ConstantesDoJogo.IMAGE_PATH_ONIVORO);
                break;
            case CARNIVORO:
                this.herbcarn = ConstantesDoJogo.VALOR_HERBCARN_CARNIVORO;
                this.sprite = new Sprite(this.coordenada, ConstantesDoJogo.IMAGE_PATH_CARNIVORO);
                break;
            case HERBIVORO:
                this.herbcarn = ConstantesDoJogo.VALOR_HERBCARN_HERBIVORO;
                this.sprite = new Sprite(this.coordenada,ConstantesDoJogo.IMAGE_PATH_HERBIVORO);
                break;
            default:
                break;
        }
    }

    Criatura getInstance() {
        return this;
    }

    public void reproduz(int cromossomoPai[]){

        for(int i=0; i < 16; i++){
            if(Math.random() > 0.5){
                this.cromossomo[i] = cromossomoPai[i];
            }
            if(Math.random() < 0.02){
                this.cromossomo[i] = (int)(Math.random() * 10);
            }
        }
    }

    public void morre() {
        if (!morto) {
            fitness = System.currentTimeMillis() - momentoNascimento;
            morto = true;
            Sprite.SpriteList.remove(sprite);

            Application.getInstance()
                    .setNumeroDeCriaturasVivas(
                            Application
                            .getInstance()
                            .getNumeroDeCriaturasVivas() - 1);

        }
    }

    public void revive() {
        if (morto) {

            momentoNascimento = System.currentTimeMillis();
            energia = ConstantesDoJogo.QUANTIDADE_INICIAL_DE_ENERGIA;
            Sprite.SpriteList.add(sprite);
            morto = false;

            if (tipo != TipoDeCriatura.JOGADOR) {
                randomizaPosicao();
            } else {
                coordenada.setX(ConstantesDoJogo.DIMENSAO_AREA_JOGO/2 -
                        ConstantesDoJogo.DIMENSAO_SPRITE/2);
                coordenada.setY(ConstantesDoJogo.DIMENSAO_AREA_JOGO/2 -
                        ConstantesDoJogo.DIMENSAO_SPRITE/2);
            }
            Application.getInstance()
                    .setNumeroDeCriaturasVivas(
                            1 + Application.getInstance().getNumeroDeCriaturasVivas());

        }
    }

    public void randomizaProximaDirecaoAleatoria() {
        numeroDeVezesQueAndouAleatoriamente = 0;
        proximaDirecaoAleatoria = (int) (Math.random() * 8);
    }

    public void anda(double dirx, double diry) {

        if (coordenada.getX() + dirx * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE >
                ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE) {
            dirx = 0;
            coordenada.setX(ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE);
            randomizaProximaDirecaoAleatoria();
        } else if (coordenada.getX() + dirx * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE < 0) {
            dirx = 0;
            coordenada.setX(0);
            randomizaProximaDirecaoAleatoria();
        }

        if (coordenada.getY() + diry * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE >
                ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE) {

            diry = 0;
            coordenada.setY(ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE);
            randomizaProximaDirecaoAleatoria();

        } else if (coordenada.getY() + diry * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE < 0) {
            diry = 0;
            coordenada.setY(0);
            randomizaProximaDirecaoAleatoria();
        }

        coordenada.setX(coordenada.getX() + dirx * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE);
        coordenada.setY(coordenada.getY() + diry * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE);
        if (dirx != 0 || diry != 0)
            energia -= velocidade;

    }

    public void corre(double dirx, double diry) {

        if (coordenada.getX() + dirx * velocidade * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE >
                ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE) {
            dirx = 0;
            coordenada.setX(ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE);
            randomizaProximaDirecaoAleatoria();
        } else if (coordenada.getX() + dirx * velocidade * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE < 0) {
            dirx = 0;
            coordenada.setX(0);
            randomizaProximaDirecaoAleatoria();
        }

        if (coordenada.getY() + diry * velocidade * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE >
                ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE) {
            diry = 0;
            coordenada.setY(ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE);
            randomizaProximaDirecaoAleatoria();
        } else if (coordenada.getY() + diry  * velocidade * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE < 0) {
            diry = 0;
            coordenada.setY(0);
            randomizaProximaDirecaoAleatoria();
        }

        coordenada.setX(coordenada.getX() + dirx * velocidade * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE);
        coordenada.setY(coordenada.getY() + diry * velocidade * ConstantesDoJogo.UNIDADE_DE_VELOCIDADE);


        if (dirx != 0 || diry != 0)
            energia -= 2 * velocidade;
    }

    public void persegue(Coordenada coordenada) {
        if (Coordenada.calculaDistancia(coordenada, this.coordenada) > ConstantesDoJogo.DIMENSAO_SPRITE) {
            Coordenada vetorDirecao = Coordenada.subtrai(this.coordenada, coordenada);
            if (vetorDirecao.getX() != 0 || vetorDirecao.getY() != 0) {
                Coordenada.normalize(vetorDirecao);
                corre(vetorDirecao.getX() * -1, vetorDirecao.getY() * -1);
            }
        }

    }

    public void foge(Coordenada coordenada) {

        Coordenada vetorDirecao = Coordenada.subtrai(this.coordenada, coordenada);
        if (vetorDirecao.getX() != 0 || vetorDirecao.getY() != 0) {
            Coordenada.normalize(vetorDirecao);
            corre(vetorDirecao.getX(), vetorDirecao.getY());
        }
    }

    public void andaAleatoriamente() {

        if (numeroDeVezesQueAndouAleatoriamente >=
                ConstantesDoJogo.NUMERO_MAX_DE_VEZES_NA_MESMA_DIRECAO){
            randomizaProximaDirecaoAleatoria();
        }

        ++numeroDeVezesQueAndouAleatoriamente;

        switch (proximaDirecaoAleatoria) {
            case 0:
                anda(1, 0);
                break;
            case 1:
                anda(-1, 0);
                break;
            case 2:
                anda(0, 1);
                break;
            case 3:
                anda(0, -1);
                break;
            case 4:
                anda(1, 1);
                break;
            case 5:
                anda(-1, 1);
                break;
            case 6:
                anda(1, -1);
                break;
            case 7:
                anda(-1, -1);
                break;
            default:break;
        }
    }


    public void correAleatoriamente() {

        if (numeroDeVezesQueAndouAleatoriamente >=
                ConstantesDoJogo.NUMERO_MAX_DE_VEZES_NA_MESMA_DIRECAO){
            randomizaProximaDirecaoAleatoria();
        }

        ++numeroDeVezesQueAndouAleatoriamente;

        switch (proximaDirecaoAleatoria) {
            case 0:
                corre(1, 0);
                break;
            case 1:
                corre(-1, 0);
                break;
            case 2:
                corre(0, 1);
                break;
            case 3:
                corre(0, -1);
                break;
            case 4:
                corre(1, 1);
                break;
            case 5:
                corre(-1, 1);
                break;
            case 6:
                corre(1, -1);
                break;
            case 7:
                corre(-1, -1);
                break;
            default:break;
        }
    }

    public void comeCriatura() {
        this.energia += (6 - this.herbcarn) * 150;
    }

    public void comePlanta() {
        this.energia += this.herbcarn * 100;
    }

    public void decideAcao(List<Criatura> criaturas, List<Coordenada> coordenadasDaPlantas) {

        Boolean temPlantaPorPerto = false;
        Boolean temHerbivoroPorPerto = false;
        Boolean temCarnivoroPorPerto = false;
        Boolean temJogadorPorPerto = false;

        Coordenada jogador = Application.getInstance().getPlayer().getCoordenada();
        List<Criatura> carnivorosVisiveis = new ArrayList<>();
        List<Criatura> herbivorosVisiveis = new ArrayList<>();
        List<Coordenada> coordenadasDasPlantasVisiveis = new ArrayList<>();

        int indiceDoGene = 0;

        //Define criaturas que estão na área de visão
        for(Criatura criatura: criaturas) {

            if (!criatura.isMorto() && criatura.id != this.id &&
                    checaColisao(criatura.getCoordenada(), ConstantesDoJogo.AREA_DE_VISAO)) {

                switch (criatura.getTipo()) {

                    case CARNIVORO:

                        carnivorosVisiveis.add(criatura);
                        if (!temCarnivoroPorPerto) {
                            indiceDoGene += ConstantesDoJogo.PRESENCA_CARNIVORO;
                            temCarnivoroPorPerto = true;
                        }
                        break;

                    case HERBIVORO:

                        herbivorosVisiveis.add(criatura);
                        if (!temHerbivoroPorPerto) {
                            indiceDoGene += ConstantesDoJogo.PRESENCA_HERBIVORO;
                            temHerbivoroPorPerto = true;
                        }
                        break;

                    case JOGADOR:

                        indiceDoGene += ConstantesDoJogo.PRESENCA_JOGADOR;
                        temJogadorPorPerto = true;
                        break;
                }
            }
        }

        //Define plantas que estão na área de visão
        for(Coordenada coordenada: coordenadasDaPlantas) {

            if (checaColisao(coordenada, ConstantesDoJogo.AREA_DE_VISAO)) {

                coordenadasDasPlantasVisiveis.add(coordenada);
                if (!temPlantaPorPerto) {
                    indiceDoGene += ConstantesDoJogo.PRESENCA_PLANTA;
                    temPlantaPorPerto = true;
                }
            }
        }

        Coordenada carnivoroMaisProximo = null;
        Coordenada herbivoroMaisProximo = null;
        Coordenada plantaMaisProxima = null;

        //Dentre os carnivoros visiveis, define o mais proximo
        double distanciaMaisProxima = -1;

        for(Criatura carnivoro: Application.getInstance().getCriaturasCarnivoras()) {
            if (carnivoro.getId() != id) {
                double distancia = Coordenada.calculaDistancia(coordenada, carnivoro.getCoordenada());
                if(distancia < distanciaMaisProxima || distanciaMaisProxima == -1) {
                    carnivoroMaisProximo = carnivoro.getCoordenada();
                    distanciaMaisProxima = distancia;
                }
            }
        }

        distanciaMaisProxima = -1;
        for (Criatura herbivoro: Application.getInstance().getCriaturasHerbivoras()) {
            if (herbivoro.getId() != id) {
                double distancia = Coordenada.calculaDistancia(coordenada, herbivoro.getCoordenada());
                if(distancia < distanciaMaisProxima || distanciaMaisProxima == -1) {
                    herbivoroMaisProximo = herbivoro.getCoordenada();
                    distanciaMaisProxima = distancia;
                }
            }
        }

        //Dentre as plantas visiveis, define a mais proxima
        distanciaMaisProxima = -1;
        for(Coordenada planta: coordenadasDaPlantas){
            double distancia = Coordenada.calculaDistancia(coordenada, planta);
            if(distancia < distanciaMaisProxima || distanciaMaisProxima == -1) {
                plantaMaisProxima = planta;
                distanciaMaisProxima = distancia;
            }
        }

        tomaAcao(cromossomo[indiceDoGene], carnivoroMaisProximo, herbivoroMaisProximo,
                plantaMaisProxima, Application.getInstance().getPlayer().getCoordenada());

    }

    Boolean checaColisao(Coordenada objeto, int raio) {

        Coordenada centro = new Coordenada(
                coordenada.getX() + ConstantesDoJogo.DIMENSAO_SPRITE / 2,
                coordenada.getY() + ConstantesDoJogo.DIMENSAO_SPRITE / 2);

        Coordenada centroObjeto = new Coordenada(
                objeto.getX() + ConstantesDoJogo.DIMENSAO_SPRITE / 2,
                objeto.getY() + ConstantesDoJogo.DIMENSAO_SPRITE / 2);

        return Coordenada.calculaDistancia(centroObjeto, centro) < raio;

    }

    public void tomaAcao(int acao, Coordenada carnivoroMaisProximo, Coordenada herbivoroMaisProximo,
                         Coordenada plantaMaisProxima, Coordenada jogador) {

        switch (acao){

            case 0:
                andaAleatoriamente();
                break;
            case 1:
                correAleatoriamente();
                break;
            case 2:
                randomizaProximaDirecaoAleatoria();
                if (plantaMaisProxima != null){
                    persegue(plantaMaisProxima);
                }
                break;
            case 3:
                randomizaProximaDirecaoAleatoria();
                if (plantaMaisProxima != null){
                    foge(plantaMaisProxima);
                }
                break;
            case 4:
                randomizaProximaDirecaoAleatoria();
                if (herbivoroMaisProximo != null) {
                    persegue(herbivoroMaisProximo);

                }
                break;
            case 5:
                randomizaProximaDirecaoAleatoria();
                if (herbivoroMaisProximo != null) {
                    foge(herbivoroMaisProximo);
                }
                break;
            case 6:
                randomizaProximaDirecaoAleatoria();
                if (carnivoroMaisProximo != null) {
                    persegue(carnivoroMaisProximo);
                }
                break;
            case 7:
                randomizaProximaDirecaoAleatoria();
                if (carnivoroMaisProximo != null) {
                    foge(carnivoroMaisProximo);
                }
                break;
            case 8:
                randomizaProximaDirecaoAleatoria();
                if (jogador != null) {
                    persegue(jogador);
                }
                break;
            case 9:
                randomizaProximaDirecaoAleatoria();
                if (jogador != null){
                    foge(jogador);
                }
                break;
        }
    }

    void randomizaPosicao() {

        int x, y;

        do {

            x = (int) (Math.random() * (ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE));
            y = (int) (Math.random() * (ConstantesDoJogo.DIMENSAO_AREA_JOGO - ConstantesDoJogo.DIMENSAO_SPRITE));

            coordenada.setX(x);
            coordenada.setY(y);

        } while (id != Application.getInstance().getPlayer().getId() &&
                checaColisao(Application.getInstance().getPlayer().getCoordenada(), ConstantesDoJogo.AREA_DE_VISAO));

    }

    @Override
    public void Update() {

            if (!morto) {

                if (energia <= 0) {
                    energia = 0;
                    morre();
                }

                Application application = Application.getInstance();

                //Pega instâncias de todas criaturas
                List<Criatura> carnivoros = application.getCriaturasCarnivoras();
                List<Criatura> herbivoros = application.getCriaturasHerbivoras();
                Criatura jogador = application.getCriaturaJogadora();
                //Pega instância de todas as plantas
                List<Coordenada> coordenadasDasPlantas = new ArrayList<>();
                List<Sprite> plantas = application.getPlantas();
                plantas.forEach(planta -> coordenadasDasPlantas.add(planta.getCoordenada()));


                if (tipo == TipoDeCriatura.CARNIVORO) {

                    //Trata caso em que colidiu com alguma criatura
                    List<Criatura> criaturasComidas = new ArrayList<>();
                    herbivoros.forEach(herbivoro -> {
                        if (!herbivoro.isMorto() && checaColisao(herbivoro.getCoordenada(),ConstantesDoJogo.DIMENSAO_SPRITE)) {
                            criaturasComidas.add(herbivoro);
                            comeCriatura();
                        }
                    });
                    //Trata caso em que colidiu com o jogador
                    if (!jogador.isMorto() && checaColisao(jogador.getCoordenada(), ConstantesDoJogo.DIMENSAO_SPRITE)) {
                        criaturasComidas.add(jogador);
                        comeCriatura();
                    }
                    //Mata criaturas comidas
                    criaturasComidas.forEach(Criatura::morre);
                }


                List<Sprite> plantasComidas = new ArrayList<>();
                if (tipo == TipoDeCriatura.HERBIVORO) {

                    //Trata caso em que colidiu com alguma planta
                    for (Sprite planta : plantas) {
                        if (checaColisao(planta.getCoordenada(), ConstantesDoJogo.DIMENSAO_SPRITE)) {
                            plantasComidas.add(planta);
                            comePlanta();
                        }
                    }
                    Application.getInstance().getPlantas().removeAll(plantasComidas);
                    Sprite.SpriteList.removeAll(plantasComidas);
                    //Trata caso em que colidiu com jogador
                    if (!jogador.isMorto() && checaColisao(jogador.getCoordenada(), ConstantesDoJogo.DIMENSAO_SPRITE)) {
                        jogador.comeCriatura();
                        morre();
                    }
                }


                List<Criatura> criaturas = new ArrayList<>();

                criaturas.addAll(carnivoros);
                criaturas.addAll(herbivoros);
                criaturas.add(jogador);

                //Decide e toma ação dado estado atual do jogo
                decideAcao(criaturas, coordenadasDasPlantas);
            }

    }

    public boolean isMorto() {
        return morto;
    }

    public void setMorto(boolean morto) {
        this.morto = morto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnergia() {
        return energia;
    }

    public void setEnergia(int energia) {
        this.energia = energia;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public TipoDeCriatura getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeCriatura tipo) {
        this.tipo = tipo;
    }

    public int getHerbcarn() {
        return herbcarn;
    }

    public void setHerbcarn(int herbcarn) {
        this.herbcarn = herbcarn;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int[] getCromossomo() {
        return cromossomo;
    }

    public void setCromossomo(int[] cromossomo) {
        this.cromossomo = cromossomo;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getProximaDirecaoAleatoria() {
        return proximaDirecaoAleatoria;
    }

    public void setProximaDirecaoAleatoria(int proximaDirecaoAleatoria) {
        this.proximaDirecaoAleatoria = proximaDirecaoAleatoria;
    }

    public int getNumeroDeVezesQueAndouAleatoriamente() {
        return numeroDeVezesQueAndouAleatoriamente;
    }

    public void setNumeroDeVezesQueAndouAleatoriamente(int numeroDeVezesQueAndouAleatoriamente) {
        this.numeroDeVezesQueAndouAleatoriamente = numeroDeVezesQueAndouAleatoriamente;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Criatura.nextId = nextId;
    }
}
