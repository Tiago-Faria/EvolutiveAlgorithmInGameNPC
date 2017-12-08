package evolutiveNPC;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayerCharacter extends Criatura {

    private boolean control;
    private int ButtonUp;
    private int ButtonDown;
    private int ButtonLeft;
    private int ButtonRight;
    
    public PlayerCharacter(Coordenada coordenada) {
        super(TipoDeCriatura.JOGADOR, coordenada);
    }
    

    void KeyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_CONTROL:
                this.control = true;
                break;
            case KeyEvent.VK_UP:
                this.ButtonUp = 1;
                break;
            case KeyEvent.VK_DOWN:
                this.ButtonDown = 1;
                break;
            case KeyEvent.VK_LEFT:
                this.ButtonLeft = 1;
                break;
            case KeyEvent.VK_RIGHT:
                this.ButtonRight = 1;
                break;
        }
    }

    public void KeyReleased(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_CONTROL:
                this.control = false;
                break;
            case KeyEvent.VK_UP:
                this.ButtonUp = 0;
                break;
            case KeyEvent.VK_DOWN:
                this.ButtonDown = 0;
                break;
            case KeyEvent.VK_LEFT:
                this.ButtonLeft = 0;
                break;
            case KeyEvent.VK_RIGHT:
                this.ButtonRight = 0;
                break;
        }
    }

    public Criatura getSuperInstance() {
        return super.getInstance();
    }

    @Override
    public void Update(){

        List<Sprite> plantasComidas = new ArrayList<>();

        if (!super.isMorto()) {


            if (super.getEnergia() <= 0) {
                morre();
            }

            //Trata caso em que colidiu com alguma planta
            for (Sprite planta : Application.getInstance().getPlantas()) {
                if (super.checaColisao(planta.getCoordenada(), ConstantesDoJogo.DIMENSAO_SPRITE)) {
                    plantasComidas.add(planta);
                    super.comePlanta();
                }
            }
            Application.getInstance().getPlantas().removeAll(plantasComidas);
            Sprite.SpriteList.removeAll(plantasComidas);

            Coordenada direcao = new Coordenada(ButtonRight - ButtonLeft, ButtonDown - ButtonUp);
            if (control)
                super.corre(direcao.getX(), direcao.getY());
            else
                super.anda(direcao.getX(), direcao.getY());
        }
    }
    
    
}
