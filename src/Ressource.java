public class Ressource {

    private static int nbRessourcesCreees = 0;
    public final int ident;
    public final String type;
    private int quantite;
    private int x;
    private int y;
    
    public Ressource(final String type, final int quantite) {
        this.type = type;
        this.quantite = quantite;
        ident = nbRessourcesCreees++;
        x = -1;
        y = -1;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(final int quantite) {
        this.quantite = quantite;
    }
    
    public void setPosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public void initialisePosition() {
        this.x = -1;
        this.y = -1;
    }
    
    @Override
    public String toString() {
        String str = type + "[id:" + ident + " quantite: " + quantite + "] ";

        if (x == -1 || y == -1)
            return str + " n'est pas sur le terrain.";
        
        return str + " en position (" + this.x + ", " + this.y + ")";
    }
}
