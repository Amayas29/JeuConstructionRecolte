import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

/**
* @author Amayas SADI
* @author HAmid KOLLI
*
* Cette classe est une classe outils des différents événements
*/
public final class Evenement {
	
	/**	
	* La liste des constructions affecteés par un événement
	*/
	private static ArrayList<Construction> constructionsSaccagees = new ArrayList<Construction>();
	
	/**	
	* La liste des ressources affecteés par un événement
	*/
	private static ArrayList<Ressource> ressourcesSaccagees = new ArrayList<Ressource>();

	/**
	* Bloque le constructeur
	* <p> Pour ne pas instancier de cette classe c'est une classe utilitaire </p>
	*/
	private Evenement() {}

	/**
	* La probabilite qu'un agent se duplique
	*/
	public static final double PROBA_AGENTS = 0.01;

	/**
	* Permet de dupliquer les agents selon une probabilite 
	* @see PROBA_AGENTS
	*
	* @param simulation : la simulation du jeu
	*
	* @return si la dupliquation a ete effectuee
	*/
	public static boolean duplicateAgents(Simulation simulation) {
       
		ArrayList<Agent> agents = simulation.getAgents();

		// Si la liste des agents est vide ou n'existe pas 
		if(agents == null || agents.isEmpty())
			return false;

		boolean duplic = false;

		// On parcours les agents
		for(int i = 0;i < agents.size(); i++) {
			
			Agent agent = agents.get(i);
			
			// Si la probabilite est verifier on duplique l'agent
			if(Math.random() < PROBA_AGENTS) {

				duplic = true;

				if(agent.getClass().getName().equals("Recolteur"))
					agents.add(new Recolteur((Recolteur)agent));

				else if(agent.getClass().getName().equals("Planteur"))
					agents.add(new Planteur((Planteur)agent));

				else
					duplic = false;
			}
		}

		return duplic;
	}

	/**
	* Permet de generer des nouvelle ressources de fer et de pierre sur le terrain
	*
	* @param simulation : la simulation du jeu 
	*/
	public static void generer(Simulation simulation){

		// Si la simulation n'existe pas on fait rien
        if(simulation == null)
            return;

       Terrain terrain = simulation.getTerrain();
       
       // On chercher la premiere case vide
       int i = -1,j = -1;
       for(i = 0; i < terrain.nbLignes; i++)
       	for(j = 0; j < terrain.nbColonnes; j++)
       		if(terrain.getCase(i, j) == null)
       			break;
  
  		// Si aucune case vide existe on fait rien
  		if(i == terrain.nbLignes && j == terrain.nbColonnes)
  			return;

  		// Sinon on cree une ressource (soit fer soit pierre)
        Ressource ressource;

        if(Math.random() <= 0.5) 
        	ressource = new Pierre();
        else
        	ressource = new Fer();

        // Et on l'ajout a la liste des ressources et au terrain
        simulation.getRessources().add(ressource);
        terrain.setCase(i, j, ressource);
    }

    /**
    * Permet de regenerer la quantite d'une ressource
    *
    * @param ressource : la ressource
    */
	public static void regenerer(Ressource ressource) {
		
		// Si la ressource n'existe pas on fait rien
		if(ressource == null)
			return;

		// Sinon on regenere une quantite qui sera inferieur au max de son type
		Random rand = new Random();
		int maxQte;

		if(ressource.type.equals("Bois")) 
			maxQte = Bois.MAX_BOIS_ARBRE;
		
		else if(ressource.type.equals("Pierre")) 
			maxQte = Pierre.MAX_PIERRE_CARRIERE;

		else if(ressource.type.equals("Fer"))
			maxQte = Fer.MAX_FER_MINE;
		
		else 
			maxQte = 5;
		
		// On change la quantite de la ressource avec une nouvelle quantite entre la quantite courante et le max
		ressource.setQuantite(ressource.getQuantite() + rand.nextInt(maxQte - ressource.getQuantite()));
	}

	/**
	* Le nombre des événements liés aux constructions
	*/
	private static final int NOMBRE_EVENTS_CONSTRUCTIONS = 4;

	/**
	* Le nombre des événements liés au ressources
	*/
	private static final int NOMBRE_EVENTS_RESSOURCES = 3;

