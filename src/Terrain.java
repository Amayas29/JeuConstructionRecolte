public class Terrain {

    public static final int NBLIGNESMAX = 20;
    public static final int NBCOLONNESMAX = 20;
    private static final int NBCARAFFICHES = 5;
    public final int nbLignes;
    public final int nbColonnes;
    private Ressource[][] terrain;
    
    public Terrain() {
        this(20, 20);
    }
    
    public Terrain(int nbLignes, int nbColonnes) {
        if (nbLignes > 20)
            nbLignes = 20;
        
        if (nbLignes <= 0)
            nbLignes = 1;
        
        this.nbLignes = nbLignes;
        
        if (nbColonnes > 20)
            nbColonnes = 20;

        if (nbColonnes <= 0)
            nbColonnes = 1;

        this.nbColonnes = nbColonnes;
        terrain = new Ressource[nbLignes][nbColonnes];
    }
    
    public Ressource getCase(int x, int y) {
        if (sontValides(x, y))
            return terrain[x][y];

        return null;
    }
    
    public Ressource videCase(int x, int y) {
        if (sontValides(x, y) && terrain[x][y] != null) {
            Ressource ressource = terrain[x][y];
            ressource.initialisePosition();
            terrain[x][y] = null;
            return ressource;
        }
        return null;
    }
    
    public boolean setCase(int x, int y, Ressource ressource) {
        if (sontValides(x, y)) {

            if (terrain[x][y] != null)
                terrain[x][y].initialisePosition();
            
            ressource.setPosition(x, y);
            terrain[x][y] = ressource;
            return true;
        }
        return false;
    }
    
    public boolean caseEstVide(int x, int y) {
        return !sontValides(x, y) || terrain[x][y] == null;
    }
    
    public boolean sontValides(int x, int y) {
        return x >= 0 && x < nbLignes && y >= 0 && y < nbColonnes;
    }
    
    public void affiche() {
        String hSep = ":";
        String vSep = "";
        
        for (int i = 0; i < 5; ++i)
            vSep += "-";
        
        for (int j = 0; j < nbColonnes; ++j)
            hSep += vSep + ":";
        
        String s = hSep + "\n";
        String str = hSep+ "\n";

        for (int k = 0; k < nbLignes; ++k) {
            for (int l = 0; l < nbColonnes; ++l) {
                if (terrain[k][l] == null)
                    s += "|" + String.format("%-5s", " ");
                
                else 
                    s += "|" + premiersCar(terrain[k][l].type);
                
            }
            s += "|\n" + str;
        }
        System.out.println(s);
    }
    
    @Override
    public String toString() {
        int i = 0;
        for (int j = 0; j < nbLignes; ++j) {
            for (int k = 0; k < nbColonnes; ++k) {
                if (terrain[j][k] != null) {
                    ++i;
                }
            }
        }

        String rep= "Terrain de " + nbLignes + "x" + nbColonnes + " cases: ";
        if (i == 0)
            return rep + "toutes les cases sont libres.";
        
        if (i == 1)
            return rep + "il y a une case occupee.";
        
        return rep + "il y a " + i + " cases occupees.";

    }
    
    private String premiersCar(String s) {
        return String.format("%-" + NBCARAFFICHES + "s", s).substring(0, 5);
    }
}