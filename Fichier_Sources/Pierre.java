import java.util.Random;

/**
* @author Amayas SADI
* @author Hamid KOLLI
*
* Cette classe permet de gerer les ressources du type pierre
*/
public class Pierre extends Ressource {

	/**
	* Le nombre max des pierre d'une case sur le terrain
	*/
	public static final int MAX_PIERRE_CARRIERE = 6;
	
	/**
	* Constructeur de pierre
	*
	* @param quantite : la quantite de pierre
	*/
	public Pierre(int quantite) {

		super("Pierre", quantite);

		if(quantite > MAX_PIERRE_CARRIERE)
			setQuantite(MAX_PIERRE_CARRIERE);

		else if (quantite < 0)
			setQuantite(0);
	}

	/**
	* Constructeur par defaut
	*/
	public Pierre() {
		this(new Random().nextInt(MAX_PIERRE_CARRIERE - 1) + 1);
	}

	/**
	* Permet de cloner une pierre 
	*
	* @return la pierre cloner
	*/
	public Pierre clone() {
		Pierre p = new Pierre(getQuantite());
		p.setPosition(getX(), getY());
		return p;
	}

}