	/**
	* Evenement de construction : Tremblement de terre
	*/
	public static final int CATASTROPHE_NATURELLE_TREMBLEMENT_DE_TERRE = 1;

	/**
	* Evenement de construction : Inondation
	*/
    public static final int CATASTROPHE_NATURELLE_INONDATION = 2;

    /**
	* Evenement de construction : Incendie
	*/
    public static final int INCENDIE = 3;

    /**
	* Evenement de construction : Saturation des hopitaux
	*/
    public static final int SATURATION_HOPITAUX = 4;

    /**
	* Evenement de ressource : Feu de bois
	*/
    public static final int FEU_DE_BOIS = 5;
    
    /**
	* Evenement de ressource : Attaque des animaux
	*/
    public static final int ATTAQUE_ANIMAUX = 6;
    
	/**
	* Evenement de ressource : Vole
	*/
    public static final int VOLE = 7;

    /**
    * Perment de declencher les différents événements sur une simulation 
    *
    * @param simulation : la simulation du jeu
    *
    * @return un tableau int de 2 case, la premiere correspond à l'événement actif pour les constructions (-1 si aucun n'est declenche), la 2eme correspond à l'événement des ressources
    */
	public static int[] event(Simulation simulation) {

		// On vide les listes remplie dans le tour precedent
		constructionsSaccagees.clear();
		ressourcesSaccagees.clear();

        int[] numEvenement = new int[2];

        ArrayList<Construction> constructions = simulation.getConstructions();
        ArrayList<Ressource> ressources = simulation.getRessources();
        Terrain terrain = simulation.getTerrain();

        // On choisis entre les différents événements des constructions
        int choix = new Random().nextInt(NOMBRE_EVENTS_CONSTRUCTIONS);

        switch(choix) {
            
            // O: Tremblement de terre
            case 0: catastropheNaturelle(constructions, 10, 35);
                numEvenement[ 0 ] = CATASTROPHE_NATURELLE_TREMBLEMENT_DE_TERRE;
            	break;

            // 1: Inondation
            case 1: catastropheNaturelle(constructions, 30, 45);
	            numEvenement[ 0 ] = CATASTROPHE_NATURELLE_INONDATION;
	            break;
           
            //  2: Incendie
            case 2:  numEvenement[ 0 ] = INCENDIE;
            	if(!incendie(constructions))
	           		numEvenement[ 0 ] = -1;
	            break;
            
            // 3: Saturation des hopitaux
            case 3: numEvenement[ 0 ] = SATURATION_HOPITAUX;
            	if(!saturationHopitaux(constructions))
            		numEvenement[ 0 ] = -1;
	            break;
        }

        // On choisis entre les différents événements des ressources
        choix = new Random().nextInt(NOMBRE_EVENTS_RESSOURCES);

        switch(choix) {
        
            // O: Feu de bois
            case 0:  numEvenement[ 1 ] = FEU_DE_BOIS;
            	if(!feuDeBois(ressources, terrain))
        	    	numEvenement[ 1 ] = -1;
            	break;

            // 1: Attaque d'animaux
            case 1: numEvenement[ 1 ] = ATTAQUE_ANIMAUX;
           		if(!attaqueAnimaux(ressources, terrain))
           			numEvenement[ 1 ] = -1;
            	break;

            // 2: Voler
            case 2: numEvenement[ 1 ] = VOLE;
            	if(!voler(ressources, terrain))
            		numEvenement[ 1 ] = -1;
            	break;
        }

        return numEvenement;
    }

    /**
    * Permet de declencher une catastrophe naturelle qui affecte une liste des constructions
    *
    * @param constructions : la liste des constructions
    * @param minDommage : le dommage min cause par la catasrophe
    * @param maxDommage : le dommage max cause par la catasrophe
    */
	private static void catastropheNaturelle(ArrayList<Construction> constructions, int minDommage, int maxDommage) {

		// Si la liste n'existe pas ou elle est vide on fait rien
		if(constructions == null || constructions.isEmpty())
			return;

		Random rand = new Random();

		// On parcourt les construction
		for(int i=0; i < constructions.size(); i++) {

			Construction constr = constructions.get(i);
			if(constr != null) {
				// On endommage la construction
				constr.endommage(rand.nextInt(maxDommage - minDommage) + minDommage);
				// Et on ajoute la construction affecte par la catastrophe dans la liste des constructions touchées
				//constructionsSaccagees.add(constr.clone());
				constructionsSaccagees.add(constr);

			}
		}
	}

