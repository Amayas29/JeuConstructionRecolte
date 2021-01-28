import java.util.Random;

/**
* @author Amayas SADI
* @author Hamid KOLLI
*
* Cette classe permet de gerer les Planteur d'arbre
*/
public class Planteur extends Agent {

	/**
	* L'energie maxi d'un planteur
	*/
	public static final int MAX_ENERGIE = 30;

	/**
	* La variation de l'energie du planteur
	*/
	public static final int VARIATION_ENERGIE = 5;

	/**
	* Le nombre de planteur créer, permet de representer les id
	*/
	private static int nombrePlanteur = 0;

	/**
	* Constructeur de Planteur
	* 
	* @param x : l'ordonne du planteur
	* @param y : l'abscisse du planteur
	*/
	public Planteur(int x, int y) {
		super(++nombrePlanteur, MAX_ENERGIE, x, y);
	}

	/**
	* Constructeur par copie de planteur
	*
	* @param planteur : le planteur a cloner
	*/
	public Planteur(Planteur planteur) {
		super(planteur, ++nombrePlanteur);
	}

	/**
    * Permet au planteur de planter un arbre ou entretenir
    *
    * @param terrain : le terrain ou le planteur travaille
    */
	public void travailler(Terrain terrain) {

		Random rand = new Random();

		// Si il ne peut pas travailler, il se repose
		if(!peutTravailler()) {
			setEnergie(rand.nextInt(MAX_ENERGIE - MIN_ENERGIE) + MIN_ENERGIE);
			return;
		}

		// Sinon il travaille 
		Ressource ressource = terrain.getCase(x, y);

		Bois arbre = new Bois();

		// Si la case du planteur n'a pas de ressources, alors il plante un arbre
		if(ressource == null)
			terrain.setCase(x, y, arbre);

		// Sinon on chercher la case vide ou celle qui contient du bois la plus proche du planteur
		// Pour planter un arbre ou bien entretenir
		else {

			if(ressource.type.equals("Bois"))
				Evenement.regenerer(ressource);
			
			else {
				
				double minDistance = Double.POSITIVE_INFINITY;
				Ressource ressourceMin = null;
				int _x = 0, _y = 0;
				double dist;

				// Chercher la case la plus proche
				for(int i = 0; i < terrain.nbLignes; i++) {

					for(int j = 0; j < terrain.nbColonnes; j++) {

						ressource = terrain.getCase(i, j);

						dist = distance(i, j);
						
						if( dist < minDistance && (ressource == null || ressource.getClass().getName().equals("Bois"))) {
							minDistance = dist;
							ressourceMin = ressource;
							_x = i;
							_y = j;
						}
					}
				}	

				ressource = ressourceMin;

				// Si on a trouve au moins une case
				// On plante ou on entretient
				if(minDistance != Double.POSITIVE_INFINITY) {

					if(ressource == null)
						terrain.setCase(_x, _y, arbre);

					else if(ressource.type.equals("Bois"))
						Evenement.regenerer(ressource);
				}
			}
		}

		// Le planteur se fatigue 
		energie -= VARIATION_ENERGIE;	

		// 	Le planteur se déplace vers une autre case
		seDeplacer(rand.nextInt(terrain.nbLignes), rand.nextInt(terrain.nbColonnes));
	}

	/**
	* Accesseur pour modifier l'energie 
	*
	* @param energie : la nouvelle energie
	*/
	public void setEnergie(int energie) {
		super.setEnergie(energie, MAX_ENERGIE);
	}
}