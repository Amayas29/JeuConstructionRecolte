/**    
* @author Amayas SADI
* @author Hamid KOLLI
*
* Cette classe permet de gérer les agents en général
*/
public abstract class Agent {
    
    /**
    * L'energie minimale de tous les agents
    */
    public static final int MIN_ENERGIE = 5;
    
    /**
    * Un identifiant unique qui permet d'identifier chaque agent
    */
    public final int id;
   
    /**
    * L'energie restante d'un agent, sa valeur par defaut dépend du type d'agent
    */
    protected int energie;

    /**
    * L'ordonnée de la position de l'agent
    */
    protected int x;

    /**
    * L'abscisse de la position de l'agent
    */
    protected int y;

    /**
    * Constructeur d'agent
    *
    * @param id : l'identifiant de l'agent
    * @param energie : l'energie de l'agent
    * @param x : l'ordonnée de la position
    * @param y : l'abscisse de la position
    */
    public Agent(int id, int energie, int x, int y) {
        
        this.id = id;
        
        this.energie = energie <= 0 ? MIN_ENERGIE : energie; 
        
        this.x = x < 0 ? 0 : x;
        this.y = y < 0 ? 0 : y;
    }

    /**
    * Constructeur par copie
    *
    * @param agent : l'objet agent a cloner
    * @param id : l'identifiant de l'agent
    */
    protected Agent(Agent agent ,int id){
        
        if(agent != null) {
           
            energie = agent.energie;

            x = agent.x;
            y = agent.y;
        }
        this.id = id;
    }
    
    /**
    * Calcule la distance entre la position de l'agent et un point donnée
    *
    * @param x : l'ordonnée du point
    * @param y : l'abscisse du point
    *
    * @return la distance entre la position de l'agent et le point donnée
    */
    public final double distance(int x,int y) {
        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }

    /**
    * Permet de déplacer l'agent vers un autre point
    *
    * @param xnew : l'ordonnée du point
    * @param ynew : l'abscisse du point
    */
    public final void seDeplacer(int xnew,int ynew) {
        this.x = xnew;
        this.y = ynew;
    }

    /**
    * Permet de savoir si l'agent peut travailler
    *   
    * @return energie de l'agent > l'energie min
    */
    public final boolean peutTravailler() {
        return energie >= MIN_ENERGIE;
    }


    /**
    * Accesseur pour modifier l'energie de l'agent
    *
    * @param energie : la nouvelle energie de l'agent
    * @param maxEnergie : la valeur de max de l'energie  
    */
    protected void setEnergie(int energie, int maxEnergie) {
        
        if(energie > maxEnergie) 
            this.energie = maxEnergie;

        else if (energie < MIN_ENERGIE)
            this.energie = MIN_ENERGIE;

        else
            this.energie = energie;
    }

    /**
    * Accesseur pour modifier l'ordonnée
    * 
    * @param x : la nouvelle ordonne 
    * @param xMax : la valeur max pour l'odronné
    */
    public void setX(int x, int xMax) {
        if(x < 0)
            this.x = 0;

        else if(x >= xMax)
            this.x = xMax - 1;

        else
            this.x = x;
    }

    /**
    * Accesseur pour modifier l'abscisse
    * 
    * @param y : la nouvelle abscisse 
    * @param yMax : la valeur max pour l'abscisse
    */
    public void setY(int y, int yMax) {

        if(y < 0)
            this.y = 0;

        else if(y >= yMax)
            this.y = yMax - 1;

        else
            this.y = y;
    }

    /**
    * Accesseur de l'ordonnée de l'agent
    *
    * @return l'ordonnée de l'agent
    */
    public int getX() {
        return x;
    }

    /**
    * Accesseur de l'abscisse de l'agent
    *
    * @return l'abscisse de l'agent
    */
    public int getY() {
        return y;
    }

    /**
    * Accesseur de l'energie de l'agent
    *
    * @return l'energie de l'agent
    */
    public int getEnergie() {
        return energie;
    }

    /**
    * Permet de faire travailler l'agent
    *
    * @param terrain : le terrain ou l'agent travaille
    */
    public abstract void travailler(Terrain terrain);
}