	/**
	* Permet de declenche l'evenement d'incendie sur des constructions
	*
	* @param constructions : la liste des construction
	*
	* @return vrai si au moins un incendie est declenche
	*/
	private static boolean incendie(ArrayList<Construction> constructions) {

		// Si la liste n'existe pas ou elle est vide on fait rien
		if(constructions == null || constructions.isEmpty())
			return false;

		int minDommage = 50;
		int maxDommage = 70;

		Random rand = new Random();

		// On parcourt les construction
		for(int i = 0; i < constructions.size(); i++) {

			Construction constr = constructions.get(i);

			// Si la construction est soit une maison un hotel ou une cabane on les endommagent
			if(constr != null && constr.type.equals("Maison") || constr.type.equals("Hotel") || constr.type.equals("Cabane")) {
				constr.endommage(rand.nextInt(maxDommage - minDommage) + minDommage);

				// Et on ajoute la construction endommage a la liste des constructions endommages dans ce tour
				//constructionsSaccagees.add(constr.clone());
				constructionsSaccagees.add(constr);
			}

		}
		return !constructionsSaccagees.isEmpty();
	}

	/**
	* Permet de declenche l'evenement saturation d'hopitaux sur des constructions (hopitaux)
	*
	* @param constructions : la liste des construction
	*
	* @return vrai si au moins un hopital est sature
	*/
	private static boolean saturationHopitaux(ArrayList<Construction> constructions) {

		// Si la liste n'existe pas ou elle est vide on fait rien
		if(constructions == null || constructions.isEmpty())
			return false;

		int minDommage = 5;
		int maxDommage = 10;

		Random rand = new Random();

		// On parcourt les construction
		for(int i = 0; i < constructions.size(); i++) {

			Construction constr = constructions.get(i);

			// Si la construction est un hopital on l'endommage
			if(constr != null && constr.type.equals("Hopital")) {
				constr.endommage(rand.nextInt(maxDommage - minDommage) + minDommage);

				// Et on ajoute la construction endommage a la liste des constructions endommages dans ce tour
				//constructionsSaccagees.add(constr.clone());
				constructionsSaccagees.add(constr);
			}
		}
		return !constructionsSaccagees.isEmpty();
	}

	/**
	* Permet de declenche l'evenement feu de bois sur des ressources (bois)
	*
	* @param ressources : la liste des ressources
	* @param terrain : le terrain ou se trouvent les ressources
	*
	* @return vrai si au moins un arbre est brule
	*/
	private static boolean feuDeBois(ArrayList<Ressource> ressources, Terrain terrain) {

		// Si la liste ou le terrain n'existent pas ou ils sont vides on fait rien
		if(ressources == null || terrain == null || ressources.isEmpty())
			return false;

		double probaFeu = 0.15;

		// On parcourt les ressources
		for(int i = 0; i < ressources.size(); i++) {

			Ressource ress = ressources.get(i);

			// Si la ressources est du type bois et que la probabilite est verifie on brule l'arbre
			if(ress != null && ress.getX() != -1 && ress.type.equals("Bois") && Math.random() <= probaFeu) {
				ress.setQuantite(0);

				// On ajoute la ressource attaque dans la liste des ressources touchées
				addRessource(ress);

				// On actualise l'etat du terrain et de la liste
				updateRessources(ress, ressources, terrain);
			}
		}		
		return !ressourcesSaccagees.isEmpty();
	}

