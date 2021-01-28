import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
* @author Amayas SADI
* @author Hamid KOLLI
*
* Cette classe permet de gerer la simulation du jeu
*/
public class Simulation {

	/**
	* Le nombre de lignes du terrain de construction
	*/
	public static final int NOMBRE_LIGNE_CONSTRUCTION = 20;

	/**
	* Le nombre de colonnes du terrain de construction
	*/
	public static final int NOMBRE_COLONNES_CONSTRUCTION = 20;

	/**
	* L'experience courrante du jeu
	*/
	private int exp;

	/**
	* La listes des constructions
	*/
	private ArrayList<Construction> constructions;

	/**
	* Le stock des ressources
	*/
	private HashMap<String, Integer> stock;

	/**
	* La listes des ressources pressentes dans le terrain
	*/
	private ArrayList<Ressource> ressources;

	/**
	* Le terrain des ressources
	*/
	private Terrain terrain;

	/**
	* La liste des agents
	*/
	private ArrayList<Agent> agents;

	/**
	* Constructeur de simulation
	*
	* @param nombreLignesRecolte : le nombre de ligne du terrain des ressources
	* @param nombreColonnesRecolte : le nombre de colonnes du terrain des ressources
	*/
	public Simulation(int nombreLignesRecolte, int nombreColonnesRecolte) {
		this.exp = 0;
		constructions = new ArrayList<Construction>();
		stock = new HashMap<String, Integer>();
		ressources = new ArrayList<Ressource>();
		terrain = new Terrain(nombreLignesRecolte, nombreColonnesRecolte);
		agents = new ArrayList<Agent>();
	}

	/**
	* Permet d'ajouter la quantite d'un ressource au stock 
	*
	* @param ressourceType : Le type de la ressource
	* @param qte : la quantite de la ressource a ajouter
	*/
	public void addRessource(String ressourceType, int qte) {
		
		// Si le type n'est pas valide on n'ajoute pas
		if(ressourceType == null || !(ressourceType.equals("Bois") ||
			 ressourceType.equals("Pierre") || ressourceType.equals("Fer")))
			return;

		// Si le type existe deja dans notre stock on mis a jour seulement sa quantite
		if(stock.containsKey(ressourceType)) {
			Integer qteDispo = stock.get(ressourceType);
			
			if(qteDispo == null)
				qteDispo = 0;

			stock.replace(ressourceType,qteDispo + qte);
		}
		// Sinon on ajoute le type et la quantite
		else 
			stock.put(ressourceType, qte);
	}

	/**
	* Permet de mettre a jour la simulation (faire travailler les agents, nettoyer les terrains)
	*/
	public void update() {

		// Si la liste des constructions n'existe pas on sort
        if(constructions == null )
            return;

        // Sinon on parcours les constructions et on supprime celles dont l'etat est <= 0
        for(int i = 0; i < constructions.size(); i++) {

        	Construction constr = constructions.get(i);
            if(constr.getEtat() <= 0) {

                Object[] exigences = Construction.getExigences(constr.type);
                
                if(exigences != null)
                    setExp(exp - (int) (0.5 * (int)exigences[0]));

                constructions.remove(constr);
            }
        }

        // Si la liste des ressources n'existe pas on sort
        if ( ressources == null )
            return;

        // Sinon on supprime les ressources qui ont une quantite <= 0 ou qui sont pas dans le terrain
        for(int i = 0;i < ressources.size();i++) {

            if(ressources.get(i).getX() == -1 || ressources.get(i).getQuantite() <= 0)
                ressources.remove(ressources.get(i));
        }

        // Si la liste des agents n'existe pas on sort
        if( agents == null)
            return;

		// Sinon on fait travailler les agents        
        for(int i = 0; i < agents.size(); i++) {
			
			Agent ag = agents.get(i);

			if(ag.getClass().getName().equals("Planteur")) {
				if(Math.random() < 0.2)
            		ag.travailler(terrain);
			}
			else
            	ag.travailler(terrain);

            // Si c'est le recolteur on ajoute les recoltes dans le stock
            if(ag.getClass().getName().equals("Recolteur"))
                addRessource(((Recolteur)ag).getTypeRecolte(), ((Recolteur)ag).getQteRecolte());
        }
    }  

