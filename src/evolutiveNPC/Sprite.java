package evolutiveNPC;

import java.awt.Image;
import java.util.*;
import javax.swing.ImageIcon;

public class Sprite {

    private Coordenada coordenada;
    private Image image;
    public static List<Sprite> SpriteList;
    
    static{
        SpriteList = new ArrayList<>();
    }

    Sprite(Coordenada coordenada, String imagem){
        this.image = new ImageIcon(imagem).getImage();
        this.coordenada = coordenada;
        Sprite.SpriteList.add(this);
    }
    
    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
}
