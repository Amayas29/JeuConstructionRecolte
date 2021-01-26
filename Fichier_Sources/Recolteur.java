import java.util.Random;

/**
* @author Amayas SADI
* @author Hamid KOLLI
*
* Cette classe permet de gerer les Recolteur de ressources
*/
public class Recolteur extends Agent {

	/**
	* L'energie max d'un Recolteur
	*/
	public static final int MAX_ENERGIE = 20;

	/**
	* La variation de l'energie du Recolteur
	*/
	public static final int VARIATION_ENERGIE = 3;

	/**
	* Le nombre de Recolteur créer, permet de representer les id
	*/
	private static int nombreRecolteurs = 0;

	/**
	* La quantite recoltee en un tour
	*/
	private int qteRecolte;

	/**
	* Le type de recolte en un tour
	*/
	private String typeRecolte;

	/**
	* Constructeur de recolteur
	* 
	* @param x : l'ordonne du recolteur
	* @param y : l'abscisse du recolteur 
	*/
	public Recolteur(int x, int y) {
		
		super(++nombreRecolteurs, MAX_ENERGIE, x, y);

		qteRecolte = 0;
		typeRecolte = null;
	}

	/**
	* Constructeur par copie du recolteur
	*
	* @param recolteur : le recolteur a cloner
	*/
	public Recolteur(Recolteur recolteur) {	

		super(recolteur, ++nombreRecolteurs);

		qteRecolte = 0;
		typeRecolte = null;
	}

	/**
    * Permet au recolteur de recolter une ressource
    *
    * @param terrain : le terrain ou le recolteur travaille
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

		// On chercher la ressource la plus proche du recolteur
		if(ressource == null) {
			
			double minDistance = Double.POSITIVE_INFINITY;
			Ressource ressourceMin = null;

			// Chercher la case la plus proche
			for(int i = 0; i < terrain.nbLignes; i++) {

				for(int j = 0; j < terrain.nbColonnes; j++) {

					ressource = terrain.getCase(i, j);

					if(ressource == null)
						continue;

					double dist = distance(ressource.getX(), ressource.getY());
					
					if( dist < minDistance ) {
						minDistance = dist;
						ressourceMin = ressource;
					}
				}
			}	
			ressource = ressourceMin;		
		}

		// Le recolteur se fatigue 
		energie -= VARIATION_ENERGIE;

		// Si on trouve pas de ressources, on initilaise les attributs
		if(ressource == null) {
			qteRecolte = 0;
			typeRecolte = null;
			return;
		}

		// Sinon on recolte ce qu'on a trouve
		qteRecolte = rand.nextInt(ressource.getQuantite());

		typeRecolte = ressource.type;

		ressource.setQuantite(ressource.getQuantite() - qteRecolte);
			
		// Si la quantite de la ressource sur le terrain est epuisee, elle disparait
		if(ressource.getQuantite() <= 0) 
			terrain.videCase(x, y);

		// Le recolteur se déplace vers une autre case
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

	/**
	* Accesseur pour le type recolte
	*
	* @return le type recolte
	*/
	public String getTypeRecolte() {
		return typeRecolte;
	}

	/**
	* Accesseur pour la quantite recoltee
	*
	* @return la quantite recoltee
	*/
	public int getQteRecolte() {
		return qteRecolte;
	}
}