	/**
	* Permet d'initialiser une simulation
	*
	* @param nombreAgents : le nombre d'agents initials
	* @param nombreRessources : le nombre de ressources initiales
	*/
	public void intiSimulation(int nombreAgents, int nombreRessources) {

        Random rand  = new Random();

        // Au minimum 2 agents
        agents.add(new Recolteur(rand.nextInt(terrain.nbLignes),rand.nextInt(terrain.nbColonnes)));
        agents.add(new Planteur(rand.nextInt(terrain.nbLignes),rand.nextInt(terrain.nbColonnes)));
        
        // On creer les agents et on les ajoute dans la liste des agents
		for(int i = 0; i < nombreAgents; i++) {
			
			if(Math.random() < 0.5)
				agents.add(new Recolteur(rand.nextInt(terrain.nbLignes),rand.nextInt(terrain.nbColonnes)));
			else
				agents.add(new Planteur(rand.nextInt(terrain.nbLignes),rand.nextInt(terrain.nbColonnes)));
		}

		// Au moins 3 ressources
		if(nombreRessources <= 0)
			nombreRessources = 3;

		// On cree les ressources et on les ajoute a la liste des ressources et au terrain
		for(int i = 0; i < nombreRessources; i++) {
			
			Ressource ressource;
                       
            if(Math.random() < 0.2)
               ressource =  new Fer();
            
            else if (Math.random() < 0.5)
                ressource = new Pierre();
            
            else
                ressource = new Bois();

          	ressources.add(ressource);
        	terrain.setCase(rand.nextInt(terrain.nbLignes),
        		rand.nextInt(terrain.nbColonnes), ressource);
		}
	}

	/**
	* Erreur correspondante aux positions donnes lors de la construction
	*/
    public static final int ERREUR_POSITION = 0;

    /**
	* Erreur correspondante à la construction dans une case deja occupee
	*/
    public static final int ERREUR_CASE_OCCUPEE = 1;

    /**
	* Erreur correspondante ay type invalide de la construction
	*/
    public static final int ERREUR_TYPE_INVALIDE = 2;

    /**
	* Erreur correspondante au manque d'experience pour construire la construction 
	*/
    public static final int ERREUR_EXP_INSUFFISANT = 3;

    /**
	* Erreur correspondante au manque de ressources pour construire la construction 
	*/
    public static final int ERREUR_RESSOURCES_INSUFFISANTE= 4;
    
     /**
	* Erreur correspondante à la démolition d'une case vide 
	*/
    public static final int ERREUR_CASE_VIDE = 5;

    /**
	* Succes de la creation
	*/
    public static final int SUCCES = 6;

    /**
    * Permet de construire une construction
	*
	* @param type : le type de la construction a construire
	* @param x : l'ordonne de la constrction
	* @param y : l'abscisse de la construction
	*
	* @return le resultat de l'operation decrit par les constantes d'erreurs et de succes
    */
    public int construire(String type, int x, int y) {

    	// Cas positions non valide
		if(x < 0 || x >= NOMBRE_LIGNE_CONSTRUCTION || y < 0 || y >= NOMBRE_COLONNES_CONSTRUCTION)
			return ERREUR_POSITION;

		// Cas de case deja occupee
		if(getConstructionAt(x, y) != null) 
			return ERREUR_CASE_OCCUPEE;

		Object[] exigences = Construction.getExigences(type);

		// Cas de type invalide
		if(exigences == null)
			return ERREUR_TYPE_INVALIDE;

		// Cas de manque d'experience
		if(exp < (int)exigences[1])
			return ERREUR_EXP_INSUFFISANT;

		Construction construction = Construction.getConstruction(type, x, y, stock);

		// Cas de manque de ressources
		if(construction == null)
			return ERREUR_RESSOURCES_INSUFFISANTE;

		// Cas de succes et d'ajout
		constructions.add(construction);

		// Mettre a jour le stock des ressources
		int i = 2;
		
		while(i < exigences.length) {
			
			String typeExige = (String) exigences[ i++ ];
			
			Integer qteDispo = stock.get(typeExige);
			
			stock.replace(typeExige, qteDispo - (int)exigences[ i++ ]);
		}

		// Obtenir un gain d'experience 
		setExp(exp + (int)exigences[0]);

		return SUCCES;
    }

