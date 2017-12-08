package evolutiveNPC;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class Application extends JFrame implements KeyListener, MouseListener{

    private static Application application;
    private static Drawer drawer;

    private List<Sprite> plantas;
    private PlayerCharacter player;
    private ControladorDeGeracao herbivoros;
    private ControladorDeGeracao carnivoros;
    private int numeroDeCriaturasVivas;

    private int round;
    private int survived;
    private int killed;
    private int draw;
    private int arbustosConsumidos;
    private int criaturasConsumidas;
    private int ampulheta;
    private int geracaoAtual;

    private Application(){}

    public static Application getInstance(){

        if (application == null) {
            application = new Application();
        }
        return application;
    }

    public void proximoTurno() {

        ampulheta = ConstantesDoJogo.VALOR_INICIAL_AMPULHETA;

        //Mata quem restou
        player.morre();
        herbivoros.getCriaturas().forEach(Criatura::morre);
        carnivoros.getCriaturas().forEach(Criatura::morre);

        //Revive criaturas e randomiza suas posições
        player.revive();
        herbivoros.getCriaturas().forEach(Criatura::revive);
        carnivoros.getCriaturas().forEach(Criatura::revive);

        //Evolui genes das criaturas
        this.herbivoros.atualizaGeracao(geracaoAtual);
        this.carnivoros.atualizaGeracao(geracaoAtual);

        //Remove plantas que restaram
        plantas.forEach(planta -> Sprite.SpriteList.removeAll(plantas));
        this.plantas = new ArrayList<Sprite>();

        boolean aux;
        Coordenada coordenada;
        //Adiciona novas plantas
        for(int i = 0; i < ConstantesDoJogo.QUANTIDADE_DE_PLANTAS; ++i) {

            do {
                aux = false;
                coordenada = new Coordenada(
                        (int) (Math.random() * (ConstantesDoJogo.DIMENSAO_AREA_JOGO - 80)),
                        (int) (Math.random() * (ConstantesDoJogo.DIMENSAO_AREA_JOGO - 80)));

                Sprite planta;

                for (int j = 0; j < plantas.size(); ++j){
                    planta = plantas.get(j);
                    if (Coordenada.calculaDistancia(planta.getCoordenada(), coordenada) <
                            ConstantesDoJogo.DISTANCIA_MINIMA_ENTRE_DUAS_PLANTAS) {
                        aux = true;
                    }
                }

            } while (aux);


            plantas.add(new Sprite(coordenada, ConstantesDoJogo.IMAGE_PATH_PLANTA));
        }

        ++geracaoAtual;
    }

    private void mainLoop(){

        killed= 0;
        draw = 0;
        survived = 0;
        ampulheta = ConstantesDoJogo.VALOR_INICIAL_AMPULHETA;

        player = new PlayerCharacter
                (new Coordenada(
                        ConstantesDoJogo.DIMENSAO_AREA_JOGO/2 - ConstantesDoJogo.DIMENSAO_SPRITE/2,
                        ConstantesDoJogo.DIMENSAO_AREA_JOGO/2 - ConstantesDoJogo.DIMENSAO_SPRITE/2));
        this.plantas = new ArrayList<Sprite>();

        geracaoAtual = 1;
        this.herbivoros = new ControladorDeGeracao(TipoDeCriatura.HERBIVORO);
        this.carnivoros = new ControladorDeGeracao(TipoDeCriatura.CARNIVORO);

        Coordenada coordenada;
        boolean aux = false;

        for(int i = 0; i < ConstantesDoJogo.QUANTIDADE_DE_PLANTAS; ++i) {


            do {
                aux = false;
                coordenada = new Coordenada(
                        (int) (Math.random() * (ConstantesDoJogo.DIMENSAO_AREA_JOGO - 45)),
                        (int) (Math.random() * (ConstantesDoJogo.DIMENSAO_AREA_JOGO - 45)));

                Sprite planta;

                for (int j = 0; j < plantas.size(); ++j){
                    planta = plantas.get(j);
                    if (Coordenada.calculaDistancia(planta.getCoordenada(), coordenada) <
                            ConstantesDoJogo.DISTANCIA_MINIMA_ENTRE_DUAS_PLANTAS) {
                        aux = true;
                    }
                }

            } while (aux);

            plantas.add(new Sprite(coordenada, ConstantesDoJogo.IMAGE_PATH_PLANTA));
        }

        int segundos = 0;

        while(true){

            setTitle("[" + "K:" + killed + " D:" + draw + " S: " + survived +"] " + "Ampulheta: " + ampulheta + " Energia: " + player.getEnergia());
            if (segundos == ConstantesDoJogo.FPS) {

                segundos = 0;

                --ampulheta;

                player.setEnergia(player.getEnergia() -
                        (player.isMorto() ? 0 : ConstantesDoJogo.DECREMENTO_DE_ENERGIA));

                carnivoros.getCriaturas().forEach(carnivoro ->
                        carnivoro.setEnergia(carnivoro.getEnergia() -
                                (carnivoro.isMorto() ? 0 : ConstantesDoJogo.DECREMENTO_DE_ENERGIA)));

                herbivoros.getCriaturas().forEach(herbivoro ->
                        herbivoro.setEnergia(herbivoro.getEnergia() -
                                (herbivoro.isMorto() ? 0 : ConstantesDoJogo.DECREMENTO_DE_ENERGIA)));
            }

            UpdaterList.Update();
            drawer.draw();

            if (player.getSuperInstance().isMorto() || numeroDeCriaturasVivas <= 1 || ampulheta == 0) {

                if (ampulheta == 0 && !player.getSuperInstance().isMorto() && numeroDeCriaturasVivas > 1){
                    ++draw;
                }

                if (player.getSuperInstance().isMorto()){
                    ++killed;
                }

                if (!player.getSuperInstance().isMorto()){
                    ++survived;
                }

                segundos = 0;
                proximoTurno();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                ++segundos;
                Thread.sleep(1000/ ConstantesDoJogo.FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void initialize(){

        numeroDeCriaturasVivas = 0;

        addKeyListener(this);
        addMouseListener(this);
        setSize(ConstantesDoJogo.DIMENSAO_AREA_JOGO,
                ConstantesDoJogo.DIMENSAO_AREA_JOGO);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setLocationRelativeTo(null);

        drawer = new Drawer(getGraphics());
        mainLoop();
        
    }

    public Criatura getCriaturaJogadora() {
        return player.getSuperInstance();
    }

    public List<Criatura> getCriaturasHerbivoras() {
        return herbivoros.getCriaturas();
    }


    public List<Criatura> getCriaturasCarnivoras() {
        return carnivoros.getCriaturas();
    }

    public List<Sprite> getPlantas() {
        return plantas;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        player.KeyPressed(e);
    }
    public void keyReleased(KeyEvent e) {
        player.KeyReleased(e);
    }
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public PlayerCharacter getPlayer() {
        return player;
    }

    public void setPlayer(PlayerCharacter player) {
        this.player = player;
    }

    public int getNumeroDeCriaturasVivas() {
        return numeroDeCriaturasVivas;
    }

    public void setNumeroDeCriaturasVivas(int numeroDeCriaturasVivas) {
        this.numeroDeCriaturasVivas = numeroDeCriaturasVivas;
    }
}
