package evolutiveNPC;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Drawer {

    private Graphics frame;
    private BufferedImage backBuffer;
    private Image backgroundImage ;
    
    Drawer(Graphics frame){

        this.frame = frame;
        this.backBuffer = new BufferedImage(ConstantesDoJogo.DIMENSAO_AREA_JOGO,
                                            ConstantesDoJogo.DIMENSAO_AREA_JOGO,
                                            BufferedImage.TYPE_INT_RGB);
        backgroundImage = new ImageIcon(ConstantesDoJogo.IMAGE_PATH_BACKGROUND).getImage();
    }

    private void DrawBackground(){
        backBuffer.getGraphics().clipRect(0, 0,
                                            ConstantesDoJogo.DIMENSAO_AREA_JOGO,
                                            ConstantesDoJogo.DIMENSAO_AREA_JOGO);

        for (int i = 0; i < ConstantesDoJogo.DIMENSAO_AREA_JOGO/ConstantesDoJogo.DIMENSAO_SPRITE + 1; i++) {
            for (int j = 0; j < ConstantesDoJogo.DIMENSAO_AREA_JOGO/ConstantesDoJogo.DIMENSAO_SPRITE + 1; j++) {

                backBuffer.getGraphics().drawImage(backgroundImage,
                        i * ConstantesDoJogo.DIMENSAO_SPRITE,
                        j * ConstantesDoJogo.DIMENSAO_SPRITE,
                        Application.getInstance());
            }
        }
    }

    public void draw(){
        DrawBackground();
        Sprite.SpriteList.forEach(sprite -> {
            backBuffer
                    .getGraphics()
                    .drawImage(sprite.getImage(),
                            (int) sprite.getCoordenada().getX(),
                            (int) sprite.getCoordenada().getY(),
                            Application.getInstance());
        });
        this.frame.drawImage(backBuffer, 0, 0, Application.getInstance());
    }
}