    /**
    * Permet de demolir un construction
    *
    * @param x : ordonne de la construction a demolir
    * @param y: abscisse de la construction a demolir
    *
    * @return l'etat de l'operation 
    */
    public int demolir(int x, int y) {

    	// Cas de positions invalide
    	if(x < 0 || x >= NOMBRE_LIGNE_CONSTRUCTION || y < 0 || y >= NOMBRE_COLONNES_CONSTRUCTION)
			return ERREUR_POSITION;

		Construction construction = getConstructionAt(x, y);

		// Cas de case vide
    	if(construction == null)
    		return ERREUR_CASE_VIDE;

    	// Cas de succes
    	constructions.remove(construction);
    	String type = construction.type;

    	Object[] exigences = Construction.getExigences(type);

    	// On gange 2/3 des ressources exigees apres demolition
    	int i = 2;

    	while(i < exigences.length) {

    		String typeExige = (String)exigences[ i++ ];
    		Integer qteDispo = stock.get(typeExige);
    		stock.replace(typeExige, (int) (qteDispo + 2.0/3.0 * (int)exigences[ i++ ]));
    	}

    	return SUCCES;
    }

    /**
    * Permet d'affiche la'etat de la simulation
    */
    public void afficher() {
    	
    	if(terrain == null)
    		return;

    	terrain.affiche();
    	afficheQuantiteTerrain();
		afficherMapConstruction();
		System.out.println(this);
		afficherAgents();
		afficherStock();
    }

    /**
    * Permet d'afficher les quantite de ressources dans le terrain
    */
    private void afficheQuantiteTerrain() {

		if(terrain == null)
			return;

		HashMap<String,Integer> ressources = new HashMap<String,Integer>();

		// On parcours toutes les cases du terrain
		for(int i = 0; i < terrain.nbLignes; i++) {
			
			for(int j = 0; j < terrain.nbColonnes; j++) {

				if(terrain.caseEstVide(i,j))
					continue;

				// On ajoute la quantite de la case dans les ressources presentes
				Ressource ress = terrain.getCase(i,j);
				
				String type  = ress.type;
				int qteRecolte = ress.getQuantite();

				if(ressources.containsKey(type)) {

					Integer qteDispo = ressources.get(type);
					
					if(qteDispo == null)
						qteDispo = 0;

					ressources.replace(type,qteRecolte + qteDispo);

					continue;
				}
				
				ressources.put(type, qteRecolte);
			}
		}

		String displayQteTerrain = "";

		for(Map.Entry ress : ressources.entrySet()) {
			if(ress.getValue() != null && (int)ress.getValue() != 0)
				displayQteTerrain += "\t\t" + ress.getKey() + " : " + ress.getValue() + "\n";
		}

		// Si il ya aucun ressources dans le terrain
		if(displayQteTerrain.equals(""))
			System.out.println(" \tLe terrain est vide ");

		else
			System.out.println("\tLes ressources du terrain : \n" + displayQteTerrain);
    }