	/**
	* Permet de declenche l'evenement attaque d'animaux sur des ressources (bois et pierre)
	*
	* @param ressources : la liste des ressources
	* @param terrain : le terrain ou se trouvent les ressources
	*
	* @return vrai si une ressources est attaquee
	*/
	private static boolean attaqueAnimaux(ArrayList<Ressource> ressources, Terrain terrain) {
		
		// Si la liste ou le terrain n'existent pas ou ils sont vides on fait rien
		if(ressources == null || terrain == null || ressources.isEmpty())
			return false;

		// Pour melanger la liste 
		Collections.shuffle(ressources);
		Random rand = new Random();

		// On parcourt les ressources
		for(int i = 0; i <ressources.size(); i++) {

			Ressource ress = ressources.get(i);

			// Si la ressources est du type bois ou pierre, la ressource est attaquee
			if(ress != null && ress.getX() != -1 && (ress.type.equals("Bois") || ress.type.equals("Pierre"))) {
				
				int qte = rand.nextInt(ress.getQuantite());
				
				// On change la quantite restante de la ressource
				ress.setQuantite(ress.getQuantite() - qte);

				// On ajoute la ressource attaque dans la liste des ressources touchées
				addRessource(ress);

				// On actualise l'etat de la ressource dans le terrain et dans la liste
				updateRessources(ress, ressources, terrain);

				// Et c'est bon l'evenement a pris fin
				return true;
			}
		}	
		return false;	
	}

	/**
	* Permet de declenche l'evenement attaque d'animaux sur des ressources (fer)
	*
	* @param ressources : la liste des ressources
	* @param terrain : le terrain ou se trouvent les ressources
	*
	* @return vrai si une ressources est voler
	*/
	private static boolean voler(ArrayList<Ressource> ressources, Terrain terrain) {
		
		// Si la liste ou le terrain n'existent pas ou ils sont vides on fait rien
		if(ressources == null || terrain == null || ressources.isEmpty())
			return false;

		// Pour melanger la liste 
		Collections.shuffle(ressources);
		Random rand = new Random();

		// On parcourt les ressources
		for(int i = 0; i < ressources.size(); i++) {

			Ressource ress = ressources.get(i);

			// Si la ressources est du type fer, la ressource est voler
			if(ress != null && ress.getX() != -1 && ress.type.equals("Fer")) {
				
				int qte = rand.nextInt(ress.getQuantite());
				
				// On change la quantite restante de la ressource
				ress.setQuantite(ress.getQuantite() - qte);

				// On ajoute la ressource vole dans la liste des ressources touchées
				addRessource(ress);

				// On actualise l'etat de la ressource dans le terrain et dans la liste
				updateRessources(ress, ressources, terrain);

				// Et c'est bon l'evenement a pris fin
				return true;
			}
		}	
		return false;
	}

	/**
	* Permet d'actualise la ressources presenete dans le terrain et dans la liste
	*
	* @param ressource : la ressource a actualiser
	* @param ressources : la liste des ressources
	* @param terrain : le terrain des ressources
	*/
	private static void updateRessources(Ressource ressource, ArrayList<Ressource> ressources, Terrain terrain) {
        
        // Si la quantite de la ressources est epuisee on la supprime du terrain et de la liste
        if(ressource.getQuantite() <= 0) {
            terrain.videCase(ressource.getX(), ressource.getY());
            ressources.remove(ressource);
        }
    }

    /**
    * Permet d'ajouter une copie de la ressource touchee dans le tableau des ressources touchees par des evenements
    *
    * @param ressource : la ressource a ajouter
    */
    private static void addRessource(Ressource ressource) {
    	
    	// Si la ressource n'existe pas
		if(ressource == null)
			return;

		// Sinon on la clone et on l'ajoute
		Ressource cloneRes = null;

		if(ressource.type.equals("Bois")) 
			cloneRes = ((Bois) ressource).clone();

		else if(ressource.type.equals("Pierre"))
			cloneRes = ((Pierre) ressource).clone();

    	else if(ressource.type.equals("Fer"))
    		cloneRes = ((Fer) ressource).clone();	    		
    	
    	if(cloneRes != null)
    		ressourcesSaccagees.add(cloneRes);
    }

    /**
    * Accesseur de constructionsSaccagees
    *
    * @return le tableau des constructions endommages dans ce tour
    */
    public static ArrayList<Construction> getConstructionsSaccagees() {
    	return constructionsSaccagees;
    }

    /**
    * Accesseur de ressourcesSaccagees
    *
    * @return le tableau des constructions saccagees dans ce tour
    */
    public static ArrayList<Ressource> getRessourcesSaccagees() {
    	return ressourcesSaccagees;
    }
}