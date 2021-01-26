import java.util.Random;

/**
* @author Amayas SADI
* @author Hamid KOLLI
*
* Cette classe permet de gerer les ressources du type fer
*/
public class Bois extends Ressource {

	/**
	* Le nombre max du bois d'une case sur le terrain
	*/
	public static final int MAX_BOIS_ARBRE = 8;
	
	/**
	* Constructeur de bois
	*
	* @param quantite : la quantite de bois
	*/
	public Bois(int quantite) {

		super("Bois", quantite);

		if(quantite > MAX_BOIS_ARBRE)
			setQuantite(MAX_BOIS_ARBRE);

		else if (quantite < 0)
			setQuantite(0);
	}
	
	/**
	* Constructeur par defaut
	*/
	public Bois() {
		this(new Random().nextInt(MAX_BOIS_ARBRE - 1) + 1);
	}

	/**
	* Permet de cloner du bois 
	*
	* @return le bois cloner
	*/
	public Bois clone() {
		Bois b = new Bois(getQuantite());
		b.setPosition(getX(), getY());
		return b;
	}
}