    /**
    * Permet d'affiche le terrain de construction
    */
	private void afficherMapConstruction() {

		String sepVer = ":";
        String sepHor = "";
        String entete = "    ";

        for (int i = 0; i < 5; i++) {
            sepHor += "-";
        }

        for (int j = 0; j < NOMBRE_COLONNES_CONSTRUCTION; j++) {
            sepVer = sepVer + sepHor + ":";
            entete += String.format("%5d", j) + " ";
        }

        String map = entete + "\n    " + sepVer + "\n";
      
        for (int i = 0; i < NOMBRE_LIGNE_CONSTRUCTION; i++) {

        	map += String.format("%3d", i) + " ";
            for (int j = 0; j < NOMBRE_COLONNES_CONSTRUCTION; j++) {

            	Construction constr = getConstructionAt(i, j);
                
                if (constr == null)
                    map += "|" + String.format("%-5s", " ");
               
                else
                    map += "|" + String.format("%-5s", getConstructionAt(i, j)).substring(0, 5);
                
            }
            map += "|\n    " + sepVer + "\n";
        }
        System.out.println(map);
        System.out.println("\t\t\t\t\t\t\t" + NOMBRE_LIGNE_CONSTRUCTION + " * " + NOMBRE_COLONNES_CONSTRUCTION);
    }

    /**
    * Permet d'afficher l'experience courrante de la simulation
    *
    * @return une chaine qui decrit l'experience de la simulation
    */
    public String toString() {
    	return "\n\tVous avez " + exp + " points d'experiences\n";
    }

    /**
    * Permet d'afficher le nombre d'agents presents dans le terrain
    */
    private void afficherAgents() {

    	int nbPlanteur = 0;
    	int nbRecolteur = 0;

    	if(agents == null)
    		return;

    	// On parcous les agents et on calcule pour chaque type le nombre d'agents presents
    	for(Agent ag : agents) {

    		if(ag.getClass().getName().equals("Planteur"))
    			nbPlanteur++;

    		else if(ag.getClass().getName().equals("Recolteur"))
    			nbRecolteur++;
    	}

    	System.out.println("\tVoici vous agents : ");
    	System.out.println("\t\t - " + nbPlanteur + " planteurs");
    	System.out.println("\t\t - " + nbRecolteur + " recolteurs");
    	System.out.println("");
    }

    /**
    * Permet d'afficher le stock
    */
    private void afficherStock() {
    	
    	if(stock == null)
    		return;

    	String displayStock = "";

    	// On parcours le stock
    	for (Map.Entry m : stock.entrySet()) 
    		// Si la valeur de la ressources =! 0
    		if(m.getValue() != null && (int) m.getValue() != 0)
            	displayStock += "\t\t" + m.getKey() + " : " + m.getValue() + "\n";

        // Si le stock est pas vide
        if(displayStock.equals(""))
    		System.out.println("\tVous n'avez aucune ressource\n");

    	// Sinon on l'affiche
        else
    		System.out.println("\tVotre stock de ressource : \n" + displayStock);
    }

    /**
    * Permet de construire le catalogue des construction possible avec l'experience courrante
    *
    * @return Le catalogue des constructions debloqueés avec l'experience courrante
    */
    public String getCatalogue() {
    	
    	String types[] = {"Cabane", "Maison", "Marche", "Hotel", "Hopital"};
    	Object[] exigences;
    	String catalogue = "";

    	// On parcours les types de constructions
    	for(String type: types) {
    		exigences = Construction.getExigences(type);

    		if(exigences == null)
    			continue;

    		// Si l'experience est suffisante pour construire cette construction on l'ajoute au catalogue
    		if(exp >= (int)exigences[1]) {
    			catalogue += "\t\t" + type + ". Exp : " + exigences[1] + ", necessite : \n";
    			
    			int i = 2;
    			while(i < exigences.length) 
    				catalogue += "\t\t" + exigences[i++] + " : " + exigences[i++] + "\n";

    			catalogue += "\n";
    		}
       	}

    	return catalogue;
    }

