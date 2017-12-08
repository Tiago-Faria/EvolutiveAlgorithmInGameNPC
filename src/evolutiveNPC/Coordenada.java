package evolutiveNPC;

public class Coordenada {

    private double x;
    private double y;

    public Coordenada(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static void normalize(Coordenada c) {

        double modulo = Math.sqrt(Math.pow(c.getX(), 2) + Math.pow(c.getY(), 2));
        c.setX(c.getX() / modulo);
        c.setY(c.getY() / modulo);
    }

    public static double calculaDistancia(Coordenada c1, Coordenada c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
    }

    public static Coordenada subtrai(Coordenada c1, Coordenada c2) {
        return new Coordenada(c1.getX() - c2.getX(), c1.getY() - c2.getY());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
