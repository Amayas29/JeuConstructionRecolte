import java.util.HashMap;

/**
* @author Amayas SADI
* @author Hamid KOLLI
*
* Cette classe permet de gerer les construction créer
*/
public final class Construction {

	/**
	* L'etat initial d'une construction
	*/
	private static final int INITIAL_ETAT = 100;

	// On a limiter les types des constructions a 5 type.
	// les exigences de chaque type sont representer par un tableau d'objet,

	/**
	* Les exigences de la cabane, sous la forme
	*
	* [GAIN D'EXPERIENCE, EXPERIENCE DEMANDEE, type(i), quantite(i), ...]
	* avec type(i), quantite(i) sont respectivement le type de la ressource demandee et sa quantitee
	*/
	private static final Object[] exigencesCabane  = {3, 0, "Bois", 5};

	/**
	* Les exigences de la maison, sous la forme
	*
	* [GAIN D'EXPERIENCE, EXPERIENCE DEMANDEE, type(i), quantite(i), ...]
	* avec type(i), quantite(i) sont respectivement le type de la ressource demandee et sa quantitee
	*/
	private static final Object[] exigencesMaison  = {5, 10, "Bois", 8, "Pierre", 4};

	/**
	* Les exigences du marche, sous la forme
	*
	* [GAIN D'EXPERIENCE, EXPERIENCE DEMANDEE, type(i), quantite(i), ...]
	* avec type(i), quantite(i) sont respectivement le type de la ressource demandee et sa quantitee
	*/
	private static final Object[] exigencesMarche  = {5, 15, "Bois", 10, "Pierre", 8};
	
	/**
	* Les exigences de l'hotel, sous la forme
	*
	* [GAIN D'EXPERIENCE, EXPERIENCE DEMANDEE, type(i), quantite(i), ...]
	* avec type(i), quantite(i) sont respectivement le type de la ressource demandee et sa quantitee
	*/
	private static final Object[] exigencesHotel   = {5, 20, "Bois", 15, "Pierre", 10, "Fer", 5};
	
	/**
	* Les exigences de l'hopital, sous la forme
	*
	* [GAIN D'EXPERIENCE, EXPERIENCE DEMANDEE, type(i), quantite(i), ...]
	* avec type(i), quantite(i) sont respectivement le type de la ressource demandee et sa quantitee
	*/
	private static final Object[] exigencesHopital = {5, 25, "Bois", 20, "Pierre", 15, "Fer", 10};

	/**
	* Le type de la construction 
	*/
	public final String type;

	/**
	* L'etat de la construction
	*/
	private int etat;

	/**
	* L'ordonne de la construction
	*/
	private int x;

	/**
	* L'abscisse de la construction
	*/
	private int y;

	/**
	* Constructeur locale
	*
	* @param type : le type de la construction
	* @param x : L'ordonne de la construction
	* @param y : L'abscisse de la construction
	*/
	private Construction(String type, int x, int y) {
		this.type = type;
		this.etat = INITIAL_ETAT;
		this.x = x;
		this.y = y;
	}

	/**
	* Retourne une nouvelle construction
	*
	* @param type : le type de la construction
	* @param x : L'ordonne de la construction
	* @param y : L'abscisse de la construction
	* @param stock : le stock de ressources disponibles
	*
	* @return Une nouvelle construction si y suffisamment de ressources sinon null  
	*/
	public static Construction getConstruction(String type, int x, int y, HashMap<String, Integer> stock) {

		// Si le stock est vide ou n'existe pas il ne creer rien
		if(stock == null || stock.isEmpty())
			return null;

		int i = 2;

		// On reccupere les exigences du type
		Object[] exigences = Construction.getExigences(type);
		
		// Le type est invalide on creer rien
		if(exigences == null) 
			return null;

		// On parcours les exigences et on les verifient
		while(i < exigences.length) {
			
			String typeExige = (String)exigences[ i++ ];
			int qteExigee = (int)exigences[ i++ ];

			// Si la ressources demande n'existe pas on creer rien
			if(!stock.containsKey(typeExige))
				return null;

			Integer qteDispo = stock.get(typeExige);
			
			// Si la quantite est insuffisante on creer rien
			if(qteDispo == null || qteDispo < qteExigee)
				return null;
		}

		return new Construction(type, x, y);
	}

 	/**
	* Permet d'endommager la construction
	* 
	* @param dmg : le dommage a infliger 
	*/
 	public void endommage(int dmg) {
 		if(dmg > 0)
 			etat -= dmg;
 	}

	/**
	* Retourne les exigences du type sous la forme
	* [GAIN D'EXPERIENCE, EXPERIENCE DEMANDEE, type(i), quantite(i), ...]
	* avec type(i), quantite(i) sont respectivement le type de la ressource demandee et sa quantitee
	*
	* @param type : le type de la construction
	*
	* @return le tableau d'exigences du type si il est valide sinon null
	*/
 	public static Object[] getExigences(String type) {

 		if(type.equals("Cabane"))
			return exigencesCabane;

		else if(type.equals("Maison")) 
			return exigencesMaison;

		else if(type.equals("Marche"))
			return exigencesMarche;

		else if(type.equals("Hotel"))
			return exigencesHotel;

		else if(type.equals("Hopital"))
			return exigencesHopital;

		else
			return null;
 	}

	/**
	* Permet de cloner une construction
	*
	* @return un clone de la construction
	*/
 	public Construction clone() {
 		
 		Construction c = new Construction(type, x, y);
 		c.etat = etat;
 		return c;
 	}

 	/**
	* Permet de representer la construction
	*
	* @return type de la construction
	*/
 	public String toString() {
 		return type;
 	}

 	/**
	* Accessuer d'etat
	*
	* @return l'etat de la construction
	*/
 	public int getEtat() {
 		return etat;
 	}
	
	/**
	* Accesseur de X
	*
	* @return L'ordonne de la construction
	*/
 	public int getX() {
 		return x;
 	}

 	/**
	* Accesseur de Y
	*
	* @return L'abscisse de la construction
	*/
 	public int getY() {
 		return y;
 	}
 }