    /**
    * Permet d'afficher les type et la quantite des ressources recoltees lors d'un tour
    */
 	public void afficherRessourcesRecoltees() {

		HashMap<String, Integer> ressourcesRecoltees = new HashMap<String, Integer>();
		
		if(agents == null)
			return;
	
		// On parcours les agents
		for(int i = 0; i < agents.size(); i++) {
		
			Agent ag = agents.get(i);

			// Si l'agent n'est pas un recolteur on passe au suivant
			if(ag == null || !ag.getClass().getName().equals("Recolteur"))
				continue;

			Recolteur rec = (Recolteur)ag;
				
			String ressourceType = rec.getTypeRecolte();
			int qte = rec.getQteRecolte();
						
			// Si l'agent a fait une recolte
			if(ressourceType == null || qte == 0)
				continue;

			// si le type de ressource existe dans les ressources recoltees on change que sa quantite
			if(ressourcesRecoltees.containsKey(ressourceType)) {
						
				Integer qteDispo = ressourcesRecoltees.get(ressourceType);
				
				if(qteDispo == null) 
					qteDispo = 0;
				
				ressourcesRecoltees.replace(ressourceType,qteDispo + qte);
			}
			// Sinon on ajoute le nouveau type ainsi que sa quantite
			else 
				ressourcesRecoltees.put(ressourceType, qte);
		}
		
		String displayQteRecoltees = "";

		// On parcours les ressources recoltees
	    for (Map.Entry m : ressourcesRecoltees.entrySet()) 

	    	// Si la quantire != 0 on l'affiche
	       	if(m.getValue() != null && (int) m.getValue() != 0 )
	          	displayQteRecoltees += "\t\t" + m.getKey() + " : " + m.getValue() + "\n";

	    // Si il ya aucune ressource recoltees
	    if(displayQteRecoltees.equals("")) 
			System.out.print(" \tAucune récolte n'a ete faite par vos recolteurs\n");

	   	else
		   	System.out.println("\tLes ressources recoltees :\n" + displayQteRecoltees);
    }

    /**
    * Retourne la construction se trouvant dans la position x y
    *
    * @param x : l'ordonne de la position voulue
    * @param y : l'abscisse de la position voulue
    *
    * @return La construction se trouvant dans la position (x, y) si elle existe, null sinon
    */
	public Construction getConstructionAt(int x, int y) {
		
		// Les positions sont invalides
		if(x < 0 || x >= Simulation.NOMBRE_LIGNE_CONSTRUCTION || y < 0 || y >= Simulation.NOMBRE_COLONNES_CONSTRUCTION)
			return null;

		// On parcours les constructions
		for(Construction constr : constructions) {

			// Si la construction se trouve a la position x y on la retourne
			if(constr != null && constr.getX() == x && constr.getY() == y) 
				return constr;
		}

		return null;
	}

	/**
	* Accesseur de l'experience
	*
	* @return l'experience courrante de la simulation
	*/
    public int getExp() {
		return exp;
	}

	/**
	* Accesseur pour modifier l'experience
	*
	* @param exp : la nouvelle experience  
	*/
	public void setExp(int exp) {
		this.exp = exp < 0 ? 0 : exp;
	}

	/**
	* Accesseur de constructions
	*
	* @return la liste des constructions
	*/
	public ArrayList<Construction> getConstructions() {
		return constructions;
	}

	/**
	* Accesseur de stock
	*
	* @return le stock de ressources courrant
	*/
	public HashMap<String, Integer> getStock() {
		return stock;
	}

	/**
	* Accesseur de ressources
	*
	* @return la liste des ressources courrante dans le terrain de ressources
	*/
	public ArrayList<Ressource> getRessources() {
		return ressources;
	}

	/**
	* Accesseur de terrain
	*
	* @return le terrain de ressources
	*/
	public Terrain getTerrain() {
		return terrain;
	}

	/**
	* Accesseur de agents
	*
	* @return la liste des agents dans la simulation
	*/
	public ArrayList<Agent> getAgents() {
		return agents;
	}

}